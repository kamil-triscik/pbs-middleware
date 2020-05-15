package com.pbs.middleware.server.features.script.repository;

import com.pbs.middleware.server.common.repository.EntityClass;
import com.pbs.middleware.server.features.script.validation.UniqueName;
import com.pbs.middleware.server.features.script.validation.ValidGroovy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@UniqueName
@EqualsAndHashCode(callSuper = true)
@Table(
        name = "script",
        schema = "public",
        indexes = @Index(name = "IDX_SCRIPT_ID", columnList = "id")
)
public class Script extends EntityClass {

    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    @Column(name = "description", length = 100)
    private String description;

    @ValidGroovy
    @Column(name = "code", columnDefinition = "TEXT")
    private String code;

}
