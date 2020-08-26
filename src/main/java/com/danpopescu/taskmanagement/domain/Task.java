package com.danpopescu.taskmanagement.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Task extends BaseEntity {

    private String name;

    @ManyToOne
    private Project project;

    private boolean completed;

    @CreationTimestamp
    private LocalDateTime dateCreated;

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
