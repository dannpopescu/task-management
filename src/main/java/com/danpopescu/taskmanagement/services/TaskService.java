package com.danpopescu.taskmanagement.services;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.dto.TaskDTO;
import com.danpopescu.taskmanagement.exceptions.TaskNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {

    Task save(Task task);

    Task save(TaskDTO taskDTO);

    Task findById(Long id) throws TaskNotFoundException;

    Iterable<Task> findAll();

    void deleteById(Long id) throws TaskNotFoundException;

    void deleteAll(Iterable<Task> tasks);

    boolean existsById(Long id);
}
