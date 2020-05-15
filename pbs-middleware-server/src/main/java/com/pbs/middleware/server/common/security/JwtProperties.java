package com.pbs.middleware.server.common.security;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "middleware.server.security.jwt")
@Getter
@Setter
public class JwtProperties {

    private Duration expiration = Duration.ofDays(1);
    private String secretkey;

}
