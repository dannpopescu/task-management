package com.danpopescu.taskmanagement.web.resource.input;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskResourceInput {

    private String name;
    private Long project;

}
