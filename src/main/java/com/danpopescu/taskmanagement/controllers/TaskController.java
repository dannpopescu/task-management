package com.danpopescu.taskmanagement.controllers;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.dto.TaskDTO;
import com.danpopescu.taskmanagement.exceptions.TaskNotFoundException;
import com.danpopescu.taskmanagement.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping({"", "/"})
    public Iterable<TaskDTO> findAll() {
        return asDTOs(taskService.findAll());
    }

    @GetMapping("/{id}")
    public TaskDTO findById(@PathVariable Long id) throws TaskNotFoundException {
        return asDTO(taskService.findById(id));
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@RequestBody TaskDTO taskDTO) {
        return asDTO(taskService.save(taskDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) throws TaskNotFoundException {
        taskService.deleteById(id);
    }

    @PutMapping("/{id}")
    public TaskDTO update(@PathVariable Long id, @RequestBody TaskDTO taskDTO, HttpServletResponse response) {
        HttpStatus status = taskService.existsById(id) ? HttpStatus.OK : HttpStatus.CREATED;
        response.setStatus(status.value());
        taskDTO.setId(id);
        return asDTO(taskService.save(taskDTO));
    }

    private Iterable<TaskDTO> asDTOs(Iterable<Task> tasks) {
        ArrayList<TaskDTO> taskDTOs = new ArrayList<>();
        tasks.forEach(task -> taskDTOs.add(asDTO(task)));
        return taskDTOs;
    }

    private TaskDTO asDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .done(task.isDone())
                .build();
    }
}
