package ru.alex.mscalc.util.validation.annotation;

import ru.alex.mscalc.util.validation.LatinValidator;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;


@Constraint(validatedBy = LatinValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsLatin {

    String message() default "Your first/middle/last name must be only latin";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
