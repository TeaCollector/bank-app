package ru.alex.mscalc.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ru.alex.mscalc.util.validation.annotation.CanExists;


public class CanExistsValidator implements ConstraintValidator<CanExists, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }
}
