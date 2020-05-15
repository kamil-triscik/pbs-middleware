package com.pbs.middleware.server.features.ownership.domain;

import com.pbs.middleware.server.features.filetransfer.download.events.DownloadEvent;
import com.pbs.middleware.server.features.filetransfer.upload.events.UploadEvent;
import com.pbs.middleware.server.features.job.events.JobEvent;
import lombok.Getter;

public enum DomainType {
    JOB(JobEvent.class),
    DOWNLOAD(DownloadEvent.class),
    UPLOAD(UploadEvent.class);

    @Getter
    private Class clazz;

    DomainType(Class eventClass) {
        this.clazz = eventClass;
    }
}
