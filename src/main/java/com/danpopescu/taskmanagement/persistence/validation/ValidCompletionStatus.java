package com.danpopescu.taskmanagement.persistence.validation;

import com.danpopescu.taskmanagement.persistence.validation.validator.ValidCompletionStatusValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidCompletionStatusValidator.class})
@Documented
public @interface ValidCompletionStatus {

    String message() default "must be completed and have a non-null dateCompleted, or uncompleted and a null dateCompleted";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
