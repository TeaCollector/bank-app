package ru.alex.msstatement.util.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ru.alex.msstatement.util.validation.annotation.IsLatin;


public class LatinValidator implements ConstraintValidator<IsLatin, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null) {
            return value.matches("^[a-zA-Z]+$");
        }
        return true;
    }
}
