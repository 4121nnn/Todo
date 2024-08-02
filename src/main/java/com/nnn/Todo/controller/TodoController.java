package com.nnn.Todo.controller;

import com.nnn.Todo.controller.payload.TaskPayload;
import com.nnn.Todo.model.Task;
import com.nnn.Todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/tasks")
@CrossOrigin("*")
public class TodoController {


    @Autowired
    TodoService todoService;


    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        return ResponseEntity.ok(todoService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskPayload payload) throws Exception {
        Task createdTask = todoService.createTask(payload);

        String uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.getId())
                .toUriString();

        return ResponseEntity.created(URI.create(uri)).body(createdTask);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id){
        return ResponseEntity.ok(todoService.getTaskById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> markCompleted(@PathVariable Long id){
        return ResponseEntity.ok().body(todoService.markCompleted(id));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        todoService.deleteTask(id);
    }

    @PutMapping
    public ResponseEntity<Task> updateTask(@Valid @RequestBody Task task){
        Task updatedTask = todoService.updateTask(task);
        return ResponseEntity.ok(task);
    }





}
