package com.pbs.middleware.server.features.script.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GroovyValidator implements ConstraintValidator<ValidGroovy, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        // TODO: 30.11.19
//        try {
//            new GroovyShell().parse(value);
//            return true;
//        } catch(MultipleCompilationErrorsException cfe) {
//            ErrorCollector errorCollector = cfe.getErrorCollector();
//            System.out.println("Errors: "+errorCollector.getErrorCount());
//            return false;
//        }
        return true;
    }
}
