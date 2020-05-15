package com.pbs.middleware.common.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = DurationValidator.class)
@Documented
public @interface ValidDuration {

    String message() default "Invalid duration provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
