package com.pbs.middleware.it.config.containers;

import java.util.Map;
import java.util.stream.Stream;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import static com.pbs.middleware.it.config.containers.ContainerType.PSQL;

public class PsqlContainer {

    private static final String PASSWORD = "llm";
    private static final String USERNAME = "llm";

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private static final PostgreSQLContainer<?> postgres = getContainer();

        static PostgreSQLContainer<?> getContainer() {
            PostgreSQLContainer<?> psql = new PostgreSQLContainer<>();
            psql.withPassword(PASSWORD);
            psql.withUsername(USERNAME);
            return psql;
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Startables.deepStart(Stream.of(postgres)).join();

            MapPropertySource psqlResources = new MapPropertySource(PSQL.getResourceName(), getPsqlProps());

            applicationContext
                    .getEnvironment()
                    .getPropertySources()
                    .addFirst(psqlResources);
        }

        private static Map<String, Object> getPsqlProps() {
            return Map.of(
                    "middleware.server.db.psql.driver-class-name", postgres.getDriverClassName(),
//                    "spring.datasource.driver-class-name", postgres.getDriverClassName(),
                    "middleware.server.db.psql.username", postgres.getUsername(),
                    "middleware.server.db.psql.password", postgres.getPassword(),
                    "middleware.server.db.psql.url", postgres.getJdbcUrl()
            );
        }
    }
}