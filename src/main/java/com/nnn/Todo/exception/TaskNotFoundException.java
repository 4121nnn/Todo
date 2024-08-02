package com.nnn.Todo.exception;

public class TaskNotFoundException  extends RuntimeException{
    public TaskNotFoundException(String message){
        super(message);
    }
}
