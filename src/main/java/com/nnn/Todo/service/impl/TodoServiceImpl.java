package com.nnn.Todo.service.impl;

import com.nnn.Todo.controller.payload.TaskPayload;
import com.nnn.Todo.model.Task;
import com.nnn.Todo.exception.TaskNotFoundException;
import com.nnn.Todo.repository.TodoRepository;
import com.nnn.Todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {
    private TodoRepository todoRepo;
    @Override
    public List<Task> findAll() {
        List<Task> tasks =  todoRepo.findAll();
        Collections.sort(tasks, (a, b) ->  Boolean.compare(a.getCompleted(), b.getCompleted()));
        return tasks;
    }

    @Override
    public Task getTaskById(Long id) {
        return todoRepo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
    }

    @Override
    public Task createTask(TaskPayload payload) {
            Task task = new Task();
            task.setDescription(payload.description());
            task.setCompleted(false);
            task.setCreatedDate(LocalDateTime.now());
            return todoRepo.save(task);
    }

    @Override
    public void markCompleted(Long id) {
        Task task = getTaskById(id);
        boolean isCompleted = task.getCompleted();
        task.setCompleted(!isCompleted);

        if(task.getCompleted()){
            task.setCompletedDate( LocalDateTime.now() );
        }else{
            task.setCompletedDate(null);
        }

        todoRepo.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        todoRepo.deleteById(id);
    }

    @Override
    public Task updateTask(Task task) {
        Task taskFromRepo = getTaskById(task.getId());
        taskFromRepo.setDescription(task.getDescription());
        return todoRepo.save(taskFromRepo);
    }


}
