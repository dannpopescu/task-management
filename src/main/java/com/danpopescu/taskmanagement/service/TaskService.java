package com.danpopescu.taskmanagement.service;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@Service
public interface TaskService {

    Task save(@Valid Task task);

    Optional<Task> findById(Long id);

    Set<Task> findAll();

    void deleteById(Long id) throws ResourceNotFoundException;

    void deleteAll(Iterable<Task> tasks);

    boolean existsById(Long id);
}
