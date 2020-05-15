package com.pbs.middleware.server.features.job.events;

import com.pbs.middleware.server.features.pbs.qsub.QsubCommand;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event used when
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Job")
public class JobLaunchedEvent extends JobEvent {

    private QsubCommand qsubCommand;

    private String jobId;

    private String group;

    private String connection;

    private UUID owner;

    public JobLaunchedEvent(UUID domainId, QsubCommand qsubCommand, String jobId, String group, String connection, UUID owner) {
        super(domainId);
        this.qsubCommand = qsubCommand;
        this.jobId = jobId;
        this.group = group;
        this.connection = connection;
        this.owner = owner;
    }
}
