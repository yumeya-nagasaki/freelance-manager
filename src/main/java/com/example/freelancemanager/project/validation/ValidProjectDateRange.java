package com.example.freelancemanager.project.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ProjectDateRangeValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface ValidProjectDateRange {
    
    String message() default "終了日は開始日以降の日付にしてください";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
