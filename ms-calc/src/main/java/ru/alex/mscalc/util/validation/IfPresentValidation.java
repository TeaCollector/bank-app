package ru.alex.mscalc.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class IfPresentValidation implements ConstraintValidator<IfPresent, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null;
    }
}
