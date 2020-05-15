package com.pbs.middleware.server.features.filetransfer.upload.status.repository;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static javax.persistence.FetchType.EAGER;

@Entity
@Getter
@Setter
@Table(
        name = "upload_status",
        schema = "public",
        indexes = @Index(name = "IDX_UPL_STAT", columnList = "id")
)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UploadStatus {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "start_date", nullable = false)
    private Long startDate;

    @Column(nullable = false)
    private Boolean notified;

    @OneToMany(mappedBy = "uploadStatus", cascade = CascadeType.ALL, fetch = EAGER)
    private Set<UploadFileStatus> files;

    public void addFile(UploadFileStatus f) {
        if (files == null) {
            files = new HashSet<>();
        }
        files.add(f);
    }

}
