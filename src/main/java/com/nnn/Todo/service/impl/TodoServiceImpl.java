package com.nnn.Todo.service.impl;

import com.nnn.Todo.model.Task;
import com.nnn.Todo.repository.TodoRepository;
import com.nnn.Todo.service.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {
    TodoRepository todoRepo;
    @Override
    public List<Task> findAll() {
        List<Task> tasks =  todoRepo.findAll();
        Collections.sort(tasks, (a, b) ->  Boolean.compare(a.getCompleted(), b.getCompleted()));
        return tasks;
    }

    @Override
    public Task getTaskById(Long id) {
        return todoRepo.findById(id).orElseThrow();
    }

    @Override
    public Task createTask(Task task) {
        task.setCompleted(false);
        task.setCreatedDate(LocalDateTime.now());
        return todoRepo.save(task);
    }

    @Override
    public void markCompleted(Long id) {
        Task task = todoRepo.findById(id)
                .orElseThrow( () -> new RuntimeException("Task doesn't exists by id: " + id) );
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
        Task taskFromRepo = todoRepo.findById(task.getId())
                .orElseThrow( () -> new RuntimeException("Task doesn't exists by id: " + task.getId()) );
        taskFromRepo.setTask(task.getTask());
        return todoRepo.save(taskFromRepo);

    }


}
