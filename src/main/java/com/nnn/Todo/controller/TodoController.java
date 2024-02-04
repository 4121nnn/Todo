package com.nnn.Todo.controller;

import com.nnn.Todo.dto.TaskDTO;
import com.nnn.Todo.service.TodoService;
import jakarta.persistence.Entity;
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
    public ResponseEntity<List<TaskDTO>> getAllTasks(){
        List<TaskDTO> todoDTOs = todoService.findAll();

        return ResponseEntity.ok(todoDTOs);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO){
        TaskDTO createdTask = todoService.createTask(taskDTO);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable Long id){
        TaskDTO taskDTO = todoService.getTaskById(id);
        return ResponseEntity.ok(taskDTO);
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
    public ResponseEntity<TaskDTO> updateTask(@RequestBody TaskDTO task){
        TaskDTO updatedTask = todoService.updateTask(task);
        return ResponseEntity.ok(task);
    }





}
