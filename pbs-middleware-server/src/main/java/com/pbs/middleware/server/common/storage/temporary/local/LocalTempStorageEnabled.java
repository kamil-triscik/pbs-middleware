package com.pbs.middleware.server.common.storage.temporary.local;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * This annotation is used to allow components necessary for "local(disk)" type of temporary storage.
 *
 * @author Kamil Triscik
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ConditionalOnProperty(
        prefix = "middleware.server.features.storage.temporary",
        value = "type",
        havingValue = "local")
public @interface LocalTempStorageEnabled {
}
