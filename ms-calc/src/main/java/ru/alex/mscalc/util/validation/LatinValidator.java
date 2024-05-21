package ru.alex.mscalc.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class LatinValidator implements ConstraintValidator<IsLatin, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("^[a-zA-Z]+$");
    }
}
