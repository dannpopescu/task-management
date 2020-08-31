package com.danpopescu.taskmanagement.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void nullName() {
        Task task = Task.builder()
                .dateCreated(LocalDateTime.now())
                .build();

        validateOneConstraint("must not be blank", task);
    }

    @Test
    void emptyName() {
        Task task = Task.builder()
                .name("    ")
                .dateCreated(LocalDateTime.now())
                .build();

        validateOneConstraint("must not be blank", task);
    }

    @Test
    void completionDateBeforeCreationDate() {
        Task task = Task.builder()
                .name("Task")
                .dateCompleted(LocalDateTime.now())
                .completed(true)
                .dateCreated(LocalDateTime.now())
                .build();

        validateOneConstraint("creation date must be before completion date", task);
    }

    @Test
    void completedButNullCompletionDate() {
        Task task = Task.builder()
                .name("Task")
                .dateCreated(LocalDateTime.now())
                .completed(true)
                .build();

        validateOneConstraint("must be completed and have a non-null dateCompleted, or " +
                "uncompleted and a null dateCompleted", task);
    }

    @Test
    void uncompletedButNonNullCompletionDate() {
        Task task = Task.builder()
                .name("Task")
                .dateCreated(LocalDateTime.now())
                .dateCompleted(LocalDateTime.now())
                .build();

        validateOneConstraint("must be completed and have a non-null dateCompleted, or " +
                "uncompleted and a null dateCompleted", task);
    }

    @Test
    void futureCreationDate() {
        Task task = Task.builder()
                .name("Task")
                .dateCreated(LocalDateTime.of(2100, 12, 31, 23, 59))
                .build();

        validateOneConstraint("must be a past date", task);
    }

    @Test
    void futureCompletionDate() {
        Task task = Task.builder()
                .name("Task")
                .dateCreated(LocalDateTime.now())
                .completed(true)
                .dateCompleted(LocalDateTime.of(2100, 12, 31, 23, 59))
                .build();

        validateOneConstraint("must be a past date", task);
    }

    @Test
    void nameTooShort() {
        Task task = Task.builder()
                .name("12")
                .dateCreated(LocalDateTime.now())
                .build();

        validateOneConstraint("size must be between 3 and 50", task);
    }

    @Test
    void nameTooLong() {
        Task task = Task.builder()
                .name("this name is longer than 50 characters permitted...")
                .dateCreated(LocalDateTime.now())
                .build();

        validateOneConstraint("size must be between 3 and 50", task);
    }

    @Test
    void nullCreationDate() {
        Task task = Task.builder()
                .name("Task")
                .build();

        validateOneConstraint("must not be null", task);
    }

    private void validateOneConstraint(String expectedMessage, Task task) {
        Set<ConstraintViolation<Task>> constraintViolations = validator.validate(task);

        assertEquals(1, constraintViolations.size());
        assertEquals(expectedMessage, constraintViolations.iterator().next().getMessage());
    }
}