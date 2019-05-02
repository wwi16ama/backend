package com.WWI16AMA.backend_api.PlaneLog;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValiStartAndEndcountValidator implements ConstraintValidator<ValidStartAndEndcount, PlaneLogEntry> {

    @Override
    public void initialize(ValidStartAndEndcount constraintAnnotation) {
    }

    @Override
    public boolean isValid(PlaneLogEntry entry, ConstraintValidatorContext context) {
        if (entry == null) {
            return true;
        }

        return entry.getEndCount() <= entry.getStartCount();
    }
}
