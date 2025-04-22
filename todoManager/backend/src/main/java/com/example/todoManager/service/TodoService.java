package com.example.todoManager.service;

import com.example.todoManager.model.Todo;
import com.example.todoManager.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TodoService {
//    private final TodoOwnerResolverRegistry resolverRegistry;
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void save(Todo todo) {
        todoRepository.save(todo);
    }

    public Todo findById(String todoId) {
        Optional<Todo> todo = todoRepository.findById(todoId);
        if (todo.isEmpty()) {
            throw new RuntimeException("Todo not found");
        }
        return todo.get();
    }
}
