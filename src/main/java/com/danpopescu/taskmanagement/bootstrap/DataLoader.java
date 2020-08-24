package com.danpopescu.taskmanagement.bootstrap;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.services.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final TaskService taskService;

    public DataLoader(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void run(String... args) throws Exception {
        taskService.save(new Task("Read PRO Spring 5"));
        taskService.save(new Task("Finish SFG course"));
        taskService.save(new Task("Read the Spring Security Guide"));
        taskService.save(new Task("Write a generic motivation letter"));
        taskService.save(new Task("Write to 10 recruiters"));
    }
}
