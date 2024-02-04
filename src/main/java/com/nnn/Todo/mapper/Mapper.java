package com.nnn.Todo.mapper;

import com.nnn.Todo.dto.TaskDTO;
import com.nnn.Todo.model.Task;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    public static Task toTask(TaskDTO taskDTO){
        return new Task(
            taskDTO.getId(),
            taskDTO.getTask(),
            taskDTO.getCompleted(),
            taskDTO.getCreatedDate(),
            taskDTO.getCompletedDate()
        );
    }
    public static TaskDTO toTaskDTO(Task task){
        return new TaskDTO(
                task.getId(),
                task.getTask(),
                task.getCompleted(),
                task.getCreatedDate(),
                task.getCompletedDate()
        );
    }

    public static List<Task> toTask(List<TaskDTO> list){
        return list.stream()
                .map(Mapper::toTask)
                .collect(Collectors.toList());
    }
    public static List<TaskDTO> toTaskDTO(List<Task> list){
        return list.stream()
                .map(Mapper::toTaskDTO)
                .collect(Collectors.toList());
    }

}
