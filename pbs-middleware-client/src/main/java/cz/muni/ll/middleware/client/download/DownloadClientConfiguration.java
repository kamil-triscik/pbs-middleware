package cz.muni.ll.middleware.client.download;

import cz.muni.ll.middleware.client.authorization.AuthorizationConfiguration;
import cz.muni.ll.middleware.client.authorization.components.AuthorizationInterceptor;
import cz.muni.ll.middleware.client.configuration.ClientProperties;
import cz.muni.ll.middleware.client.download.client.DownloadDecoder;
import cz.muni.ll.middleware.client.download.client.DownloadClient;
import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({AuthorizationConfiguration.class})
@Configuration
public class DownloadClientConfiguration {

    @Bean
    public DownloadClient downloadsClient(
            AuthorizationInterceptor authInterceptor,
            ClientProperties clientProperties) {
        return Feign.builder()
                .decoder(new DownloadDecoder())
                .logLevel(clientProperties.getLogLevel())
                .options(clientProperties.getOptions())
                .requestInterceptor(authInterceptor)
                .target(DownloadClient.class, clientProperties.getUrl());

    }

}
