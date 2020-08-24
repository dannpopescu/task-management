package com.danpopescu.taskmanagement.services.impl;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.repositories.TaskRepository;
import com.danpopescu.taskmanagement.services.TaskService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Iterable<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteAll(Iterable<Task> tasks) {
        taskRepository.deleteAll(tasks);
    }
}
