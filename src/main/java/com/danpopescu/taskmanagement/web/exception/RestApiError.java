package com.danpopescu.taskmanagement.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class RestApiError {

    private final String error;
    private final String message;
    private final int status;
    private final ZonedDateTime timestamp;
    private final String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<?> details;
}
