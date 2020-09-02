package com.danpopescu.taskmanagement.web.mapper;

import com.danpopescu.taskmanagement.domain.Project;
import com.danpopescu.taskmanagement.web.resource.output.ProjectResourceOutput;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProjectMapper {

    ProjectResourceOutput asOutput(Project project);

    Set<ProjectResourceOutput> asOutput(Set<Project> projects);

}
