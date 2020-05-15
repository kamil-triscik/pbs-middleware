package com.pbs.middleware.server.features.filetransfer.upload.status;

import com.pbs.middleware.server.common.utils.Optional;
import com.pbs.middleware.server.features.filetransfer.upload.domain.Upload;
import com.pbs.middleware.server.features.filetransfer.upload.events.FileUploaded;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadFailed;
import com.pbs.middleware.server.features.filetransfer.upload.factory.UploadFactory;
import com.pbs.middleware.server.features.filetransfer.upload.listeners.UploadPublisher;
import com.pbs.middleware.server.features.filetransfer.upload.notification.UploadEmailFactory;
import com.pbs.middleware.server.features.filetransfer.upload.service.Uploader;
import com.pbs.middleware.server.features.filetransfer.upload.status.repository.UploadFileStatus;
import com.pbs.middleware.server.features.filetransfer.upload.status.repository.UploadStatus;
import com.pbs.middleware.server.features.filetransfer.upload.status.service.UploadStatusService;
import com.pbs.middleware.server.features.notification.email.domain.Email;
import com.pbs.middleware.server.features.notification.email.service.EmailService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadsJob {

    @Value("${middleware.server.features.transfer.upload.job.period:300}")
    private Long uploadPeriod;

    @NonNull
    private final UploadStatusService service;

    @NonNull
    private final Uploader uploader;

    @NonNull
    private final UploadFactory uploadFactory;

    @NonNull
    private final UploadEmailFactory emailFactory;

    @NonNull
    private final EmailService emailService;

    @NonNull
    private final UploadPublisher publisher;

    @PostConstruct
    private void init() {
        log.info("Upload job configured with period {} seconds", uploadPeriod);
    }

    @Scheduled(fixedDelayString = "${middleware.server.features.transfer.upload.job.period:300}000")
    public void checkUploads() {
        reUpload();
        notifyAdmin();
        removeOldOne();
    }

    private void reUpload() {
        service.getAll().forEach(failedUpload ->
                uploadFactory.find(failedUpload.getId()).ifPresentOrElse(upload -> {
                    failedUpload.getFiles().removeIf(file -> {
                        try {
                            log.info("Uploading[{}] file {}!", upload.getId(), file.getFilename());
                            final String tempFileId = upload.getFiles().get(file.getFilename()).getTempId();
                            uploader.uploadFile(upload, file.getFilename(), tempFileId);
                            publisher.publish(new FileUploaded(upload.getId(), file.getFilename(), upload.getFiles().get(file.getFilename()).getSize()));
                            return true;
                        } catch (Exception e) {
                            log.error("Upload[" + upload.getId() + "] failed again for file " + file.getFilename(), e);
                            file.setError(ExceptionUtils.getStackTrace(e));
                            return false;
                        }
                    });
                    if (failedUpload.getFiles().isEmpty()) {
                        service.delete(failedUpload.getId());
                    } else {
                        service.update(failedUpload);
                    }
                }, () -> removeUnlinked(failedUpload)));
    }

    private void notifyAdmin() {
        List<UploadStatus> failed = service.getOldAndNotNotified();
        if (!failed.isEmpty()) {
            Map<UUID, Map<String, String>> exceptions = new HashMap<>();
            failed.forEach(failedUpload ->
                    exceptions.put(
                            failedUpload.getId(),
                            failedUpload.getFiles().stream().collect(Collectors.toMap(UploadFileStatus::getFilename, UploadFileStatus::getError))));

            List<Upload> uploads = failed
                    .stream()
                    .map(UploadStatus::getId)
                    .map(uploadFactory::find)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            Email email = emailFactory.get(uploads, exceptions);
            try {
                emailService.send(email);
                service.uploadNotified(failed.stream().map(UploadStatus::getId).collect(Collectors.toList()));
            } catch (Exception e) {
                log.error("Upload failed notification sending failed.", e);
            }
        }
    }

    private void removeOldOne() {
        service.getAllAfterTimeout().forEach(failedUpload -> uploadFactory.find(failedUpload.getId())
                .ifPresentOrElse(upload -> {
                    publisher.publish(new UploadFailed(upload.getId()));
                    service.delete(failedUpload.getId());
                }, () -> removeUnlinked(failedUpload)));
    }

    private void removeUnlinked(UploadStatus uploadStatus) {
        log.warn("Removing failed upload [{}] without upload process!", uploadStatus.getId());
        service.delete(uploadStatus.getId());
    }

}
