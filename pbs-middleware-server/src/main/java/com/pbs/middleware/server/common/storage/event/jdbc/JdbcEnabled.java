package com.pbs.middleware.server.common.storage.event.jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ConditionalOnProperty(
        prefix = "middleware.server.db",
        name = "type",
        havingValue = "jdbc",
        matchIfMissing = true
)
public @interface JdbcEnabled {
}
