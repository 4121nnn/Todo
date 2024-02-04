package com.nnn.Todo.dto;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDTO {
    private Long id;
    private String task;
    private Boolean completed = false;
    private LocalDateTime createdDate;
    private LocalDateTime completedDate;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
    }

}
