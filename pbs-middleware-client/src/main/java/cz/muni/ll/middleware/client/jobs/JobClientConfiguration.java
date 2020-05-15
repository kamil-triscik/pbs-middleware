package cz.muni.ll.middleware.client.jobs;

import cz.muni.ll.middleware.client.authorization.AuthorizationConfiguration;
import cz.muni.ll.middleware.client.authorization.components.AuthorizationInterceptor;
import cz.muni.ll.middleware.client.configuration.ClientProperties;
import cz.muni.ll.middleware.client.jobs.client.JobClient;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({AuthorizationConfiguration.class})
@Configuration
public class JobClientConfiguration {

    @Bean
    public JobClient jobsClient(
            AuthorizationInterceptor authInterceptor,
            ClientProperties clientProperties) {
        return Feign.builder()
                .decoder(new GsonDecoder())
                .logLevel(clientProperties.getLogLevel())
                .options(clientProperties.getOptions())
                .requestInterceptor(authInterceptor)
                .target(JobClient.class, clientProperties.getUrl());

    }

}
