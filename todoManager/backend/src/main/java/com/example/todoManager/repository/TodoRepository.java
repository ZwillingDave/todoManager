package com.example.todoManager.repository;

import com.example.todoManager.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TodoRepository extends JpaRepository<Todo, String> , JpaSpecificationExecutor<Todo> {

    Iterable<Todo> findByTodoOwnerUuid(String ownerId);
}