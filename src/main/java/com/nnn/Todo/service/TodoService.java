package com.nnn.Todo.service;

import com.nnn.Todo.dto.TaskDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TodoService {

    List<TaskDTO> findAll();

    TaskDTO getTaskById(Long id);

    TaskDTO createTask(TaskDTO taskDTO);

    void markCompleted(Long id);

    void deleteTask(Long id);


    TaskDTO updateTask(TaskDTO task);
}
