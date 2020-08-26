package com.danpopescu.taskmanagement.repository;

import com.danpopescu.taskmanagement.domain.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
