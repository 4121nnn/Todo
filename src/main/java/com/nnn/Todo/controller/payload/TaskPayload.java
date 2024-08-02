package com.nnn.Todo.controller.payload;

import jakarta.validation.constraints.NotBlank;

public record TaskPayload (
        @NotBlank(message = "{task.description.blank}")
        String description){
}
