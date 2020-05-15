package com.pbs.middleware.server.features.filetransfer.download.status;

import com.pbs.middleware.server.common.utils.Optional;
import com.pbs.middleware.server.features.filetransfer.download.domain.Download;
import com.pbs.middleware.server.features.filetransfer.download.events.DownloadFailed;
import com.pbs.middleware.server.features.filetransfer.download.events.FileDownloaded;
import com.pbs.middleware.server.features.filetransfer.download.exceptions.DownloadException;
import com.pbs.middleware.server.features.filetransfer.download.factory.DownloadFactory;
import com.pbs.middleware.server.features.filetransfer.download.listeners.DownloadPublisher;
import com.pbs.middleware.server.features.filetransfer.download.notification.DownloadEmailFactory;
import com.pbs.middleware.server.features.filetransfer.download.service.DownloadedFile;
import com.pbs.middleware.server.features.filetransfer.download.service.Downloader;
import com.pbs.middleware.server.features.filetransfer.download.status.repository.DownloadStatus;
import com.pbs.middleware.server.features.filetransfer.download.status.repository.FileDownloadStatus;
import com.pbs.middleware.server.features.filetransfer.download.status.service.DownloadStatusService;
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
public class DownloadsJob {

    @Value("${middleware.server.features.transfer.download.job.period:300}")
    private Long downloadPeriod;

    @NonNull
    private final DownloadFactory downloadFactory;

    @NonNull
    private final DownloadPublisher downloadPublisher;

    @NonNull
    private final DownloadStatusService service;

    @NonNull
    private final Downloader downloader;

    @NonNull
    private final DownloadEmailFactory emailFactory;

    @NonNull
    private final EmailService emailService;

    @PostConstruct
    private void init() {
        log.info("Download job configured with period {} seconds", downloadPeriod);
    }

    @Scheduled(fixedDelayString = "${middleware.server.features.transfer.download.job.period:300}000")
    public void checkDownloads() {
        reDownload();
        notifyAdmin();
        removeOldOne();
    }

    private void reDownload() {
        service.getAll().forEach(failedDownload ->
                downloadFactory.find(failedDownload.getId()).ifPresentOrElse(download -> {
                    failedDownload.getFiles().removeIf(fileDownload -> {
                        try {
                            log.info("Downloading[{}] file {}.", download.getId(), fileDownload.getFilename());
                            DownloadedFile file = downloader.downloadFile(download, fileDownload.getFilename());
                            log.info("File {} downloaded.", fileDownload.getFilename());
                            downloadPublisher.publish(new FileDownloaded(download.getId(), file.getName(), file.getPath(), file.getSize(), file.getHash(), file.getTempId()));
                            return true;
                        } catch (DownloadException e) {
                            log.error("Download[" + download.getId() + "] failed for file " + fileDownload.getFilename(), e);
                            fileDownload.setError(ExceptionUtils.getStackTrace(e));
                            return false;
                        }
                    });
                    if (failedDownload.getFiles().isEmpty()) {
                        service.delete(failedDownload.getId());
                    } else {
                        service.update(failedDownload);
                    }
                }, () -> removeUnlinked(failedDownload)));
    }

    private void notifyAdmin() {
        List<DownloadStatus> failed = service.getOldAndNotNotified();
        if (!failed.isEmpty()) {
            Map<UUID, Map<String, String>> exceptions = new HashMap<>();
            failed.forEach(failedDownload ->
                    exceptions.put(
                            failedDownload.getId(),
                            failedDownload.getFiles().stream().collect(Collectors.toMap(FileDownloadStatus::getFilename, FileDownloadStatus::getError))));

            List<Download> downloads = failed
                    .stream()
                    .map(DownloadStatus::getId)
                    .map(downloadFactory::find)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            emailFactory.get(downloads, exceptions).ifPresent(email -> {
                try {
                    emailService.send(email);
                    service.downloadNotified(failed.stream().map(DownloadStatus::getId).collect(Collectors.toList()));
                } catch (Exception e) {
                    log.error("Download failed notification sending failed.", e);
                }
            });

        }
    }

    private void removeOldOne() {
        service.getExpired().forEach(failedDownload -> downloadFactory.find(failedDownload.getId())
                .ifPresentOrElse(download -> {
                    downloadPublisher.publish(new DownloadFailed(download.getId()));
                    service.delete(failedDownload.getId());
                }, () -> removeUnlinked(failedDownload)));
    }

    private void removeUnlinked(DownloadStatus downloadStatus) {
        log.warn("Removing failed download without download process!");
        service.delete(downloadStatus.getId());
    }

}
