package com.pbs.middleware.server.common.storage.temporary.local;

import com.pbs.middleware.server.common.storage.temporary.TemporaryEntity;
import com.pbs.middleware.server.common.storage.temporary.TemporaryFileNotFoundException;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorageException;
import com.pbs.middleware.server.features.template.exceptions.TemplateAlreadyExistsException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

/**
 * {@code middleware.server.features.storage.temporary.type}
 *
 * <h2>Properties</h2>
 * <ul>
 *   <li><b>{@code middleware.server.features.storage.temporary.path}</b> - path to directory, where will be store </li>
 * </ul>
 *
 * @author Kamil Triscik
 * @since 1.0
 */
@Slf4j
@Component
@LocalTempStorageEnabled
public class LocalTemporaryStorage implements TemporaryStorage {

    /**
     * Path to temporary storage.
     */
    @Value("${middleware.server.features.storage.temporary.path}")
    private String storagePath;

    @PostConstruct
    private void postConstruct() throws TemporaryStorageException {
        if (storagePath == null || storagePath.isBlank()) {
            throw new TemporaryStorageException("Temporary storage location not configured!");
        }
        Path tempStorage = Path.of(storagePath);
        if (Files.notExists(tempStorage)) {
            throw new TemporaryStorageException("Temporary storage location \"" + storagePath + "\" does not exists!");
        }
        if (!Files.isDirectory(tempStorage)) {
            throw new TemporaryStorageException("Temporary storage location \"" + storagePath + "\" is not directory!");
        }
    }

    @Override
    public synchronized TemporaryEntity save(UUID domainId, String id, byte[] data) throws TemporaryStorageException {
        final Path directoryPath = Paths.get(storagePath, domainId.toString());
        if (Files.notExists(directoryPath)) {
            try {
                Files.createDirectory(directoryPath);
            } catch (IOException e) {
                log.error("Creating new directory for " + domainId + " failed", e);
                throw new TemporaryStorageException("Creating new directory failed", e);
            }
        }

        Path newFilePath = Paths.get(storagePath, domainId.toString(), id);

        if (Files.exists(newFilePath)) {
            throw new TemplateAlreadyExistsException(format("Temporary file %s already exists!", newFilePath.toAbsolutePath().toString()));
        }

        try {
            Files.write(newFilePath, data);
        } catch (IOException e) {
            log.error("Saving temporary file failed", e);
            throw new TemporaryStorageException("Saving temporary file failed", e);
        }

        if (Files.notExists(newFilePath)) {
            throw new TemporaryStorageException(format("New temporary file check. File [%s] does not exists!", newFilePath.toString()));
        }
        try {
            if (Files.size(newFilePath) != data.length) {
                throw new TemporaryStorageException(format("New temporary file check. File [%s] does not exists!", newFilePath.toString()));
            }
        } catch (IOException e) {
            throw new TemporaryStorageException("New temporary file check failed", e);
        }

        return new LocalTemporaryEntity(id, data);
    }

    @Override
    public TemporaryEntity get(UUID domainId, String id) throws TemporaryStorageException, TemporaryFileNotFoundException {
        Path filePath = Path.of(storagePath, domainId.toString(), id);

        if (Files.notExists(filePath)) {
            throw new TemporaryFileNotFoundException(domainId, filePath.toString());
        }
        try {
            return new LocalTemporaryEntity(id, Files.readAllBytes(filePath));
        } catch (IOException e) {
            log.error("Loading temporary file failed", e);
            throw new TemporaryStorageException("Loading temporary file failed", e);
        }
    }

    @Override
    public Optional<TemporaryEntity> findById(UUID domainId, String id) throws TemporaryStorageException {
        Path filePath = Path.of(storagePath, domainId.toString(), id);

        if (Files.notExists(filePath)) {
            return Optional.empty();
        }
        try {
            return Optional.of(new LocalTemporaryEntity(id, Files.readAllBytes(filePath)));
        } catch (IOException e) {
            log.error("Loading temporary file failed", e);
            throw new TemporaryStorageException("Loading temporary file failed", e);
        }
    }

    @Override
    public List<TemporaryEntity> findByDomainId(UUID domainId) throws TemporaryStorageException, TemporaryFileNotFoundException {
        final Path directoryPath = Paths.get(storagePath, domainId.toString());
        if (Files.notExists(directoryPath)) {
            return emptyList();
        }

        List<TemporaryEntity> entities = new ArrayList<>();
        for (File file : directoryPath.toFile().listFiles()) {
            entities.add(get(domainId, file.getName()));
        }
        return entities;
    }


    @Override
    public void deleteAll(UUID domainId) throws TemporaryStorageException {
        final Path directoryPath = Paths.get(storagePath, domainId.toString());
        try {
            FileSystemUtils.deleteRecursively(directoryPath);
        } catch (IOException e) {
            log.error("Deleting directory [" + directoryPath.toString() + "] failed", e);
            throw new TemporaryStorageException("Deleting directory [" + directoryPath.toString() + "] failed", e);
        }
    }

    @Override
    public Boolean deleteById(UUID domainId, String id) throws TemporaryStorageException {
        Path fileToDelete = Path.of(storagePath, domainId.toString(), id);

        try {
            Files.deleteIfExists(fileToDelete);
        } catch (IOException e) {
            log.error("Dropping temporary file failed", e);
            throw new TemporaryStorageException("Dropping temporary file failed", e);
        }
        return Files.exists(fileToDelete);
    }

    @Override
    public Long getCount() {
        try {
            return Files.list(Paths.get(storagePath)).count();
        } catch (IOException e) {
            log.error("Fetching count files in temporary storage failed", e);
        }
        return -1L;
    }

    @Override
    public Long getTotalSize() {
        try {
            return Files.walk(Paths.get(storagePath)).mapToLong( p -> p.toFile().length() ).sum();
        } catch (IOException e) {
            log.error("Fetching temporary storage size failed", e);
        }
        return -1L;
    }

}
