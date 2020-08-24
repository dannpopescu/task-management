package com.danpopescu.taskmanagement.services.impl;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.dto.TaskDTO;
import com.danpopescu.taskmanagement.exceptions.TaskNotFoundException;
import com.danpopescu.taskmanagement.repositories.TaskRepository;
import com.danpopescu.taskmanagement.services.TaskService;
import org.springframework.stereotype.Service;

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
    public Task save(TaskDTO taskDTO) {
        Task task = dtoToTask(taskDTO);
        return taskRepository.save(task);
    }

    private Task dtoToTask(TaskDTO taskDTO) {
        return Task.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle())
                .done(taskDTO.isDone())
                .build();
    }

    @Override
    public Task findById(Long id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task Not Found"));
    }

    @Override
    public Iterable<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public void deleteById(Long id) throws TaskNotFoundException {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task Not Found");
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
