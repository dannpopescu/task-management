package com.danpopescu.taskmanagement.web.controller;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.service.TaskService;
import com.danpopescu.taskmanagement.web.exception.TaskNotFoundException;
import com.danpopescu.taskmanagement.web.resource.TaskDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @MockBean
    TaskService taskService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void findAll_WhenFound_ShouldReturnTasks() throws Exception {
        Task first = Task.builder()
                .id(1L)
                .name("First Task")
                .build();
        Task second = Task.builder()
                .id(2L)
                .name("Second Task")
                .build();

        when(taskService.findAll()).thenReturn(List.of(first, second));

        this.mockMvc.perform(
                get("/tasks")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("First Task")))
                .andExpect(jsonPath("$[0].done", is(false)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Second Task")))
                .andExpect(jsonPath("$[1].done", is(false)));

        verify(taskService).findAll();
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void findAll_WhenNoTasks_ShouldReturnEmptyArray() throws Exception {
        when(taskService.findAll()).thenReturn(new ArrayList<>());

        this.mockMvc.perform(
                get("/tasks")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(0)));

        verify(taskService).findAll();
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void findById_WhenFound_ShouldReturnTask() throws Exception {
        Task task = Task.builder()
                .id(1L)
                .name("Lorem ipsum")
                .build();

        when(taskService.findById(task.getId())).thenReturn(task);

        this.mockMvc.perform(
                get("/tasks/{id}", task.getId())
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Lorem ipsum")))
                .andExpect(jsonPath("$.done", is(false)));

        verify(taskService).findById(task.getId());
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void findById_WhenNotFound_ShouldReturn404() throws Exception {
        when(taskService.findById(any())).thenThrow(TaskNotFoundException.class);

        this.mockMvc.perform(
                get("/tasks/{id}", 1L)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(taskService).findById(1L);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void create_WhenCreated_ShouldReturn201() throws Exception {
        TaskDTO taskDTO = TaskDTO.builder().title("Lorem ipsum").build();

        Task added = Task.builder()
                .id(1L)
                .name(taskDTO.getTitle())
                .completed(taskDTO.isDone())
                .build();

        when(taskService.save(taskDTO)).thenReturn(added);

        this.mockMvc.perform(
                post("/tasks")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(added.getName())))
                .andExpect(jsonPath("$.done", is(added.isCompleted())));

        verify(taskService).save(taskDTO);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void deleteTask_ShouldReturn200_WhenDeleted() throws Exception {
        doNothing().when(taskService).deleteById(1L);

        this.mockMvc.perform(
                delete("/tasks/{id}", 1L))
                .andExpect(status().isOk());

        verify(taskService).deleteById(1L);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void deleteTask_ShouldReturn404_WhenTaskNotFound() throws Exception {
        doThrow(TaskNotFoundException.class).when(taskService).deleteById(1L);

        this.mockMvc.perform(
                delete("/tasks/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(taskService).deleteById(1L);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void update_ShouldReturn200_WhenTaskUpdated() throws Exception {
        TaskDTO taskDTO = TaskDTO.builder()
                .id(1L)
                .title("Lorem ipsum")
                .build();

        Task added = Task.builder()
                .id(1L)
                .name(taskDTO.getTitle())
                .completed(taskDTO.isDone())
                .build();

        when(taskService.save(taskDTO)).thenReturn(added);
        when(taskService.existsById(1L)).thenReturn(true);

        this.mockMvc.perform(
                put("/tasks/{id}", taskDTO.getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(added.getName())))
                .andExpect(jsonPath("$.done", is(added.isCompleted())));

        verify(taskService).save(taskDTO);
        verify(taskService).existsById(1L);
        verifyNoMoreInteractions(taskService);
    }

    @Test
    void update_ShouldReturn404_WhenTaskNotFound() throws Exception {
        TaskDTO taskDTO = TaskDTO.builder()
                .id(1L)
                .title("Lorem ipsum")
                .build();

        when(taskService.existsById(1L)).thenReturn(false);

        this.mockMvc.perform(
                put("/tasks/{id}", taskDTO.getId())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isNotFound());

        verify(taskService).existsById(1L);
        verifyNoMoreInteractions(taskService);
    }
}