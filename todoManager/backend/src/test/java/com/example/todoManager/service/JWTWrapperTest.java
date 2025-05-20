package com.example.todoManager.service;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class JWTWrapperTest {
    JWTWrapper jwtWrapper = new JWTWrapper();
    String username = "user";
    String uuid = "uuid";
    Instant expiryDate = Instant.parse("2025-03-14T08:56:23.793842400Z");
    String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoidXVpZCIsInVzZXJuYW1lIjoidXNlciIsImV4cCI6MTc0MTk0MjU4M30.qkm9yGlHoaJ-82ZrY7mhP8-sKt997CG8dMODGqd2hns";


    @Test
    void createJWTToken_valid() {
        ReflectionTestUtils.setField(jwtWrapper, "secret", "my-test-secret-for-java-unittest");
        String token = jwtWrapper.createJWTToken(uuid, username, expiryDate);
        assertEquals(validToken, token);
    }

    @Test
    void createJWTToken_invalid(){
        ReflectionTestUtils.setField(jwtWrapper, "secret", "my-test-secret-for-java-unittest");
        String token = jwtWrapper.createJWTToken(null, null, null);
        assertNotEquals(validToken, token);
    }

    @Test
    void createJWTToken_differentDate() {
        ReflectionTestUtils.setField(jwtWrapper, "secret", "my-test-secret-for-java-unittest");
        String token = jwtWrapper.createJWTToken(uuid, username, Instant.now());
        assertNotEquals(validToken, token);
    }
}