package com.pbs.middleware.server.features.contact.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ContactTypesValidator.class)
@Documented
public @interface ValidContactTypes {

    String message() default "Contact types list contain invalid value";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String fieldName() default "contactTypes";

}
