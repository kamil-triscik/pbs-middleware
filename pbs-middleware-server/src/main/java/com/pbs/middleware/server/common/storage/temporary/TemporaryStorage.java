package com.pbs.middleware.server.common.storage.temporary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TemporaryStorage {

    TemporaryEntity save(UUID domainId, String id, byte[] data) throws TemporaryStorageException;

    TemporaryEntity get(UUID domainId, String id) throws TemporaryStorageException, TemporaryFileNotFoundException;

    List<TemporaryEntity> findByDomainId(UUID domainId) throws TemporaryStorageException, TemporaryFileNotFoundException;

    Optional<TemporaryEntity> findById(UUID domainId, String id) throws TemporaryStorageException;

    Boolean deleteById(UUID domainId, String id) throws TemporaryStorageException;

    void deleteAll(UUID domainId) throws TemporaryStorageException;

    default Boolean delete(UUID domainId, TemporaryEntity entity) throws TemporaryStorageException {
        return deleteById(domainId, entity.getId());
    }

    Long getCount();

    Long getTotalSize();

}
