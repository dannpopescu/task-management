package com.danpopescu.taskmanagement.web.controller;

import com.danpopescu.taskmanagement.domain.Project;
import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.service.TaskService;
import com.danpopescu.taskmanagement.web.PatchMediaType;
import com.danpopescu.taskmanagement.web.exception.ResourceNotFoundException;
import com.danpopescu.taskmanagement.web.mapper.TaskMapper;
import com.danpopescu.taskmanagement.web.resource.input.TaskResourceInput;
import com.danpopescu.taskmanagement.web.resource.output.TaskResourceOutput;
import com.danpopescu.taskmanagement.web.util.PatchHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.json.Json;
import javax.json.JsonMergePatch;
import javax.json.JsonPatch;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @MockBean
    TaskService service;

    @MockBean
    TaskMapper mapper;

    @MockBean
    PatchHelper patchHelper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    static final long TASK_ID = 1L;
    static final long PROJECT_ID = 5L;
    static final Project PROJECT = Project.builder().id(PROJECT_ID).name("Random Project").build();

    @Test
    void findAll_ShouldReturnListOfTasks_WhenFound() throws Exception {
        Task task1 = Task.builder()
                .id(1L)
                .name("First task")
                .project(PROJECT)
                .dateCreated(LocalDateTime.now())
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .name("Second task")
                .project(PROJECT)
                .completed(true)
                .dateCreated(LocalDateTime.now())
                .dateCompleted(LocalDateTime.now())
                .build();

        Set<Task> tasks = Set.of(task1, task2);
        Set<TaskResourceOutput> outputs = Set.of(asTaskResourceOutput(task1), asTaskResourceOutput(task2));

        when(service.findAll()).thenReturn(tasks);
        when(mapper.asOutput(tasks)).thenReturn(outputs);

        this.mockMvc.perform(
                get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(outputs)));

        verify(mapper).asOutput(tasks);
        verify(service).findAll();
        verifyNoMoreInteractions(mapper);
        verifyNoMoreInteractions(service);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNotFound() throws Exception {
        Set<Task> tasks = new HashSet<>();
        when(service.findAll()).thenReturn(tasks);
        when(mapper.asOutput(tasks)).thenReturn(new HashSet<>());

        this.mockMvc.perform(
                get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));

        verify(mapper).asOutput(tasks);
        verify(service).findAll();
        verifyNoMoreInteractions(mapper);
        verifyNoMoreInteractions(service);
    }

    @Test
    void findById_ShouldReturnTask_WhenFound() throws Exception {
        Task task = Task.builder()
                .id(TASK_ID)
                .name("First task")
                .project(PROJECT)
                .dateCreated(LocalDateTime.now())
                .build();

        TaskResourceOutput output = asTaskResourceOutput(task);

        when(service.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(mapper.asOutput(task)).thenReturn(output);

        this.mockMvc.perform(
                get("/tasks/{id}", TASK_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(output)));

        verify(mapper).asOutput(task);
        verify(service).findById(TASK_ID);
        verifyNoMoreInteractions(mapper);
        verifyNoMoreInteractions(service);
    }

    @Test
    void findById_ShouldReturn404_WhenNotFound() throws Exception {
        when(service.findById(TASK_ID)).thenReturn(Optional.empty());

        this.mockMvc.perform(
                get("/tasks/{id}", TASK_ID))
                .andExpect(status().isNotFound());

        verify(service).findById(TASK_ID);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(mapper);
    }

    @Test
    void create_ShouldReturn201_WhenCreated() throws Exception {
        TaskResourceInput input = TaskResourceInput.builder()
                .name("Lorem ipsum")
                .project(PROJECT_ID)
                .build();

        Task task = asTask(input, null);

        Task added = Task.builder()
                .id(TASK_ID)
                .name("Lorem ipsum")
                .project(PROJECT)
                .dateCreated(task.getDateCreated())
                .build();

        TaskResourceOutput output = asTaskResourceOutput(added);

        when(mapper.asTask(input)).thenReturn(task);
        when(service.save(task)).thenReturn(added);
        when(mapper.asOutput(added)).thenReturn(output);

        this.mockMvc.perform(
                post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.containsString("/tasks/1")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(output)));

        verify(mapper).asTask(input);
        verify(mapper).asOutput(added);
        verify(service).save(task);
        verifyNoMoreInteractions(mapper);
        verifyNoMoreInteractions(service);
    }

    @Test
    void replace_ShouldReturn204_WhenReplaced() throws Exception {
        TaskResourceInput input = TaskResourceInput.builder()
                .name("Random task")
                .project(PROJECT_ID)
                .completed(true)
                .dateCreated(LocalDateTime.now())
                .dateCompleted(LocalDateTime.now())
                .build();

        Task foundTask = Task.builder().id(TASK_ID).build();
        Task updatedTask = asTask(input, TASK_ID);

        when(service.findById(TASK_ID)).thenReturn(Optional.of(foundTask));
        when(mapper.updateTask(foundTask, input)).thenReturn(updatedTask);
        when(service.save(updatedTask)).thenReturn(updatedTask);

        this.mockMvc.perform(
                put("/tasks/{id}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNoContent());

        verify(service).findById(TASK_ID);
        verify(mapper).updateTask(foundTask, input);
        verify(service).save(updatedTask);
        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void replace_ShouldReturn404_WhenNotFound() throws Exception {
        TaskResourceInput input = TaskResourceInput.builder()
                .name("Random task")
                .project(PROJECT_ID)
                .completed(true)
                .dateCreated(LocalDateTime.now())
                .dateCompleted(LocalDateTime.now())
                .build();

        when(service.findById(1L)).thenReturn(Optional.empty());

        this.mockMvc.perform(
                put("/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());

        verify(service).findById(1L);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(mapper);
    }

    @Test
    void delete_ShouldReturn204_WhenDeleted() throws Exception {
        doNothing().when(service).deleteById(TASK_ID);

        this.mockMvc.perform(
                delete("/tasks/{id}", TASK_ID))
                .andExpect(status().isNoContent());

        verify(service).deleteById(TASK_ID);
        verifyNoMoreInteractions(service);
    }

    @Test
    void delete_ShouldReturn404_WhenNotFound() throws Exception {
        doThrow(ResourceNotFoundException.class).when(service).deleteById(TASK_ID);

        this.mockMvc.perform(
                delete("/tasks/{id}", TASK_ID))
                .andExpect(status().isNotFound());

        verify(service).deleteById(TASK_ID);
        verifyNoMoreInteractions(service);
    }

    @Test
    void jsonPatch_ShouldReturn200_WhenUpdated() throws Exception {
        Task task = Task.builder()
                .id(TASK_ID)
                .name("Random Task")
                .dateCreated(LocalDateTime.now())
                .completed(true)
                .dateCompleted(LocalDateTime.now())
                .build();

        Task updatedTask = Task.builder()
                .id(TASK_ID)
                .name("Updated Random Task")
                .dateCreated(task.getDateCreated())
                .build();

        JsonPatch patch = Json.createPatchBuilder()
                .replace("/name", "Updated Random Task")
                .replace("/completed", false)
                .remove("/dateCompleted")
                .build();

        TaskResourceOutput output = asTaskResourceOutput(updatedTask);

        when(service.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(patchHelper.patch(any(), eq(task), eq(Task.class))).thenReturn(updatedTask);
        when(service.save(updatedTask)).thenReturn(updatedTask);
        when(mapper.asOutput(updatedTask)).thenReturn(output);

        this.mockMvc.perform(
                patch("/tasks/{id}", TASK_ID)
                        .contentType(PatchMediaType.APPLICATION_JSON_PATCH)
                        .characterEncoding("utf-8")
                        .content(patch.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(output)));

        verify(service).findById(TASK_ID);
        verify(patchHelper).patch(any(), eq(task), eq(Task.class));
        verify(service).save(updatedTask);
        verify(mapper).asOutput(updatedTask);
        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(patchHelper);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void jsonPatch_ShouldReturn404_WhenNotFound() throws Exception {
        JsonPatch patch = Json.createPatchBuilder()
                .replace("/name", "Updated Random Task")
                .replace("/completed", false)
                .remove("/dateCompleted")
                .build();

        when(service.findById(TASK_ID)).thenReturn(Optional.empty());

        this.mockMvc.perform(
                patch("/tasks/{id}", TASK_ID)
                        .contentType(PatchMediaType.APPLICATION_JSON_PATCH)
                        .characterEncoding("utf-8")
                        .content(patch.toString()))
                .andExpect(status().isNotFound());

        verify(service).findById(TASK_ID);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(mapper);
        verifyNoInteractions(patchHelper);
    }

    @Test
    void mergePatch_ShouldReturn200_WhenPatched() throws Exception {
        Task task = Task.builder()
                .id(TASK_ID)
                .name("Random Task")
                .dateCreated(LocalDateTime.now())
                .completed(true)
                .dateCompleted(LocalDateTime.now())
                .build();

        Task updatedTask = Task.builder()
                .id(TASK_ID)
                .name("Updated Random Task")
                .dateCreated(task.getDateCreated())
                .build();

        JsonMergePatch patch = Json.createMergePatch(Json.createObjectBuilder()
                .add("name", "Updated Random Task")
                .add("completed", false)
                .addNull("dateCompleted")
                .build());

        TaskResourceOutput output = asTaskResourceOutput(updatedTask);

        when(service.findById(TASK_ID)).thenReturn(Optional.of(task));
        when(patchHelper.mergePatch(any(), eq(task), eq(Task.class))).thenReturn(updatedTask);
        when(service.save(updatedTask)).thenReturn(updatedTask);
        when(mapper.asOutput(updatedTask)).thenReturn(output);

        this.mockMvc.perform(
                patch("/tasks/{id}", TASK_ID)
                        .contentType(PatchMediaType.APPLICATION_MERGE_PATCH)
                        .characterEncoding("utf-8")
                        .content(patch.toJsonValue().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(output)));

        verify(service).findById(TASK_ID);
        verify(patchHelper).mergePatch(any(), eq(task), eq(Task.class));
        verify(service).save(updatedTask);
        verify(mapper).asOutput(updatedTask);
        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(patchHelper);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void mergePatch_ShouldReturn404_WhenNotFound() throws Exception {
        JsonMergePatch patch = Json.createMergePatch(Json.createObjectBuilder()
                .add("name", "Updated Random Task")
                .add("completed", false)
                .addNull("dateCompleted")
                .build());

        when(service.findById(TASK_ID)).thenReturn(Optional.empty());

        this.mockMvc.perform(
                patch("/tasks/{id}", TASK_ID)
                        .contentType(PatchMediaType.APPLICATION_MERGE_PATCH)
                        .characterEncoding("utf-8")
                        .content(patch.toJsonValue().toString()))
                .andExpect(status().isNotFound());

        verify(service).findById(TASK_ID);
        verifyNoMoreInteractions(service);
        verifyNoInteractions(mapper);
        verifyNoInteractions(patchHelper);
    }

    @Test
    void markCompleted_ShouldReturn204_IfCompletedSuccessfully() throws Exception {
        Task task = Task.builder()
                .id(TASK_ID)
                .name("Random Task")
                .dateCreated(LocalDateTime.now())
                .build();

        when(service.findById(TASK_ID)).thenReturn(Optional.ofNullable(task));
        when(service.save(task)).thenReturn(task);

        this.mockMvc.perform(
                put("/tasks/{id}/completed", TASK_ID)
                        .header("Content-Length", 0))
                .andExpect(status().isNoContent());

        verify(service).findById(TASK_ID);
        verify(service).save(task);
        verifyNoMoreInteractions(service);
    }

    @Test
    void markCompleted_ShouldReturn400_WhenAlreadyCompleted() throws Exception {
        Task task = Task.builder()
                .id(TASK_ID)
                .name("Random Task")
                .dateCreated(LocalDateTime.now())
                .dateCompleted(LocalDateTime.now())
                .completed(true)
                .build();

        when(service.findById(TASK_ID)).thenReturn(Optional.ofNullable(task));

        this.mockMvc.perform(
                put("/tasks/{id}/completed", TASK_ID)
                        .header("Content-Length", 0))
                .andExpect(status().isBadRequest());

        verify(service).findById(TASK_ID);
        verifyNoMoreInteractions(service);
    }

    @Test
    void markCompleted_ShouldReturn404_WhenNotFound() throws Exception {
        when(service.findById(TASK_ID)).thenReturn(Optional.empty());

        this.mockMvc.perform(
                put("/tasks/{id}/completed", TASK_ID)
                        .header("Content-Length", 0))
                .andExpect(status().isNotFound());

        verify(service).findById(TASK_ID);
        verifyNoMoreInteractions(service);
    }

    @Test
    void markUncompleted_ShouldReturn204_IfUncompletedSuccessfully() throws Exception {
        Task task = Task.builder()
                .id(TASK_ID)
                .name("Random Task")
                .dateCreated(LocalDateTime.now())
                .completed(true)
                .dateCompleted(LocalDateTime.now())
                .build();

        when(service.findById(TASK_ID)).thenReturn(Optional.ofNullable(task));
        when(service.save(task)).thenReturn(task);

        this.mockMvc.perform(
                delete("/tasks/{id}/completed", TASK_ID)
                        .header("Content-Length", 0))
                .andExpect(status().isNoContent());

        verify(service).findById(TASK_ID);
        verify(service).save(task);
        verifyNoMoreInteractions(service);
    }

    @Test
    void markUncompleted_ShouldReturn400_WhenAlreadyUncompleted() throws Exception {
        Task task = Task.builder()
                .id(TASK_ID)
                .name("Random Task")
                .dateCreated(LocalDateTime.now())
                .build();

        when(service.findById(TASK_ID)).thenReturn(Optional.ofNullable(task));

        this.mockMvc.perform(
                delete("/tasks/{id}/completed", TASK_ID)
                        .header("Content-Length", 0))
                .andExpect(status().isBadRequest());

        verify(service).findById(TASK_ID);
        verifyNoMoreInteractions(service);
    }

    @Test
    void markUncompleted_ShouldReturn404_WhenNotFound() throws Exception {
        when(service.findById(TASK_ID)).thenReturn(Optional.empty());

        this.mockMvc.perform(
                delete("/tasks/{id}/completed", TASK_ID)
                        .header("Content-Length", 0))
                .andExpect(status().isNotFound());

        verify(service).findById(TASK_ID);
        verifyNoMoreInteractions(service);
    }

    private Task asTask(TaskResourceInput resourceInput, Long id) {
        if (resourceInput.getDateCreated() == null) {
            resourceInput.setDateCreated(LocalDateTime.now());
        }

        return Task.builder()
                .id(id)
                .name(resourceInput.getName())
                .project(PROJECT)
                .completed(resourceInput.isCompleted())
                .dateCreated(LocalDateTime.now())
                .dateCompleted(resourceInput.getDateCompleted())
                .build();
    }

    private TaskResourceOutput asTaskResourceOutput(Task task) {
        return TaskResourceOutput.builder()
                .id(task.getId())
                .name(task.getName())
                .project(task.getProject() != null ? task.getProject().getId() : null)
                .completed(task.isCompleted())
                .dateCreated(task.getDateCreated())
                .dateCompleted(task.getDateCompleted())
                .build();
    }
}