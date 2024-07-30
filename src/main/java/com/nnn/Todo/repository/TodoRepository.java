package com.nnn.Todo.repository;

import com.nnn.Todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TodoRepository extends JpaRepository<Task, Long> {
}
