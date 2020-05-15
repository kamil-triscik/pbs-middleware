package com.pbs.middleware.server.features.connection.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = ConnectionUniqueNameValidator.class)
@Documented
public @interface UniqueName {

    String message() default "Connection name already exists";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String fieldName() default "name";

    boolean isClassType() default true;

}
