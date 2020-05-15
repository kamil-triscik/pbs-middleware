package com.pbs.middleware.server.features.job;

import java.util.concurrent.ForkJoinPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobsConfiguration {

    public static final String JOB_MONITORING_DELAY = "${middleware.server.features.job.monitoring.delay:600}000";


    @Value("${middleware.features.job.monitoring.threads:3}")
    private Integer usedThreads;

    @Bean
    public ForkJoinPool getForkJoinPool() {
        return new ForkJoinPool(usedThreads);
    }

}
