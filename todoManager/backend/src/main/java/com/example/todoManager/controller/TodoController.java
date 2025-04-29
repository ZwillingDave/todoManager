package com.example.todoManager.controller;

import com.example.todoManager.model.Todo;
import com.example.todoManager.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }


    @GetMapping("/{todoId}")
    public ResponseEntity<Todo> getTodoById(@PathVariable String todoId) {
        try {
            Todo todo = todoService.findById(todoId);
            return ResponseEntity.ok(todo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
