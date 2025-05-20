package com.example.todoManager.service;

import com.example.todoManager.dto.auth.AuthResponse;
import com.example.todoManager.dto.auth.LoginRequest;
import com.example.todoManager.dto.auth.SignupRequest;
import com.example.todoManager.exception.SignupRequestException;
import com.example.todoManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
        SignupRequestException signupRequestException = new SignupRequestException();
        if (checkRequestVariables(signupRequest, signupRequestException)){
            throw signupRequestException;
        };
        if(userService.doesUserExist(signupRequest.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .uuid("user-" + new UUIDWrapper().createUUID())
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .password(BCrypt.hashpw(signupRequest.getPassword(), BCrypt.gensalt()))
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

        if(!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
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

    private static Boolean checkRequestVariables(SignupRequest signupRequest, SignupRequestException signupRequestException) {
        if (signupRequest.getName() == null || signupRequest.getName().isEmpty()){
            signupRequestException.addMessage("Non Valid Username");
        }
        if (signupRequest.getEmail() == null || signupRequest.getEmail().isEmpty()){
            signupRequestException.addMessage("Non Valid Email");
        }
        if (signupRequest.getPassword() == null || signupRequest.getPassword().isEmpty()){
            signupRequestException.addMessage("Non Valid Password");
        }
        return signupRequestException.shouldBeThrown();
    }
}
