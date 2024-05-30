package ru.alex.mscalc.util.validation;

import ru.alex.mscalc.util.validation.annotation.Adult;

import java.time.LocalDate;
import java.time.Period;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    @Override
    public boolean isValid(LocalDate receivedData, ConstraintValidatorContext context) {
        LocalDate dateNow = LocalDate.now();
        return Period.between(receivedData, dateNow).getYears() > 18;
    }
}
