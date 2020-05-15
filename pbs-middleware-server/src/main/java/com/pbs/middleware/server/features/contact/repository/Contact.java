package com.pbs.middleware.server.features.contact.repository;

import com.pbs.middleware.server.common.repository.EntityClass;
import com.pbs.middleware.common.validations.ValidStringCollection;
import com.pbs.middleware.server.features.contact.validations.UniqueEmail;
import com.pbs.middleware.server.features.contact.validations.ValidContactTypes;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@UniqueEmail
@EqualsAndHashCode(callSuper = true)
@Table(
        name = "contact",
        schema = "public",
        indexes = @Index(name = "IDX_CONTACT", columnList = "id")
)
public class Contact extends EntityClass {

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "description", length = 100)
    private String description;

    @ValidStringCollection(fieldName = "contactTypes")
    @ValidContactTypes
    @ElementCollection
    @CollectionTable(name = "contact_type", joinColumns = @JoinColumn(name = "contact_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type")
    private Set<ContactType> contactTypes = new HashSet<>();

}
