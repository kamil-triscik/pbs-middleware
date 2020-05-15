package com.pbs.middleware.server.features.job.qstat;

import com.pbs.middleware.server.features.job.domain.State;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(
        name = "job_qstat",
        schema = "public",
        indexes =
        @Index(name = "IDX_QSTAT", columnList = "id")
)
@NoArgsConstructor
@AllArgsConstructor
public class JobQstat {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "qstat", nullable = false)
    private String qstat;

    @Column(name = "job_group")
    private String group;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private State status;
}
