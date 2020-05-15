package cz.muni.ll.middleware.client.upload.client;

import cz.muni.ll.middleware.client.authorization.AuthenticationType;
import cz.muni.ll.middleware.client.authorization.Credentials;
import cz.muni.ll.middleware.client.authorization.Utils;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientEncoder;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientErrorDecoder;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientRetryer;
import feign.Feign;
import feign.Logger.Level;
import feign.Request.Options;
import feign.RequestInterceptor;
import feign.gson.GsonDecoder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static feign.Logger.Level.NONE;

public class UploadClientBuilder {

    private final Credentials credentials;
    private final String url;
    private final List<RequestInterceptor> requestInterceptors = new LinkedList<>();
    private AuthenticationType authenticationType = AuthenticationType.BASIC;
    private Level level = NONE;
    private Options options = null;

    public UploadClientBuilder(String url, Credentials credentials) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid url provided: " + url, e);
        }
        this.url = url;
        this.credentials = credentials;
    }

    public UploadClientBuilder(String url, String email, String password) {
        this(url, new Credentials(email, password));
    }

    public UploadClientBuilder requestInterceptor(RequestInterceptor requestInterceptor) {
        requestInterceptors.add(requestInterceptor);
        return this;
    }

    public UploadClientBuilder authenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
        return this;
    }

    public UploadClientBuilder logLevel(Level level) {
        this.level = level;
        return this;
    }

    public UploadClientBuilder options(Options options) {
        this.options = options;
        return this;
    }

    public UploadClientBuilder options(int connectTimeoutMillis, int readTimeoutMillis) {
        this.options = new Options(connectTimeoutMillis, readTimeoutMillis);
        return this;
    }

    public UploadClient build() {
        Feign.Builder builder = Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new LLMClientEncoder())
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

        return new UploadClient(builder.target(UploadClientDescriptor.class, this.url));
    }
}
