package com.example.todoManager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class JWTWrapperTest {
    JWTWrapper jwtWrapper = new JWTWrapper();
    String username = "user";
    String uuid = "uuid";
    Instant expiryDate = Instant.parse("2025-03-14T08:56:23.793842400Z");
    String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoidXVpZCIsImVtYWlsIjoidXNlciIsImV4cCI6MTc0MTk0MjU4M30.vJzjtv1yITx1SKp6pcY_e0R6FPHOx4UVvkqpD2Niwkc";

    @Test
    void createJWTToken_valid() {
        String token = jwtWrapper.createJWTToken(uuid, username, expiryDate);
        assertEquals(validToken, token);
    }

    @Test
    void createJWTToken_invalid(){
        String token = jwtWrapper.createJWTToken(null, null, null);
        assertNotEquals(validToken, token);
    }

    @Test
    void createJWTToken_differentDate() {
        String token = jwtWrapper.createJWTToken(uuid, username, Instant.now());
        assertNotEquals(validToken, token);
    }
}