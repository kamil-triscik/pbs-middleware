package cz.muni.ll.middleware.client.configuration;

import cz.muni.ll.middleware.client.authorization.Credentials;
import feign.Logger;
import feign.Request;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class ClientConfiguration {

    @Value("${middleware.login}")
    private String login;

    @Value("${middleware.password}")
    private String password;

    @Value("${middleware.url}")
    private String url;

    @Value("#{feign.Logger.Level.valueOf(${middleware.loglevel:NONE})")
    private Logger.Level logLevel;

    @Value("${middleware.timeout.connection.value:30}")
    private Long connectionTimeout ;

    @Value("#{java.util.concurrent.TimeUnit.valueOf(${middleware.timeout.connection.unit:NONE})}")
    private TimeUnit connectionTimeoutUnit = SECONDS;

    @Value("${middleware.timeout.read.value:30}")
    private Long readTimeout;

    @Value("#{java.util.concurrent.TimeUnit.valueOf(${middleware.timeout.read.unit:NONE})}")
    private TimeUnit readTimeoutUnit = SECONDS;

    @Value("${middleware.redirects:false}")
    private boolean followRedirects;

    @Bean
    public ClientProperties clientProperties() {
        return new ClientProperties(
                new Credentials(login, password),
                url,
                logLevel,
                new Request.Options(
                        connectionTimeout,
                        connectionTimeoutUnit,
                        readTimeout,
                        readTimeoutUnit,
                        followRedirects));
    }




}
