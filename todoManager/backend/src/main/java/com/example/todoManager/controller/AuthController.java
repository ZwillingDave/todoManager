package com.example.todoManager.controller;

import com.example.todoManager.dto.auth.LoginRequest;
import com.example.todoManager.dto.auth.AuthResponse;
import com.example.todoManager.dto.auth.PayloadResponse;
import com.example.todoManager.dto.auth.SignupRequest;
import com.example.todoManager.service.AuthService;
import com.example.todoManager.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest) {
        AuthResponse response = authService.signup(signupRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        if (!response.getSuccess()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/decodePayload")
    public ResponseEntity<PayloadResponse> decodePayload(@CookieValue("token") String token) {
        PayloadResponse response = tokenService.decodePayload(token);
        return ResponseEntity.ok(response);
    }

}
