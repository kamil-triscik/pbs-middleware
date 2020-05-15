package com.pbs.middleware.server.features.filetransfer.download.status.repository;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@Table(
        name = "download_status",
        schema = "public",
        indexes = @Index(name = "IDX_DOWN_STAT", columnList = "id")
)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class DownloadStatus {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "start_date", nullable = false)
    private Long startDate;

    @Column(nullable = false)
    private Boolean notified;

    @OneToMany(mappedBy = "downloadStatus", cascade = CascadeType.ALL, fetch = EAGER)
    private Set<FileDownloadStatus> files;

    public void addFile(FileDownloadStatus f) {
        if (files == null) {
            files = new HashSet<>();
        }
        files.add(f);
    }

}
