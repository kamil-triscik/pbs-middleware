package com.pbs.middleware.it.config.containers;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.lifecycle.Startables;

import static com.pbs.middleware.it.config.containers.ContainerType.MONGO;
import static com.pbs.middleware.it.config.containers.ContainerType.PSQL;

public class MongoContainer {

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private static final String DB_NAME = "llm-middleware";
        private static final String PASSWORD = "llm_admin_pass";
        private static final String USERNAME = "llm_admin";

        private static final String USERNAME_PROP = "MONGO_INITDB_ROOT_USERNAME";
        private static final String PASSWORD_PROP = "MONGO_INITDB_ROOT_PASSWORD";

        private static final Integer PORT = 27017;

        static GenericContainer<?> mongodb = buildContainer();

        static GenericContainer<?> buildContainer() {
            GenericContainer<?> mongodb = new GenericContainer<>("mongo:4.2")
                    .withExposedPorts(PORT)
                    .withEnv(USERNAME_PROP, USERNAME)
                    .withEnv(PASSWORD_PROP, PASSWORD);

            mongodb.setPortBindings(List.of(PORT + ":" + PORT));
            return mongodb;
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Startables.deepStart(Stream.of(mongodb)).join();

            MapPropertySource mongoProps = new MapPropertySource(MONGO.getResourceName(), getMongoProps());

            applicationContext
                    .getEnvironment()
                    .getPropertySources()
                    .addAfter(PSQL.getResourceName(), mongoProps);
        }

        private static Map<String, Object> getMongoProps() {
            return Map.of(
                    "middleware.server.db.mongo.name", DB_NAME,
                    "middleware.server.db.mongo.host", mongodb.getContainerIpAddress(),
                    "middleware.server.db.mongo.port", PORT,
                    "middleware.server.db.mongo.user", USERNAME,
                    "middleware.server.db.mongo.password", PASSWORD,
                    "middleware.server.db.type", "mongodb"
            );
        }
    }
}