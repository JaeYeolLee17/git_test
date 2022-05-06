package com.e4motion.challenge.common.annotation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

    @Override
    public void initialize(NullOrNotBlank constraintAnnotation) {
        // Nothing to do here
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.debug("NullOrNotBlankValidator >> " + value + "...");
        if (value != null) {
            log.debug("NullOrNotBlankValidator >> " + value.trim() + "...");
            log.debug("NullOrNotBlankValidator >> " + value.trim().length() + "...");
        }
        return value == null || value.trim().length() > 0;
    }
}
