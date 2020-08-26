package com.danpopescu.taskmanagement.service;

import com.danpopescu.taskmanagement.domain.Project;

import java.util.Optional;

public interface ProjectService {

    Project save(Project project);

    Optional<Project> findById(Long id);

    Iterable<Project> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
}
