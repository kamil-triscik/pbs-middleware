package cz.muni.ll.middleware.client.download.client;

import cz.muni.ll.middleware.client.authorization.AuthenticationType;
import cz.muni.ll.middleware.client.authorization.Utils;
import cz.muni.ll.middleware.client.authorization.Credentials;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientErrorDecoder;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientRetryer;
import feign.Feign;
import feign.Logger.Level;
import feign.Request.Options;
import feign.RequestInterceptor;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static feign.Logger.Level.NONE;

public class DownloadClientBuilder {

    private final Credentials credentials;
    private final String url;
    private final List<RequestInterceptor> requestInterceptors = new LinkedList<>();
    private AuthenticationType authenticationType = AuthenticationType.BASIC;
    private Level level = NONE;
    private Options options = null;

    public DownloadClientBuilder(String url, Credentials credentials) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid url provided: " + url, e);
        }
        this.url = url;
        this.credentials = credentials;
    }

    public DownloadClientBuilder(String url, String email, String password) {
        this(url, new Credentials(email, password));
    }

    public DownloadClientBuilder requestInterceptor(RequestInterceptor requestInterceptor) {
        requestInterceptors.add(requestInterceptor);
        return this;
    }

    public DownloadClientBuilder authenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
        return this;
    }

    public DownloadClientBuilder logLevel(Level level) {
        this.level = level;
        return this;
    }

    public DownloadClientBuilder options(Options options) {
        this.options = options;
        return this;
    }

    public DownloadClientBuilder options(int connectTimeoutMillis, int readTimeoutMillis) {
        this.options = new Options(connectTimeoutMillis, readTimeoutMillis);
        return this;
    }

    public DownloadClient build() {
        Feign.Builder builder = Feign.builder()
                .decoder(new DownloadDecoder())
                .encoder(new GsonEncoder())
                .logger(new Slf4jLogger())
                .logLevel(this.level)
                .errorDecoder(new LLMClientErrorDecoder())
                .retryer(new LLMClientRetryer())
                .requestInterceptors(
                        this.requestInterceptors
                                .stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList()))
                .requestInterceptor(Utils.getAuthorizationInterceptor(credentials, authenticationType, url));

        if (options != null) {
            builder.options(options);
        }

        return builder.target(DownloadClient.class, this.url);
    }
}
