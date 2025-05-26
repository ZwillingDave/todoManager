package com.example.todoManager.service;


import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.todoManager.dto.auth.PayloadResponse;
import com.example.todoManager.exception.JWTValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    TokenService tokenService;

    @Mock
    JWTWrapper jwtWrapper;
    String uuid = "uuid";
    String email = "user@email.at";
    Instant expiryDate = Instant.parse("2025-03-14T08:56:23.793842400Z");
    String validToken = "token";


    @BeforeEach
    void setUp() {
        tokenService = new TokenService("my-test-secret-for-java-unittest", jwtWrapper);
    }

    @Test
    void createToken_valid() {
        Mockito.when(jwtWrapper.createJWTToken(uuid, email, expiryDate)).thenReturn(validToken);
        String result = tokenService.createToken(uuid, email, expiryDate);
        assertEquals(validToken, result);
    }

    @Test
    void createToken_invalidParameters() {
        List<String> messages = List.of("Expiry date not valid when creating new JWT.", "Username not valid when creating new JWT.");
        JWTValidationException exception = assertThrows(JWTValidationException.class, () -> tokenService.createToken(null, null, null));
        assertTrue(exception.getMessages().containsAll(messages));

        messages = List.of("Username not valid when creating new JWT.");
        JWTValidationException usernameNullException = assertThrows(JWTValidationException.class, () -> tokenService.createToken(null, null, expiryDate));
        assertTrue(usernameNullException.getMessages().containsAll(messages));

        messages = List.of("Expiry date not valid when creating new JWT.");
        JWTValidationException expiryDateNullException = assertThrows(JWTValidationException.class, () -> tokenService.createToken(uuid, email, null));
        assertTrue(expiryDateNullException.getMessages().containsAll(messages));
    }

    @Test
    void createToken_shouldThrowValidationException() {
        Mockito.when(jwtWrapper.createJWTToken(uuid, email, expiryDate)).thenThrow(new JWTCreationException("Bla", null));
        JWTValidationException jwtValidationException = assertThrows(JWTValidationException.class, () -> {
            tokenService.createToken(uuid, email, expiryDate);
        });
        assertTrue(jwtValidationException.containsMessage("Bla"));
    }

    @Test
    void validateTokenDoesNotThrowException() {
        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjU3NDE5NDI1ODN9.Y5qGWfJytOsFajWT5pswHgyXdtNjWOrCmNbDnLiF9F0";
        tokenService.validateToken(validToken);
    }

    @Test
    void validateTokenThrowsExceptionWithInvalidToken() {
        assertThrows(JWTVerificationException.class, () -> {
            tokenService.validateToken("test");
        });
    }

    @Test
    void validateToken_failsWithExpiredDate() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjE3NDE5NDI1ODN9.q7OZfffwwH_U0k2j6oI1__mTgOw2R6JxNzYTllfQK_Q";
        assertThrows(JWTVerificationException.class, () -> {
            tokenService.validateToken(invalidToken);
        });
    }


    @Test
    void validateToken_failsWithIncorrectSignature() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjU3NDE5NDI1ODN9.qoXXnVlkQh4suvSL1EE-C1d7tzhxfUCc5obOn3uj-bE";
        assertThrows(JWTVerificationException.class, () -> {
            tokenService.validateToken(invalidToken);
        });
    }



    @Test
    void testdecodePayload() {
        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjU3NDE5NDI1ODN9.Y5qGWfJytOsFajWT5pswHgyXdtNjWOrCmNbDnLiF9F0";
        PayloadResponse jwt = tokenService.decodePayload(validToken);
        assertEquals("user", jwt.getUsername());
        assertEquals("2151-12-15T16:03:03Z", jwt.getExpiryDate());
    }
}