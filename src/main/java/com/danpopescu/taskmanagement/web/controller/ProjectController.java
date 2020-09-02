package com.danpopescu.taskmanagement.web.controller;

import com.danpopescu.taskmanagement.domain.Project;
import com.danpopescu.taskmanagement.service.ProjectService;
import com.danpopescu.taskmanagement.web.exception.ResourceNotFoundException;
import com.danpopescu.taskmanagement.web.mapper.ProjectMapper;
import com.danpopescu.taskmanagement.web.resource.output.ProjectResourceOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;
    private final ProjectMapper mapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<ProjectResourceOutput> findAll() {
        return mapper.asOutput(service.findAll());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProjectResourceOutput findById(@PathVariable Long id) {
        Project project = service.findById(id).orElseThrow(ResourceNotFoundException::new);
        return mapper.asOutput(project);
    }
}
