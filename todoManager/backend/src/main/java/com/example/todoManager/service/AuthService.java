package com.example.todoManager.service;

import com.example.todoManager.dto.auth.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public AuthResponse signup() {
        return AuthResponse.builder().build();
    }
}
