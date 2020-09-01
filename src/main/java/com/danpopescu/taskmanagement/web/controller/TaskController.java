package com.danpopescu.taskmanagement.web.controller;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.service.TaskService;
import com.danpopescu.taskmanagement.web.PatchMediaType;
import com.danpopescu.taskmanagement.web.exception.ResourceNotFoundException;
import com.danpopescu.taskmanagement.web.mapper.TaskMapper;
import com.danpopescu.taskmanagement.web.resource.input.CreateTaskDetails;
import com.danpopescu.taskmanagement.web.resource.input.TaskResourceInput;
import com.danpopescu.taskmanagement.web.resource.output.TaskResourceOutput;
import com.danpopescu.taskmanagement.web.util.PatchHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.json.JsonMergePatch;
import javax.json.JsonPatch;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;
    private final TaskMapper taskMapper;
    private final ObjectMapper objectMapper;
    private final PatchHelper patchHelper;

    @GetMapping
    public Set<TaskResourceOutput> findAll() {
        Set<Task> tasks = service.findAll();
        return taskMapper.asOutput(tasks);
    }

    @GetMapping("/{id}")
    public TaskResourceOutput findById(@PathVariable Long id) {
        Task task = service.findById(id).orElseThrow(ResourceNotFoundException::new);
        return taskMapper.asOutput(task);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResourceOutput> create(@RequestBody CreateTaskDetails taskDetails,
                                                     UriComponentsBuilder uriComponentsBuilder) {

        Task task = taskMapper.asTask(taskDetails);
        task.setDateCreated(LocalDateTime.now());
        task = service.save(task);

        URI location = uriComponentsBuilder
                .path("/tasks/{id}")
                .buildAndExpand(task.getId())
                .toUri();

        return ResponseEntity.created(location).body(taskMapper.asOutput(task));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResourceOutput> replace(@PathVariable Long id,
                                                      @RequestBody TaskResourceInput resourceInput) {
        Task task = service.findById(id).orElseThrow(ResourceNotFoundException::new);
        task = taskMapper.updateTask(task, resourceInput);
        service.save(task);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}", consumes = PatchMediaType.APPLICATION_JSON_PATCH_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResourceOutput> jsonPatch(@PathVariable Long id,
                                                        @RequestBody JsonPatch mergePatchDocument) {

        Task task = service.findById(id).orElseThrow(ResourceNotFoundException::new);
        Task updated = patchHelper.patch(mergePatchDocument, task, Task.class);
        service.save(updated);
        return ResponseEntity.ok(taskMapper.asOutput(updated));
    }

    @PatchMapping(path = "/{id}", consumes = PatchMediaType.APPLICATION_MERGE_PATCH_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskResourceOutput> mergePatch(@PathVariable Long id,
                                                         @RequestBody JsonMergePatch patchDocument) {

        Task task = service.findById(id).orElseThrow(ResourceNotFoundException::new);
        Task updated = patchHelper.mergePatch(patchDocument, task, Task.class);
        service.save(updated);
        return ResponseEntity.ok(taskMapper.asOutput(updated));
    }

    @PutMapping(path = "/{id}/completed", headers = "content-length=0")
    public ResponseEntity<Void> markCompleted(@PathVariable Long id) {
        Task task = service.findById(id).orElseThrow(ResourceNotFoundException::new);
        if (task.isCompleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task already completed");
        }
        task.setCompleted(true);
        task.setDateCompleted(LocalDateTime.now());
        service.save(task);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{id}/completed")
    public ResponseEntity<Void> markUncompleted(@PathVariable Long id) {
        Task task = service.findById(id).orElseThrow(ResourceNotFoundException::new);
        if (!task.isCompleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task not completed");
        }
        task.setCompleted(false);
        task.setDateCompleted(null);
        service.save(task);
        return ResponseEntity.noContent().build();
    }
}
