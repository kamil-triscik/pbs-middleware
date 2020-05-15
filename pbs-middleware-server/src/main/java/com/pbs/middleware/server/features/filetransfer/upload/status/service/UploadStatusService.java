package com.pbs.middleware.server.features.filetransfer.upload.status.service;

import com.google.common.collect.ImmutableList;
import com.pbs.middleware.server.features.filetransfer.upload.status.repository.UploadFileStatus;
import com.pbs.middleware.server.features.filetransfer.upload.status.repository.UploadStatus;
import com.pbs.middleware.server.features.filetransfer.upload.status.repository.UploadStatusRepository;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadStatusService {

    @NonNull
    private final UploadStatusRepository uploadStatusRepository;

    @Value("#{T(java.time.Duration).parse('${middleware.server.features.transfer.upload.limit.notification}')}")
    private Duration delay;

    @Value("#{T(java.time.Duration).parse('${middleware.server.features.transfer.upload.retention}')}")
    private Duration retention;

    public void create(UploadFileStatus status) {
        UploadStatus toSave = uploadStatusRepository
                .findById(status.getUploadStatus().getId())
                .map(it -> {
                    status.setUploadStatus(it);
                    it.getFiles().add(status);
                    uploadStatusRepository.save(it);
                    return it;
                }).orElseGet(status::getUploadStatus);
        uploadStatusRepository.save(toSave);
    }

    public List<UploadStatus> getAll() {
        return ImmutableList.copyOf(uploadStatusRepository.findAll());
    }

    public void uploadNotified(List<UUID> notifiedIds) {
        if (!notifiedIds.isEmpty()) {
            uploadStatusRepository.setNotified(notifiedIds);
        }
    }

    public List<UploadStatus> getOldAndNotNotified() {
        return uploadStatusRepository.findAllNotNotified(Math.toIntExact(delay.getSeconds()));
    }

    public List<UploadStatus> getAllAfterTimeout() {
        return uploadStatusRepository.findAllOld(Math.toIntExact(retention.getSeconds()));
    }

    public void update(UploadStatus id) {
        uploadStatusRepository.save(id);
    }

    public void delete(UUID id) {
        if (uploadStatusRepository.existsById(id)) {
            uploadStatusRepository.deleteById(id);
        }
    }

}
