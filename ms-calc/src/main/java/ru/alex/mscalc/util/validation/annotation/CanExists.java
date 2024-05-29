package ru.alex.mscalc.util.validation.annotation;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;
import ru.alex.mscalc.util.validation.CanExistsValidator;


@Constraint(validatedBy = CanExistsValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CanExists {

    String message() default "Your age is less than 18 years old";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
