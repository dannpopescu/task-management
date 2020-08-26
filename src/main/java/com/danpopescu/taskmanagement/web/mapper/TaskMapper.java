package com.danpopescu.taskmanagement.web.mapper;

import com.danpopescu.taskmanagement.domain.Task;
import com.danpopescu.taskmanagement.web.resource.input.TaskResourceInput;
import com.danpopescu.taskmanagement.web.resource.input.TaskResourceOutput;
import org.mapstruct.Mapper;

@Mapper(uses = IdMapper.class)
public interface TaskMapper {

    Task asTask(TaskResourceInput resourceInput);

    TaskResourceOutput asOutput(Task task);
}
