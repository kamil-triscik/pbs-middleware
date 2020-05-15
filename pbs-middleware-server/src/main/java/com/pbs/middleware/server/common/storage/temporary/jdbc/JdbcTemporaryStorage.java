package com.pbs.middleware.server.common.storage.temporary.jdbc;

import com.pbs.middleware.server.common.storage.temporary.TemporaryEntity;
import com.pbs.middleware.server.common.storage.temporary.TemporaryFileNotFoundException;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorage;
import com.pbs.middleware.server.common.storage.temporary.TemporaryStorageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;

@Slf4j
@Repository
@Transactional
@JdbcTempStorageEnabled
public class JdbcTemporaryStorage implements TemporaryStorage {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public TemporaryEntity save(UUID domainId, String id, byte[] data) throws TemporaryStorageException {
        try {
            JdbcTemporaryEntity entity = new JdbcTemporaryEntity(id, domainId, data.length, DigestUtils.md5DigestAsHex(data), data);
            manager.persist(entity);
            return entity;
        } catch (Exception e) {
            throw new TemporaryStorageException("Temporary file saving failed", e);
        }
    }

    @Override
    public TemporaryEntity get(UUID domainId, String id) throws TemporaryStorageException, TemporaryFileNotFoundException {
        try {
            return manager.createQuery(
                    "SELECT te FROM JdbcTemporaryEntity te WHERE te.domainId = : did AND te.id = :id", TemporaryEntity.class)
                    .setParameter("did", domainId)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new TemporaryFileNotFoundException(domainId, id);
        } catch (Exception e) {
            throw new TemporaryStorageException("Temporary file fetching failed", e);
        }
    }

    @Override
    public Optional<TemporaryEntity> findById(UUID domainId, String id) throws TemporaryStorageException {
        try {
            return Optional.ofNullable((JdbcTemporaryEntity) manager.createQuery(
                    "SELECT te FROM JdbcTemporaryEntity te WHERE te.domainId = : did AND te.id = :id")
                    .setParameter("did", domainId)
                    .setParameter("id", id)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new TemporaryStorageException("Temporary file fetching failed", e);
        }
    }

    @Override
    public List<TemporaryEntity> findByDomainId(UUID domainId) throws TemporaryStorageException, TemporaryFileNotFoundException {
        return manager.createQuery(
                "SELECT te FROM JdbcTemporaryEntity te WHERE te.domainId = : did", TemporaryEntity.class)
                .setParameter("did", domainId)
                .getResultList();
    }

    @Override
    public void deleteAll(UUID domainId) throws TemporaryStorageException {
        try {
            manager.createQuery(
                    "DELETE FROM JdbcTemporaryEntity te WHERE te.domainId = :did")
                    .setParameter("did", domainId).executeUpdate();
        } catch (Exception e){
            throw new TemporaryStorageException("Deleting data for domain id [" + domainId +"] failed", e);
        }
    }

    @Override
    public Boolean deleteById(UUID domainId, String id) throws TemporaryStorageException {
        try {
            findById(domainId, id).ifPresent(manager::remove);
            return findById(domainId, id).isEmpty();
        } catch (Exception e) {
            throw new TemporaryStorageException("Temporary file removing failed", e);
        }
    }

    @Override
    public Long getCount() {
        try {
            return ((Number) manager.createQuery("SELECT COALESCE(COUNT(te),0) FROM JdbcTemporaryEntity te").getSingleResult()).longValue();
        } catch (Exception e) {
            log.error("Fetching count files in temporary storage failed", e);
        }
        return -1L;
    }

    @Override
    public Long getTotalSize() {
        try {
            return ((Number) manager.createQuery("SELECT COALESCE(sum(te.size),0) FROM JdbcTemporaryEntity te").getSingleResult()).longValue();
        } catch (Exception e) {
            log.error("Fetching temporary storage size failed", e);
        }
        return -1L;
    }

}
