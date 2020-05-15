package com.pbs.middleware.server.features.filetransfer.download.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DownloadRequestValidator.class)
@Documented
public @interface ValidDownloadRequest {

    String message() default "Folder or at least files list should be filled!";

    boolean isClassType() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String fieldName() default "folder, files";

}
