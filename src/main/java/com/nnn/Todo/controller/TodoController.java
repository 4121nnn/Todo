package com.nnn.Todo.controller;

import com.nnn.Todo.model.Task;
import com.nnn.Todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tasks")
@CrossOrigin("*")
public class TodoController {


    @Autowired
    TodoService todoService;


    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> todoDTOs = todoService.findAll();

        return ResponseEntity.ok(todoDTOs);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        Task createdTask = todoService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id){
        Task task = todoService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping("{id}")
    public void markCompleted(@PathVariable Long id){
        todoService.markCompleted(id);
    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable Long id){
        todoService.deleteTask(id);
    }
    @PostMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task task){
        Task updatedTask = todoService.updateTask(task);
        return ResponseEntity.ok(task);
    }





}
