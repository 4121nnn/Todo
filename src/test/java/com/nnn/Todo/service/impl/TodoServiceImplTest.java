package com.nnn.Todo.service.impl;


import com.nnn.Todo.exception.TaskNotFoundException;
import com.nnn.Todo.repository.TodoRepository;
import com.nnn.Todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    void testGetTaskById_ThrowsTaskNotFoundException(){
        Long taskId = 1L;

        // Mocking the repository to return an empty Optional when findById is called
        when(todoRepository.findById(taskId)).thenReturn(Optional.empty());

        // Asserting that the TaskNotFoundException is thrown
        assertThrows(TaskNotFoundException.class, () -> {
            todoService.getTaskById(taskId);
        });

        verify(todoRepository, times(1)).findById(taskId);
    }

}