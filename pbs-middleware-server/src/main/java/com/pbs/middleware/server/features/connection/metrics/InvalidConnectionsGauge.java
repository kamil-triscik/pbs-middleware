package com.pbs.middleware.server.features.connection.metrics;

import com.pbs.middleware.server.features.connection.health.ConnectionHealthCheck;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import lombok.NonNull;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import static org.springframework.boot.actuate.health.Status.UP;

@Component
public class InvalidConnectionsGauge {

    private final Gauge gauge;

    public InvalidConnectionsGauge(@NonNull MeterRegistry registry, ConnectionHealthCheck connectionHealthCheck) {

        this.gauge = Gauge
                .builder("connection_invalid_count", getSupplier(connectionHealthCheck))
                .description("Connection health")
                .tags("feature", "connection")
                .register(registry);
    }

    private Supplier<Number> getSupplier(ConnectionHealthCheck connectionHealthCheck) {
        return () -> {
            if (connectionHealthCheck.health().getStatus().equals(UP)) {
                return 0;
            } else {
                Map<String, Object> connectionsStates = (Map<String, Object>) connectionHealthCheck.getHealth(true)
                        .getDetails().getOrDefault("connectionsStates", new HashMap<>());

                return connectionsStates.values().stream()
                        .map(it -> (Map<String, Object>) it)
                        .map(it -> it.get("Status"))
                        .filter(it -> !it.equals("UP"))
                        .count();
            }
        };
    }
}
