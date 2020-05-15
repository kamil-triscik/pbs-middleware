package com.pbs.middleware.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
public class MiddlewareServer {

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        System.setProperty("spring.security.strategy", "MODE_INHERITABLETHREADLOCAL");
        try {
            SpringApplication.run(MiddlewareServer.class, args);
        } catch (Throwable e) {
            log.error("Application startup failed", e);
            System.exit(1);
        }
    }
}
