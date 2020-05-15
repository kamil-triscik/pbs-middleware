package com.pbs.middleware.server.common;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ServerConfiguration {

    @Value("${middleware.server.application.name}")
    private String applicationName;

    private final DiskSpaceHealthIndicator diskSpaceHealthIndicator;

    private final HealthEndpoint healthEndpoint;

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    @Bean
    Gauge applicationHealth(@NonNull MeterRegistry registry) {
        Supplier<Number> connectionHealth = () -> healthEndpoint.health().getStatus().equals(Status.UP) ? 1 : 0;
        return Gauge
                .builder("application_health", connectionHealth)
                .description("Application health")
                .tags("feature", "application")
                .register(registry);
    }

    @Bean
    Gauge diskUsageHealth(@NonNull MeterRegistry registry) {
        Supplier<Number> connectionHealth = () -> diskSpaceHealthIndicator.health().getStatus().equals(Status.UP) ? 1 : 0;
        return Gauge
                .builder("disk_health", connectionHealth)
                .description("Disk health")
                .tags("feature", "disk")
                .register(registry);
    }

}
