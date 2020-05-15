package com.pbs.middleware.server.common.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "middleware.server.security.url")
@Getter
@Setter
public class UrlProperties {

    private String auth = "/auth";
    private String logout = "/logout";

}
