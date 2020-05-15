package com.pbs.middleware.server.features.ownership.service;

import com.pbs.middleware.server.common.security.SecurityUser;
import com.pbs.middleware.server.features.ownership.domain.DomainType;
import com.pbs.middleware.server.features.ownership.domain.Ownership;
import com.pbs.middleware.server.features.ownership.repository.OwnershipRepository;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pbs.middleware.server.features.users.repository.UserRole.ROLE_ADMIN;

@Service
@Transactional
@RequiredArgsConstructor
public class OwnershipService {

    private final OwnershipRepository repository;

    public void create(UUID domain, DomainType type) {
        repository.save(new Ownership(domain, getLoggedUserUuid(), type));
    }

    public void delete(UUID domainId) {
        repository.deleteByDomain(domainId);
    }

    public Set<UUID> getAll(DomainType domainType) {
        return isAdmin()
                ? repository.findAllByDomainType(domainType)
                : repository.findAllDByOwnerAndDomainType(getLoggedUserUuid(), domainType);
    }

    public boolean existsOwnerShip(UUID uuid) {
        return repository.existsByDomain(uuid);
    }

    public boolean isOwner(UUID uuid) {
        return repository.existsOwnershipByOwnerAndAndDomain(getLoggedUserUuid(), uuid);
    }

    public UUID getLoggedUserUuid() {
        return ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public boolean isAdmin() {
        return ((SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities().contains(ROLE_ADMIN);
    }

}
