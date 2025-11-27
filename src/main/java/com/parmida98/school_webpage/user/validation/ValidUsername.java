package com.parmida98.school_webpage.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {}) // ingen egen validator behövs – komponerar bara befintliga
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Size(min = 2, max = 25, message = "Username length should be between 2-25")
@NotBlank(message = "Username may not contain whitespace characters only")
public @interface ValidUsername {

    String message() default "Invalid username";

    Class[] groups() default {};

    Class[] payload() default {};
}
