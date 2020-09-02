package com.danpopescu.taskmanagement.web.controller;

import com.danpopescu.taskmanagement.domain.Project;
import com.danpopescu.taskmanagement.service.ProjectService;
import com.danpopescu.taskmanagement.web.mapper.ProjectMapper;
import com.danpopescu.taskmanagement.web.resource.output.ProjectResourceOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @MockBean
    ProjectService service;

    @MockBean
    ProjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    List<Project> projectList;

    @BeforeEach
    void setUp() {
        projectList = new ArrayList<>();
        projectList.add(new Project(1L, "First project"));
        projectList.add(new Project(2L, "Second project"));
    }

    @Test
    void findAll_ShouldReturnProjects_IfAnyFound() throws Exception {
        Set<Project> projects = new HashSet<>(projectList);
        Set<ProjectResourceOutput> outputs = asOutput(projects);

        when(service.findAll()).thenReturn(projects);
        when(mapper.asOutput(projects)).thenReturn(outputs);

        this.mockMvc.perform(
                get("/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(outputs)));

        verify(service).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void findAll_ShouldReturnEmptyArray_IfNothingFound() throws Exception {
        Set<Project> projects = new HashSet<>();
        when(service.findAll()).thenReturn(projects);
        when(mapper.asOutput(projects)).thenReturn(new HashSet<>());

        this.mockMvc.perform(
                get("/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));

        verify(service).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void findById_ShouldReturnProject_IfFound() throws Exception {
        Project project = projectList.get(0);
        ProjectResourceOutput output = asOutput(project);
        when(service.findById(any())).thenReturn(Optional.of(project));
        when(mapper.asOutput(project)).thenReturn(output);

        this.mockMvc.perform(
                get("/projects/{id}", anyLong()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(output)));

        verify(service).findById(any());
        verify(mapper).asOutput(project);
        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void findById_ShouldReturn404_IfNotFound() throws Exception {
        when(service.findById(any())).thenReturn(Optional.empty());

        this.mockMvc.perform(
                get("/projects/{id}", anyLong()))
                .andExpect(status().isNotFound());

        verify(service).findById(any());
        verifyNoMoreInteractions(service);
        verifyNoInteractions(mapper);
    }

    private Set<ProjectResourceOutput> asOutput(Set<Project> projects) {
        if (projects == null) {
            return null;
        }
        return projects.stream().map(this::asOutput).collect(Collectors.toSet());
    }

    private ProjectResourceOutput asOutput(Project project) {
        if (project == null) {
            return null;
        }
        return new ProjectResourceOutput(project.getId(), project.getName());
    }
}