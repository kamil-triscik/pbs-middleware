package com.pbs.middleware.server.features.connection.service;

import com.pbs.middleware.server.features.connection.repository.ConnectionRepository;
import com.pbs.middleware.server.features.connection.repository.Connection;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class ConnectionService {

    @NonNull
    private final ConnectionRepository connectionRepository;

    public List<Connection> getAll() {
        return connectionRepository.findAll();
    }

    public Connection get(@NonNull UUID uuid) {
        return connectionRepository.findById(uuid).orElseThrow(() -> ConnectionNotFoundException.of(uuid));
    }

    public Connection get(@NonNull String uuidOrName) {
        if (uuidOrName.isEmpty() || uuidOrName.isBlank()) {
            throw new IllegalArgumentException("uuidOrName can not be null");
        }
        try {
            return connectionRepository
                    .findByNameOrUuid(UUID.fromString(uuidOrName), uuidOrName)
                    .orElseThrow(() -> ConnectionNotFoundException.of(uuidOrName));
        } catch (IllegalArgumentException e) {
            return connectionRepository
                    .findByName(uuidOrName)
                    .orElseThrow(() -> ConnectionNotFoundException.of(uuidOrName));
        }

    }

    public Connection create(@Valid @NonNull Connection connection) {
        return connectionRepository.save(connection);
    }

    public Connection update(@NonNull UUID id, @Valid @NonNull Connection connection) {
        get(id);
        return connectionRepository.save(connection);
    }

    public void delete(@NonNull UUID domainId) {
        connectionRepository.deleteById(domainId);
    }

}
