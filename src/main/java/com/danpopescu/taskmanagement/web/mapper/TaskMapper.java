package com.danpopescu.taskmanagement.web.mapper;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.web.resource.input.TaskResourceInput;
import com.danpopescu.taskmanagement.web.resource.output.TaskResourceOutput;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.Set;

@Mapper(uses = IdMapper.class, componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = LocalDateTime.class)
public interface TaskMapper {

    @Mapping(target = "dateCreated", source = "dateCreated", defaultExpression = "java(LocalDateTime.now())")
    Task asTask(TaskResourceInput resourceInput);

    Task updateTask(@MappingTarget Task task, TaskResourceInput resourceInput);

    TaskResourceOutput asOutput(Task task);

    Set<TaskResourceOutput> asOutput(Set<Task> tasks);
}
