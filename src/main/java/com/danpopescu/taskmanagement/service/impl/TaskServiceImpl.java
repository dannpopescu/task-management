package com.danpopescu.taskmanagement.service.impl;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.persistence.repository.TaskRepository;
import com.danpopescu.taskmanagement.service.TaskService;
import com.danpopescu.taskmanagement.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(@Valid Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Set<Task> findAll() {
        Set<Task> tasks = new HashSet<>();
        taskRepository.findAll().forEach(tasks::add);
        return tasks;
    }

    @Override
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteAll(Iterable<Task> tasks) {
        taskRepository.deleteAll(tasks);
    }

    @Override
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }
}
