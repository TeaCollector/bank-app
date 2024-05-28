package ru.alex.mscalc.util.validation;

import ru.alex.mscalc.util.validation.annotation.IsLatin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class LatinValidator implements ConstraintValidator<IsLatin, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("^[a-zA-Z]+$");
    }
}
