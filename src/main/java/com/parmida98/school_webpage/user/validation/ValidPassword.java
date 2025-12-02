package com.parmida98.school_webpage.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Pattern(
        regexp = "^" +
                "(?=.*[a-z])" +        // at least one lowercase letter
                "(?=.*[A-Z])" +        // at least one uppercase letter
                "(?=.*[0-9])" +        // at least one digit
                "(?=.*[@$!%*?&])" +   // at least one special character
                "[A-Za-z0-9 @$!%*?&]+$"
)
@Size(min = 8, max = 80)
public @interface ValidPassword {

    String message() default "Invalid password";

    Class<?>[] groups() default {};                     // Standard för alla Bean Validation-annotations

    Class<? extends Payload>[] payload() default {};
}
