package com.pbs.middleware.server.features.filetransfer.download.status.repository;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Table(
        name = "file_download_status",
        schema = "public",
        indexes = @Index(name = "IDX_FILE_DOWN_STAT", columnList = "id, download_status_id")
)
@IdClass(FileDownloadStatusId.class)
@AllArgsConstructor()
@NoArgsConstructor
public class FileDownloadStatus {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Id
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "download_status_id", nullable = false)
    private DownloadStatus downloadStatus;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "error")
    private String error;

}
