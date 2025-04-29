package com.example.todoManager.service;

import com.example.todoManager.dto.auth.AuthResponse;
import com.example.todoManager.dto.auth.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void signupShouldReturnAuthResponse() {
        AuthResponse response = authService.signup(signupRequest);
        assertNotNull(response);
        assertEquals(AuthResponse.class, response.getClass());
    }

    @Test
    void test() {

    }
}