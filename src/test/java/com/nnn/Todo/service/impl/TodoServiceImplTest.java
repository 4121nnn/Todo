package com.nnn.Todo.service.impl;


import com.nnn.Todo.controller.payload.TaskPayload;
import com.nnn.Todo.exception.TaskNotFoundException;
import com.nnn.Todo.model.Task;
import com.nnn.Todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.transaction.CannotCreateTransactionException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    MessageSource messageSource;



    @Test
    void findAll_Success_ReturnsTasksSortedByCompetedValue(){
        // given
        Task completedTask = generateTask();
        completedTask.setCompleted(true);
        List<Task> tasks = Arrays.asList(generateTask(), completedTask, generateTask());
        when(todoRepository.findAll()).thenReturn(tasks);

        // when
        var res = todoService.findAll();

        // then
        assertEquals(3, res.size());
        assertEquals(false, res.get(0).getCompleted());
        assertEquals(false, res.get(1).getCompleted());
        assertEquals(true, res.get(2).getCompleted());
    }

    @Test
    void findAll_Success_ReturnsEmptyList(){
        // given
        when(todoRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        var res = todoService.findAll();

        // then
        assertEquals(0, res.size());
    }

    @Test
    void findAll_DatabaseConnectionError_ReturnsInternalServerError() throws Exception {
        // given
        when(todoRepository.findAll()).thenThrow(new CannotCreateTransactionException("Database error occurred. Please try again later."));

        // when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            todoRepository.findAll();
        });

        // then
        assertEquals(exception.getMessage(), "Database error occurred. Please try again later.");
        verify(todoRepository).findAll();
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    void getTaskById_TaskExists_ReturnsOptionalOfTask(){
        // given
        Task task = generateTask();
        when(todoRepository.findById(task.getId())).thenReturn(Optional.of(task));

        // when
        var res = todoRepository.findById(task.getId());

        // then
        assertTrue(res.isPresent());
        assertEquals(task, res.get());
        verify(todoRepository, times(1)).findById(task.getId());
    }

    @Test
    void getTaskById_TaskDoesNotExist_ThrowsTaskNotFoundException(){
        // given
        Long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());
        when(messageSource.getMessage("exception.task.not_found", new Object[]{id}, Locale.getDefault()))
                .thenReturn("Task not found with ID: " + id);

        // when
        assertThrows(TaskNotFoundException.class, () -> {
            todoService.getTaskById(id);
        });

        verify(todoRepository, times(1)).findById(id);
    }

    @Test
    void createTask_Success_ReturnsCreatedTask(){
        // given
        TaskPayload payload = new TaskPayload("task 1");
        Task task = generateTask();
        task.setDescription(payload.description());
        when(todoRepository.save(any(Task.class))).thenReturn(task);

        // when
        var res = todoService.createTask(payload);

        // then
        assertEquals(task, res);
        verify(todoRepository, times(1)).save(any(Task.class));
    }

    @Test
    void markCompleted_Success_ReturnMarkedTask(){
        // given
        Task task = generateTask();
        when(todoRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(todoRepository.save(task)).thenReturn(task);

        // when
        Task res = todoService.markCompleted(task.getId());

        // then
        assertEquals(task.getId(), res.getId());
        assertEquals(task.getDescription(), res.getDescription());
        assertEquals(task.getCreatedDate(), res.getCreatedDate());
        assertTrue(res.getCompleted());
        assertNotNull(res.getCompletedDate());
        verify(todoRepository, times(1)).save(task);

        // mark as not completed
        res = todoService.markCompleted(task.getId());

        assertFalse(res.getCompleted());
        assertNull(res.getCompletedDate());
        verify(todoRepository, times(2)).save(task);
    }

    @Test
    void deleteTask_Success(){
        // give
        long id = 1L;
        doNothing().when(todoRepository).deleteById(id);

        // when
        todoService.deleteTask(id);

        // then
        verify(todoRepository, times(1)).deleteById(id);
    }

    @Test
    void updateTask_UpdatesTask_ReturnsUpdatedTask(){
        // Arrange
        Task taskToUpdate = spy(generateTask());
        Task updatedTask = taskToUpdate;
        taskToUpdate.setDescription("Old description");
        updatedTask.setDescription("New Description");
        when(todoRepository.findById(taskToUpdate.getId())).thenReturn(Optional.of(taskToUpdate));
        when(todoRepository.save(updatedTask)).thenReturn(updatedTask);

        // Act
        Task res = todoService.updateTask(taskToUpdate);

        // Assert
        assertEquals(res, updatedTask);
        verify(taskToUpdate, times(1)).setDescription(updatedTask.getDescription());
        verify(todoRepository, times(1)).save(updatedTask);
    }



    private Task generateTask(){
        Random random = new Random();
        long id = random.nextLong();
        return Task.builder()
                .id(id)
                .description("Task description " + id)
                .createdDate(LocalDateTime.now())
                .build();
    }

}