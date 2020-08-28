package com.danpopescu.taskmanagement.bootstrap;

import com.danpopescu.taskmanagement.domain.Project;
import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.service.ProjectService;
import com.danpopescu.taskmanagement.service.TaskService;
import com.danpopescu.taskmanagement.web.mapper.TaskMapper;
import com.danpopescu.taskmanagement.web.resource.input.TaskResourceInput;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

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

        taskService.save(Task.builder().name("Read PRO Spring 5").project(project1).build());
        taskService.save(Task.builder().name("Finish SFG course").project(project1).build());
        taskService.save(Task.builder().name("Read the Spring Security Guide").project(project1).build());
        taskService.save(Task.builder().name("Write a generic motivation letter").project(project2).build());
        taskService.save(Task.builder().name("Write to 10 recruiters").project(project2).build());

        TaskResourceInput input = new TaskResourceInput();
        input.setName("Taskinggg");
        input.setProject(project1.getId());

        System.out.println(taskMapper.asTask(input));

        Task task = Task.builder()
                .name("First name")
                .project(project1)
                .id(22L)
                .dateCreated(LocalDateTime.now())
                .completed(true)
                .build();

        Task task2 = Task.builder().id(1L).build();

        System.out.println(taskMapper.asOutput(Set.of(task, task2)));

        TaskResourceInput res = TaskResourceInput.builder()
                .name("Second name")
                .project(2L)
                .build();

        System.out.println(task);

        taskMapper.updateTask(task, res);

        System.out.println(task);
    }
}
