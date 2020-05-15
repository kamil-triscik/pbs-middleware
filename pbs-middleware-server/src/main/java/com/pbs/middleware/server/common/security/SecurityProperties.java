package com.pbs.middleware.server.common.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "middleware.server.security")
@Getter
@Setter
public class SecurityProperties {

    private String swagger = "/swagger-ui.html/**;/webjars/springfox-swagger-ui/**;/swagger-resources/**;/v2/api-docs/**;/csrf";
    private String cors;

}
