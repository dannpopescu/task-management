package com.danpopescu.taskmanagement.web.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestApiPropertyValidationError {

    private final String property;
    private final Object invalidValue;
    private final String message;
}
