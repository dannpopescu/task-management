package com.danpopescu.taskmanagement.domain;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseEntity {

    private String name;

    @Builder
    public Project(Long id, String name) {
        super(id);
        this.name = name;
    }
}
