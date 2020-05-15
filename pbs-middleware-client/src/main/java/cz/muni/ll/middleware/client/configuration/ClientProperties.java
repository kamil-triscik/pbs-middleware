package cz.muni.ll.middleware.client.configuration;

import cz.muni.ll.middleware.client.authorization.Credentials;
import feign.Logger;
import feign.Request;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ClientProperties {

    private final Credentials credentials;

    private final String url;

    private final Logger.Level logLevel;

    private final Request.Options options;

}
