package com.danpopescu.taskmanagement.web.resource.input;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TaskResourceOutput {

    private Long id;
    private String name;
    private Long project;
    private boolean completed;
    private LocalDateTime dateCreated;
    private LocalDateTime dateCompleted;

}
