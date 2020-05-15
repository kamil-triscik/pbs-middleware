package com.pbs.middleware.server.features.connection.metrics;

import com.pbs.middleware.server.features.connection.health.ConnectionHealthCheck;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.function.Supplier;
import lombok.NonNull;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class ConnectionHealthGauge {

    private final Gauge gauge;

    public ConnectionHealthGauge(@NonNull MeterRegistry registry, ConnectionHealthCheck connectionHealthCheck) {
        Supplier<Number> connectionHealth = () -> connectionHealthCheck.health().getStatus().equals(Status.UP) ? 1 : 0;
        this.gauge = Gauge
                .builder("connection_health", connectionHealth)
                .description("Connection health")
                .tags("feature", "connection")
                .register(registry);
    }
}
