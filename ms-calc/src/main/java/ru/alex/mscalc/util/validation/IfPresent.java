package ru.alex.mscalc.util.validation;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;


@Constraint(validatedBy = IfPresentValidation.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IfPresent {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}