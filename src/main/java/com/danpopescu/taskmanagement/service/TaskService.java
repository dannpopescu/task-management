package com.danpopescu.taskmanagement.service;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.web.exception.ResourceNotFoundException;
import com.danpopescu.taskmanagement.web.resource.TaskDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public interface TaskService {

    Task save(Task task);

    Task save(TaskDTO taskDTO);

    Optional<Task> findById(Long id);

    Set<Task> findAll();

    void deleteById(Long id) throws ResourceNotFoundException;

    void deleteAll(Iterable<Task> tasks);

    boolean existsById(Long id);
}
