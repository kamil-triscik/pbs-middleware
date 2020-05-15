package cz.muni.ll.middleware.client.rest.exceptions;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

import static java.lang.annotation.ElementType.METHOD;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = METHOD)
public @interface NotSupported {

    @AliasFor(
            annotation = Deprecated.class
    )
    String name() default "";

    /**
     * Return reason, why method is not supported.
     *
     * @return the reason string
     * @since 0.0.8
     */
    String reason() default "";


}
