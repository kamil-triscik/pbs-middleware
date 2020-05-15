package com.pbs.middleware.server.features.connection.repository;

import com.pbs.middleware.server.features.connection.ConnectionConfig;
import com.pbs.middleware.server.features.connection.validations.UniqueName;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import static com.pbs.middleware.server.features.connection.ConnectionConfig.DISCRIMINATOR_COLUMN;
import static com.pbs.middleware.server.features.connection.ConnectionConfig.GENERAL_CONNECTION;
import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@UniqueName
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Table(
        name = ConnectionConfig.TABLE_NAME,
        schema = "public",
        indexes = {
                @Index(name = "IDX_CONN_ID", columnList = "id"),
                @Index(name = "IDX_CONN_NAME", columnList = "name"),
        }
)
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorColumn(name = DISCRIMINATOR_COLUMN, discriminatorType = STRING)
@DiscriminatorValue(GENERAL_CONNECTION)
public class Connection {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @NotBlank(message = "Connection name cannot be empty.")
    @Size(max = 20, message = "Connection name cannot be longer than 20 characters.")
    @Column(name = "name", nullable = false, unique = true, length = 20)
    private String name;

    @Size(max = 100, message = "Connection description cannot be longer than 100 characters.")
    @Column(name = "description", length = 100)
    private String description;

    @NotBlank(message = "Pbs host cannot be empty.")
    @Size(max = 100, message = "Pbs host cannot be longer than 100 characters.")
    @Column(name = "pbs_host", nullable = false, length = 100)
    private String pbsHost;
}
