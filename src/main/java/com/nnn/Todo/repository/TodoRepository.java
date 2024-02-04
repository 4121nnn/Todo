package com.nnn.Todo.repository;

import com.nnn.Todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface TodoRepository extends JpaRepository<Task, Long> {
}
