package com.danpopescu.taskmanagement.persistence.validation;

import com.danpopescu.taskmanagement.persistence.validation.validator.ConsistentDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

/**
 * The annotated task's creation date must be before
 * it's completion date.
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConsistentDateValidator.class})
@Documented
public @interface ConsistentDates {

    String message() default "creation date must be before completion date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
