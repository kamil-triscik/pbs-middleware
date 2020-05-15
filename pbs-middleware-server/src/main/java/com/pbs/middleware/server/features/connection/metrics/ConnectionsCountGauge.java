package com.pbs.middleware.server.features.connection.metrics;

import com.pbs.middleware.server.common.metrics.CachedSupplier;
import com.pbs.middleware.server.features.connection.repository.ConnectionRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ConnectionsCountGauge {

    private final Gauge gauge;

    public ConnectionsCountGauge(@NonNull MeterRegistry registry, ConnectionRepository connectionRepository) {
        CachedSupplier<Number> connectionsCount = new CachedSupplier<>(connectionRepository::count);
        this.gauge = Gauge
                .builder("connections_count", connectionsCount)
                .description("Connection count")
                .tags("feature", "connection")
                .register(registry);
    }
}
