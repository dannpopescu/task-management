package com.danpopescu.taskmanagement.persistence.validation.validator;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.persistence.validation.ConsistentDates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConsistentDateValidator implements ConstraintValidator<ConsistentDates, Task> {

    @Override
    public void initialize(ConsistentDates constraintAnnotation) {
    }

    @Override
    public boolean isValid(Task task, ConstraintValidatorContext context) {
        if (task.getDateCompleted() == null) {
            return true;
        } else if (task.getDateCreated() != null) {
            return task.getDateCreated().compareTo(task.getDateCompleted()) < 0;
        }

        return false;
    }
}
