package com.example.todoManager.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(String message) {
        super(message);
    }
    public TodoNotFoundException() {
        super();
    }
}
