package com.pbs.middleware.server.features.filetransfer.download.status.service;

import com.google.common.collect.ImmutableList;
import com.pbs.middleware.server.features.filetransfer.download.status.repository.DownloadStatus;
import com.pbs.middleware.server.features.filetransfer.download.status.repository.DownloadStatusRepository;
import com.pbs.middleware.server.features.filetransfer.download.status.repository.FileDownloadStatus;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DownloadStatusService {

    private final DownloadStatusRepository downloadStatusRepository;

    @Value("#{T(java.time.Duration).parse('${middleware.server.features.transfer.download.limit.notification}')}")
    private Duration delay;

    @Value("#{T(java.time.Duration).parse('${middleware.server.features.transfer.download.retention}')}")
    private Duration retention;

    public void create(FileDownloadStatus status) {
        downloadStatusRepository
                .findById(status.getDownloadStatus().getId())
                .ifPresentOrElse(
                        it -> {
                            status.setDownloadStatus(it);
                            it.getFiles().add(status);
                            downloadStatusRepository.save(it);
                        },
                        () -> downloadStatusRepository.save(status.getDownloadStatus())
                );
    }

    public List<DownloadStatus> getAll() {
        return ImmutableList.copyOf(downloadStatusRepository.findAll());
    }

    public void downloadNotified(List<UUID> notifiedIds) {
        if (!notifiedIds.isEmpty()) {
            downloadStatusRepository.setNotified(notifiedIds);
        }
    }

    public List<DownloadStatus> getOldAndNotNotified() {
        return downloadStatusRepository.findAllNotNotified(Math.toIntExact(delay.getSeconds()));
    }

    public List<DownloadStatus> getExpired() {
        return downloadStatusRepository.findAllOld(Math.toIntExact(retention.getSeconds()));
    }

    public void update(DownloadStatus id) {
        downloadStatusRepository.save(id);
    }

    public void delete(UUID id) {
        if (downloadStatusRepository.existsById(id)) {
            downloadStatusRepository.deleteById(id);
        }
    }

}
