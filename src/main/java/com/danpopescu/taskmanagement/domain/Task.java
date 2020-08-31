package com.danpopescu.taskmanagement.domain;

import com.danpopescu.taskmanagement.persistence.validation.ConsistentDates;
import com.danpopescu.taskmanagement.persistence.validation.ValidCompletionStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@ConsistentDates
@ValidCompletionStatus

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Task extends BaseEntity {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @ManyToOne
    private Project project;

    private boolean completed;

    @NotNull
    @Past
    private LocalDateTime dateCreated;

    @Past
    private LocalDateTime dateCompleted;

    @Builder
    public Task(Long id, String name, Project project, boolean completed,
                LocalDateTime dateCreated, LocalDateTime dateCompleted) {
        super(id);
        this.name = name;
        this.project = project;
        this.completed = completed;
        this.dateCreated = dateCreated;
        this.dateCompleted = dateCompleted;
    }
}
