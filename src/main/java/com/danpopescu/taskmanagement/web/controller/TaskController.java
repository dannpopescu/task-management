package com.danpopescu.taskmanagement.web.controller;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.service.TaskService;
import com.danpopescu.taskmanagement.web.exception.ResourceNotFoundException;
import com.danpopescu.taskmanagement.web.mapper.TaskMapper;
import com.danpopescu.taskmanagement.web.resource.input.TaskResourceInput;
import com.danpopescu.taskmanagement.web.resource.input.TaskResourceOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;
    private final TaskMapper mapper;

    @GetMapping
    public Set<TaskResourceOutput> findAll() {
        Set<Task> tasks = service.findAll();
        return mapper.asOutput(tasks);
    }

    @GetMapping("/{id}")
    public TaskResourceOutput findById(@PathVariable Long id) {
        Task task = service.findById(id).orElseThrow(ResourceNotFoundException::new);
        return mapper.asOutput(task);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResourceOutput> create(@RequestBody TaskResourceInput taskResource,
                                                     UriComponentsBuilder uriComponentsBuilder) {

        Task task = mapper.asTask(taskResource);
        task = service.save(task);

        URI location = uriComponentsBuilder
                .path("/tasks/{id}")
                .buildAndExpand(task.getId())
                .toUri();

        return ResponseEntity.created(location).body(mapper.asOutput(task));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResourceOutput> replace(@PathVariable Long id,
                                                      @RequestBody TaskResourceInput resourceInput) {
        Task task = service.findById(id).orElseThrow(ResourceNotFoundException::new);
        task = mapper.updateTask(task, resourceInput);
        service.save(task);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
