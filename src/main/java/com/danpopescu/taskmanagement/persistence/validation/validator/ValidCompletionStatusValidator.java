package com.danpopescu.taskmanagement.persistence.validation.validator;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.persistence.validation.ValidCompletionStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidCompletionStatusValidator implements ConstraintValidator<ValidCompletionStatus, Task> {

    @Override
    public void initialize(ValidCompletionStatus constraintAnnotation) {
    }

    @Override
    public boolean isValid(Task task, ConstraintValidatorContext context) {
        return (!task.isCompleted() && task.getDateCompleted() == null)
                || (task.isCompleted() && task.getDateCompleted() != null);
    }
}
