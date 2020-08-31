package com.danpopescu.taskmanagement.bootstrap;

import com.danpopescu.taskmanagement.domain.Project;
import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.service.ProjectService;
import com.danpopescu.taskmanagement.service.TaskService;
import com.danpopescu.taskmanagement.web.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final TaskMapper taskMapper;

    @Override
    public void run(String... args) throws Exception {
        Project project1 = Project.builder().name("Learn Spring").build();
        projectService.save(project1);

        Project project2 = Project.builder().name("Get a job").build();
        projectService.save(project2);

        taskService.save(Task.builder().name("Read PRO Spring 5").dateCreated(LocalDateTime.now()).project(project1).build());
        taskService.save(Task.builder().name("Finish SFG course").dateCreated(LocalDateTime.now()).project(project1).build());
        taskService.save(Task.builder().name("Read the Spring Security Guide").dateCreated(LocalDateTime.now()).project(project1).build());
        taskService.save(Task.builder().name("Write a generic motivation letter").dateCreated(LocalDateTime.now()).project(project2).build());
        taskService.save(Task.builder().name("Write to 10 recruiters").dateCreated(LocalDateTime.now()).project(project2).build());
    }
}
