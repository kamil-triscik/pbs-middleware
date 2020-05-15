package com.pbs.middleware.it.config.containers;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.SocketUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.lifecycle.Startables;

import static com.pbs.middleware.it.config.containers.ContainerType.MONGO;
import static com.pbs.middleware.it.config.containers.ContainerType.SERVER;


public class RemoteServerContainer {

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private static final String DOCKER_IMAGE = "rastasheep/ubuntu-sshd";

        private static final Integer SERVER_PORT = SocketUtils.findAvailableTcpPort();

        static GenericContainer<?> remote_server = getContainer();

        static GenericContainer<?> getContainer() {
            GenericContainer<?> mongodb = new GenericContainer<>(DOCKER_IMAGE)
                    .withExposedPorts(22);

            mongodb.setPortBindings(List.of(SERVER_PORT + ":22"));

            return mongodb;
        }

        private static Map<String, Object> getRemoteServerProps() {
            return Map.of(
                    "remote.host", remote_server.getHost(),
                    "remote.port", SERVER_PORT
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Startables.deepStart(Stream.of(remote_server)).join();

            MapPropertySource remoteServerProps = new MapPropertySource(
                    SERVER.getResourceName(),
                    getRemoteServerProps()
            );

            applicationContext.getBeanFactory().registerSingleton("ubuntuContainer", remote_server);

            applicationContext
                    .getEnvironment()
                    .getPropertySources()
                    .addAfter(MONGO.getResourceName(), remoteServerProps);
        }
    }
}