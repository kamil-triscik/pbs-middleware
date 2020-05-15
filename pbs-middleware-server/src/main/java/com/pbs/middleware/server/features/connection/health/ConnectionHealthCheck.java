package com.pbs.middleware.server.features.connection.health;

import com.pbs.middleware.server.features.connection.repository.ConnectionRepository;
import com.pbs.middleware.server.features.ssh.shell.Result;
import com.pbs.middleware.server.features.ssh.shell.ShellException;
import com.pbs.middleware.server.features.ssh.shell.ShellFactory;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.springframework.boot.actuate.health.Status.DOWN;
import static org.springframework.boot.actuate.health.Status.UP;

@Component(value = "connections")
@RequiredArgsConstructor
public class ConnectionHealthCheck implements HealthIndicator {

    @Value("${middleware.features.connection.health.command:pwd}")
    private String shellCommand;

    private final ConnectionRepository connectionRepository;

    private final ShellFactory shellFactory;

    private Health health = Health.up().build();

    @Override
    public Health getHealth(boolean includeDetails) {
        return this.health();
    }

    @Override
    public Health health() {
        return this.health;
    }

    @Scheduled(fixedDelayString = "${middleware.features.connection.health.interval:300}000")
    public void testConnections() {
        AtomicReference<Status> status = new AtomicReference<>(UP);
        Map<String, Object> details = connectionRepository.findAll().parallelStream().map(connection -> {
            try {
                Result result = shellFactory.get(connection).executeCommand(shellCommand);
                if (result.getStderr() != null && !result.getStderr().isEmpty()) {
                    return Map.entry(connection.getName(), Map.of("Status", DOWN.getCode(), "Message", result.getStderr()));
                }
                return Map.entry(connection.getName(), Map.of("Status", UP.getCode()));
            } catch (ShellException e) {
                status.set(DOWN);
                return Map.entry(connection.getName(), Map.of("Status", DOWN.getCode(), "Message", e.getLocalizedMessage()));
            }
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.health = Health.status(status.get()).withDetails(Map.of("Check shell command", shellCommand, "connectionsStates", details)).build();
    }
}
