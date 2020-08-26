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
        taskService.save(Task.builder().title("Read PRO Spring 5").build());
        taskService.save(Task.builder().title("Finish SFG course").build());
        taskService.save(Task.builder().title("Read the Spring Security Guide").build());
        taskService.save(Task.builder().title("Write a generic motivation letter").build());
        taskService.save(Task.builder().title("Write to 10 recruiters").build());
    }
}
