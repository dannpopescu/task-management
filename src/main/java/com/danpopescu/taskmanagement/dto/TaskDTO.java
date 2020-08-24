package com.danpopescu.taskmanagement.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskDTO {

    private Long id;
    private String title;
    private boolean done;

    @Builder
    public TaskDTO(Long id, String title, boolean done) {
        this.id = id;
        this.title = title;
        this.done = done;
    }
}
