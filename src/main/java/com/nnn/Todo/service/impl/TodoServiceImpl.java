package com.nnn.Todo.service.impl;

import com.nnn.Todo.dto.TaskDTO;
import com.nnn.Todo.exception.ResourceNotFoundException;
import com.nnn.Todo.mapper.Mapper;
import com.nnn.Todo.model.Task;
import com.nnn.Todo.repository.TodoRepository;
import com.nnn.Todo.service.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {
    TodoRepository todoRepo;
    @Override
    public List<TaskDTO> findAll() {
        return todoRepo.findAll().stream()
                .map(Mapper::toTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        return Mapper.toTaskDTO(todoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task doesn't exists by id: " + id)));
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        taskDTO.prePersist();
        Task createdTask = todoRepo.save(Mapper.toTask(taskDTO));
        return Mapper.toTaskDTO(createdTask);

    }

    @Override
    public void markCompleted(Long id) {
        Task task = todoRepo.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Task doesn't exists by id: " + id) );
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
    public TaskDTO updateTask(TaskDTO taskDTO) {
        Task task = todoRepo.findById(taskDTO.getId())
                .orElseThrow( () -> new ResourceNotFoundException("Task doesn't exists by id: " + taskDTO.getId()) );
        task.setTask(taskDTO.getTask());
        Task updatedTask = todoRepo.save(task);
        return Mapper.toTaskDTO(updatedTask);
    }


}
