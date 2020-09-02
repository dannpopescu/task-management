package com.danpopescu.taskmanagement.service;

import com.danpopescu.taskmanagement.domain.Project;

import java.util.Optional;
import java.util.Set;

public interface ProjectService {

    Project save(Project project);

    Optional<Project> findById(Long id);

    Set<Project> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
}
