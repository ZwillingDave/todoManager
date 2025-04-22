package com.example.todoManager.service;

import com.example.todoManager.model.Todo;
import com.example.todoManager.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;


    @BeforeEach
    void setUp() {
        todoService = new TodoService(todoRepository);
    }

    @Test
    void save_shouldCallRepositorySave() {
        Todo todo = Todo.builder().build();

        todoService.save(todo);

        Mockito.verify(todoRepository, Mockito.times(1)).save(todo);
    }

    @Test
    void findById_shouldReturnTodoIfExists() {
        Todo todo = Todo.builder().uuid("uuid").build();

        Mockito.when(todoRepository.findById("uuid")).thenReturn(Optional.of(todo));
        todoService.findById("uuid");

        Mockito.verify(todoRepository, Mockito.times(1)).findById("uuid");
    }

    @Test
    void findById_shouldThrowExceptionIfTodoNotFound() {
        Mockito.when(todoRepository.findById("test")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> todoService.findById("test"));

        assertEquals("Todo not found", exception.getMessage());
    }
}