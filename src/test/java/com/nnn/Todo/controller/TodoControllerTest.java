package com.nnn.Todo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nnn.Todo.config.GlobalExceptionHandler;
import com.nnn.Todo.controller.payload.TaskPayload;
import com.nnn.Todo.exception.TaskNotFoundException;
import com.nnn.Todo.model.Task;
import com.nnn.Todo.service.TodoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.CannotCreateTransactionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;

    @InjectMocks
    private TodoController todoController;

    @Mock
    private TodoService todoService;

    @BeforeAll
    public static void setupBeforeAll(){
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(todoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }



    @Test
    void getAllTasks_Success_ReturnsAllTasks() throws Exception {
        // given
        LocalDateTime createdDate = LocalDateTime.now();
        List<Task> tasks = List.of(new Task(1L, "taks 1", false, createdDate, null),
                new Task(2L, "taks 2", false, createdDate, null));
        when(todoService.findAll()).thenReturn(tasks);

        // when
        mockMvc.perform(get("/api/tasks").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(tasks)));


        // then
        verify(todoService).findAll();
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void getAllTasks_DatabaseConnectionError_ReturnsInternalServerError() throws Exception {
        // given
        when(todoService.findAll()).thenThrow(new CannotCreateTransactionException("Database error occurred. Please try again later."));

        // when
        mockMvc.perform(get("/api/tasks").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("Database error occurred. Please try again later."));

        // then
        verify(todoService).findAll();
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void createTask_Success_ReturnsCreatedTask() throws Exception {
        // given
        TaskPayload payload = new TaskPayload("task1");
        Task task = generateTask();
        task.setDescription(payload.description());

        String expectedUri = "http://localhost/api/tasks/" + task.getId();
        when(todoService.createTask(payload)).thenReturn(task);

        // when
        mockMvc.perform(post("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", expectedUri))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(task)));

        // then
        verify(todoService).createTask(payload);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void createTask_RequestNotValid_ReturnsBadRequest() throws Exception {
        // given
        TaskPayload payload = new TaskPayload("");

        // when
        mockMvc.perform(post("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Description must not be blank"));

        // then
        verifyNoInteractions(todoService);
    }


    @Test
    void getTaskById_TaskExists_ReturnsTask() throws Exception {
        // arrange
        Task task = generateTask();
        when(todoService.getTaskById(task.getId())).thenReturn(task);

        // act and assert
        mockMvc.perform(get("/api/tasks/{id}", task.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(task)));

        // verify
        verify(todoService, times(1)).getTaskById(task.getId());
    }

    @Test
    void getTaskById_TaskDoesNotExists_ReturnNotFound() throws Exception{
        // given
        Long taskId = 1L;
        when(todoService.getTaskById(taskId)).thenThrow(new TaskNotFoundException("Task with ID " + taskId + " not found"));

        // when
        mockMvc.perform(get("/api/tasks/{id}", taskId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task with ID 1 not found"))
                .andReturn();

        // then
        verify(todoService, times(1)).getTaskById(taskId);
    }
    @Test
    void markCompleted_MarksTaskCompeted_ReturnsOk() throws Exception {
        // given
        long id = 1L;
        doNothing().when(todoService).markCompleted(id);

        // when
        mockMvc.perform(patch("/api/tasks/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        verify(todoService).markCompleted(id);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void markCompleted_TaskDoesNotExist_ReturnsNotFound() throws Exception {
        // given
        long id = 1L;
        doThrow(new TaskNotFoundException("Task with ID " + id + " not found")).when(todoService).markCompleted(id);

        // when
        mockMvc.perform(patch("/api/tasks/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task with ID " + id + " not found"));

        // then
        verify(todoService).markCompleted(id);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void deleteTask_ReturnsOk() throws Exception {
        // give
        long id = 1L;
        doNothing().when(todoService).deleteTask(id);

        // when
        mockMvc.perform(delete("/api/tasks/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        verify(todoService).deleteTask(id);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void updateTask_UpdatesTask_ReturnsUpdatedTask() throws Exception {
        // given
        Task task = generateTask();
        when(todoService.updateTask(task)).thenReturn(task);

        // when
        mockMvc.perform(put("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(task)));

        // then
        verify(todoService).updateTask(task);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    void updateTask_TaskDoesNotExists_ReturnsNotFound() throws Exception {
        // given
        Task task = generateTask();
        when(todoService.updateTask(task)).thenThrow(new TaskNotFoundException("Task with ID " + task.getId() + " not found"));

        // when
        mockMvc.perform(put("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task with ID " + task.getId() + " not found"));

        // then
        verify(todoService).updateTask(task);
        verifyNoMoreInteractions(todoService);
    }



    @ParameterizedTest
    @CsvSource({
            "'', Description cannot be blank",
            "'null', Description cannot be blank",
            "'very long description very long description very long description', Description length must not exceed 50 characters"
    })
    void updateTask_DescriptionIsInvalid_ReturnsBadRequest(String description, String expectedError) throws Exception {
        // Arrange
        Task task = generateTask();
        if ("null".equals(description)) {
            task.setDescription(null);
        }else{
            task.setDescription(description);
        }
        // Act & Assert
        mockMvc.perform(put("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").value(expectedError));

        // Verify
        verifyNoInteractions(todoService);
    }



    private String parseDate(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
    private Task generateTask(){
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return Task.builder()
                .id(1L)
                .description("task 1")
                .completed(false)
                .createdDate(now)
                .build();
    }

}