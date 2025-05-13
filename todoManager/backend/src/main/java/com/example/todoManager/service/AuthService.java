package com.example.todoManager.service;

import com.example.todoManager.dto.auth.AuthResponse;
import com.example.todoManager.dto.auth.LoginRequest;
import com.example.todoManager.dto.auth.SignupRequest;
import com.example.todoManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public AuthService(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public AuthResponse signup(SignupRequest signupRequest) {
        if(userService.doesUserExist(signupRequest.getEmail())) {
            throw new RuntimeException("User already exists");
        }
        User user = User.builder()
                .uuid("user-" + new UUIDWrapper().createUUID())
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword()) // TODO: Hash password
                .build();

        userService.save(user);

        return AuthResponse.builder()
                .message("Successfully signed up")
                .path("/login")
                .success(true)
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        if(!userService.doesUserExist(loginRequest.getEmail())) {
            return AuthResponse.builder()
                    .message("User does not exist")
                    .success(false)
                    .build();
        }
        User user = userService.findByEmail(loginRequest.getEmail());

        if(!user.getPassword().equals(loginRequest.getPassword())) {
            return AuthResponse.builder()
                    .message("Incorrect password")
                    .success(false)
                    .build();
        }

        return AuthResponse.builder()
                .token(tokenService.createToken(user.getUuid(), user.getName(), Instant.now().plusSeconds(3600)))
                .message("Successfully logged in")
                .path("/dashboard")
                .success(true)
                .build();
    }
}
