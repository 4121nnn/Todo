package com.nnn.Todo.service;

import com.nnn.Todo.controller.payload.TaskPayload;
import com.nnn.Todo.model.Task;

import java.util.List;


public interface TodoService {

    List<Task> findAll();

    Task getTaskById(Long id);

    Task createTask(TaskPayload payload);

    Task markCompleted(Long id);

    void deleteTask(Long id);


    Task updateTask(Task task);
}
