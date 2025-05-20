package com.example.todoManager.controller;

import com.example.todoManager.dto.auth.AuthResponse;
import com.example.todoManager.dto.auth.LoginRequest;
import com.example.todoManager.dto.auth.SignupRequest;
import com.example.todoManager.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String signupUrl = "/api/auth/signup";
    private final String loginUrl = "/api/auth/login";

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setup() {
        signupRequest = SignupRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password123")
                .build();

        loginRequest = LoginRequest.builder()
                .email("john@example.com")
                .password("password123")
                .build();
    }

    @Test
    void signup_returnsOkResponse() throws Exception {
        AuthResponse mockResponse = AuthResponse.builder()
                .success(true)
                .message("Signed up successfully")
                .token("mock.token")
                .path("/dashboard")
                .build();

        Mockito.when(authService.signup(any(SignupRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post(signupUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token").value("mock.token"));
    }

    @Test
    void login_successful_returnsOkResponse() throws Exception {
        AuthResponse mockResponse = AuthResponse.builder()
                .success(true)
                .message("Login successful")
                .token("mock.jwt.token")
                .path("/dashboard")
                .build();

        Mockito.when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token").value("mock.jwt.token"));
    }

    @Test
    void login_unsuccessful_returnsBadRequest() throws Exception {
        AuthResponse mockResponse = AuthResponse.builder()
                .success(false)
                .message("Invalid credentials")
                .build();

        Mockito.when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post(loginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }
}