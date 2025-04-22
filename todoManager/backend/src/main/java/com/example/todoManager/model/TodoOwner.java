package com.example.todoManager.model;


public interface TodoOwner {
    String getUuid();

    Iterable<Todo> getTodos();

}
