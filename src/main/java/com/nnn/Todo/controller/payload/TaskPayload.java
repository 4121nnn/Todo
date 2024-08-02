package com.nnn.Todo.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskPayload (
        @NotBlank(message = "Description must not be blank")
        String description){
}
