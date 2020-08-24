package com.danpopescu.taskmanagement.services;

import com.danpopescu.taskmanagement.domain.Task;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TaskService {

    Task save(Task task);

    Optional<Task> getById(Long id);

    Iterable<Task> getAll();

    void deleteById(Long id);

    void deleteAll(Iterable<Task> tasks);

}
