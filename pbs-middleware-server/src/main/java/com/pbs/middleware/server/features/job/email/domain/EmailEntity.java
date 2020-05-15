package com.pbs.middleware.server.features.job.email.domain;

import com.pbs.middleware.server.common.repository.EntityClass;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(
        name = "email",
        schema = "public"
)
public class EmailEntity extends EntityClass {

    @Column(name = "recipient", nullable = false, length = 50)
    private String recipient;

    @Column(name = "subject", nullable = false, length = 50)
    private String subject;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailState state;

    @Column(name = "create_date", nullable = false)
    private Long createDate;

    @Column(name = "send_date")
    private Long sendDate;
}
