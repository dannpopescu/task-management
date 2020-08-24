package com.danpopescu.taskmanagement.controllers;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.services.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public Iterable<Task> getAllTasks() {
        return taskService.getAll();
    }
}
