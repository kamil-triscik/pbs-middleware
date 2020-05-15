package com.pbs.middleware.server.features.script.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = GroovyValidator.class)
@Documented
public @interface ValidGroovy {

    String message() default "Invalid groovy script.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String fieldName() default "code";

}
