package com.pbs.middleware.server.features.filetransfer.upload.status.repository;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@Table(
        name = "file_upload_status",
        schema = "public",
        indexes = @Index(name = "IDX_FILE_UPL_STAT", columnList = "id, upload_status_id")
)
@IdClass(UploadFileStatusId.class)
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileStatus {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Id
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "upload_status_id", nullable = false)
    private UploadStatus uploadStatus;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "error")
    private String error;

}
