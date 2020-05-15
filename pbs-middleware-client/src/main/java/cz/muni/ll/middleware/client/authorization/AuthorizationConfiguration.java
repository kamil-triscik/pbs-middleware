package cz.muni.ll.middleware.client.authorization;

import cz.muni.ll.middleware.client.authorization.components.AuthorizationInterceptor;
import cz.muni.ll.middleware.client.configuration.ClientConfiguration;
import cz.muni.ll.middleware.client.configuration.ClientProperties;
import cz.muni.ll.middleware.client.authorization.components.jwt.AuthorizationClient;
import cz.muni.ll.middleware.client.authorization.components.jwt.JwtAuthorizationInterceptor;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static cz.muni.ll.middleware.client.authorization.AuthenticationType.JWT;

@Configuration
@Import({ClientConfiguration.class})
public class AuthorizationConfiguration {

    @Value("${middleware.server.security.auth.type:BASIC}")
    private AuthenticationType authenticationType;

    public AuthorizationClient authorizationClient(ClientProperties clientProperties) {
        return Feign.builder()
                .decoder(new GsonDecoder())
                .target(AuthorizationClient.class, clientProperties.getUrl());
    }

    @Bean
    public AuthorizationInterceptor authInterceptor(ClientProperties clientProperties) {
        if (authenticationType == JWT) {
            return AuthorizationInterceptor.of(new JwtAuthorizationInterceptor(clientProperties.getCredentials(), authorizationClient(clientProperties)));
        } else {
            Credentials credentials = clientProperties.getCredentials();
            return AuthorizationInterceptor.of(new BasicAuthRequestInterceptor(credentials.getEmail(), credentials.getPassword()));
        }
    }

}
