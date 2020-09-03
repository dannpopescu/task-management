package com.danpopescu.taskmanagement.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RestApiError> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        RestApiError error = RestApiError.builder()
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .timestamp(ZonedDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        List<RestApiPropertyValidationError> validationErrors = ex.getConstraintViolations().stream()
                .map(this::toPropertyValidationError)
                .collect(Collectors.toList());

        RestApiError error = RestApiError.builder()
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .path(req.getRequestURI())
                .timestamp(ZonedDateTime.now())
                .details(validationErrors)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private RestApiPropertyValidationError toPropertyValidationError(ConstraintViolation<?> violation) {
        return RestApiPropertyValidationError.builder()
                .property(violation.getPropertyPath().toString())
                .invalidValue(violation.getInvalidValue())
                .message(violation.getMessage())
                .build();
    }
}
