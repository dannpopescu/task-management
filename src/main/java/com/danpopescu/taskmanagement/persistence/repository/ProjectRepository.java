package com.danpopescu.taskmanagement.persistence.repository;

import com.danpopescu.taskmanagement.domain.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {
}
