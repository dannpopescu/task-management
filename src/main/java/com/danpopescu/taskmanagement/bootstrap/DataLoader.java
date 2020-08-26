package com.danpopescu.taskmanagement.bootstrap;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.service.TaskService;
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
        taskService.save(Task.builder().name("Read PRO Spring 5").build());
        taskService.save(Task.builder().name("Finish SFG course").build());
        taskService.save(Task.builder().name("Read the Spring Security Guide").build());
        taskService.save(Task.builder().name("Write a generic motivation letter").build());
        taskService.save(Task.builder().name("Write to 10 recruiters").build());
    }
}
