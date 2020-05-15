package com.pbs.middleware.server.common.security;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class SecurityInfo implements InfoContributor {

    @Value("${middleware.server.security.auth.type:BASIC}")
    private AuthenticationType authenticationType;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("security", Map.of("authentication", Map.of("type", authenticationType)));
    }
}
