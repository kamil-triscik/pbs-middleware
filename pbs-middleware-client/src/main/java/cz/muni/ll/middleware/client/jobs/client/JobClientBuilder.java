package cz.muni.ll.middleware.client.jobs.client;

import cz.muni.ll.middleware.client.authorization.AuthenticationType;
import cz.muni.ll.middleware.client.authorization.Credentials;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientErrorDecoder;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientRetryer;

import com.google.gson.GsonBuilder;
import feign.Feign;
import feign.Logger.Level;
import feign.Request.Options;
import feign.RequestInterceptor;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cz.muni.ll.middleware.client.authorization.Utils.getAuthorizationInterceptor;
import static feign.Logger.Level.NONE;

public class JobClientBuilder {

    private final Credentials credentials;
    private final String url;
    private final List<RequestInterceptor> requestInterceptors = new LinkedList<>();
    private AuthenticationType authenticationType = AuthenticationType.BASIC;
    private Level level = NONE;
    private Options options = null;

    public JobClientBuilder(String url, Credentials credentials) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid url provided: " + url, e);
        }
        this.url = url;
        this.credentials = credentials;
    }

    public JobClientBuilder(String url, String email, String password) {
        this(url, new Credentials(email, password));
    }

    public JobClientBuilder requestInterceptor(RequestInterceptor requestInterceptor) {
        requestInterceptors.add(requestInterceptor);
        return this;
    }

    public JobClientBuilder authenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
        return this;
    }

    public JobClientBuilder logLevel(Level level) {
        this.level = level;
        return this;
    }

    public JobClientBuilder options(Options options) {
        this.options = options;
        return this;
    }

    public JobClientBuilder options(int connectTimeoutMillis, int readTimeoutMillis) {
        this.options = new Options(connectTimeoutMillis, readTimeoutMillis);
        return this;
    }

    public JobClient build() {
        Feign.Builder builder = Feign.builder()
                .decoder(new GsonDecoder(new GsonBuilder().serializeNulls().create()))
                .encoder(new GsonEncoder())
                .logLevel(this.level)
                .logger(new Slf4jLogger())
                .errorDecoder(new LLMClientErrorDecoder())
                .retryer(new LLMClientRetryer())
                .requestInterceptors(
                        this.requestInterceptors
                                .stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList()))
                .requestInterceptor(getAuthorizationInterceptor(credentials, authenticationType, url));

        if (options != null) {
            builder.options(options);
        }

        return builder.target(JobClient.class, this.url);
    }
}
