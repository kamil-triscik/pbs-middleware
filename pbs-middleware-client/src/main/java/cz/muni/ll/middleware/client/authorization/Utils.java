package cz.muni.ll.middleware.client.authorization;

import cz.muni.ll.middleware.client.authorization.components.AuthorizationInterceptor;
import cz.muni.ll.middleware.client.authorization.components.jwt.AuthorizationClient;
import cz.muni.ll.middleware.client.authorization.components.jwt.JwtAuthorizationInterceptor;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientErrorDecoder;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import static cz.muni.ll.middleware.client.authorization.AuthenticationType.JWT;

public class Utils {

    public static AuthorizationInterceptor getAuthorizationInterceptor(Credentials credentials, AuthenticationType authenticationType, String url) {
        if (authenticationType == JWT) {
            return AuthorizationInterceptor.of(new JwtAuthorizationInterceptor(
                    credentials,
                    Feign.builder()
                            .errorDecoder(new LLMClientErrorDecoder())
                            .encoder(new GsonEncoder())
                            .decoder(new GsonDecoder())
                            .target(AuthorizationClient.class, url)));
        } else {
            return AuthorizationInterceptor.of(new BasicAuthRequestInterceptor(credentials.getEmail(), credentials.getPassword()));
        }
    }

}
