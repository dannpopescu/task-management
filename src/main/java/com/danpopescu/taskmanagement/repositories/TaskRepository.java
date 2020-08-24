package com.danpopescu.taskmanagement.repositories;

import com.danpopescu.taskmanagement.domain.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
}
