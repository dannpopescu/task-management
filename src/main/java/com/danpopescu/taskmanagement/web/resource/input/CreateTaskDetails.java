package com.danpopescu.taskmanagement.web.resource.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDetails {

    @NotBlank
    private String name;

    private Long project;
}
