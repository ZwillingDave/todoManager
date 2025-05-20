package com.example.todoManager.service;

import com.example.todoManager.dto.auth.AuthResponse;
import com.example.todoManager.dto.auth.SignupRequest;
import com.example.todoManager.exception.SignupRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;


    @BeforeEach
    void setUp() {
    }

    @Test
    void signupShouldReturnAuthResponse() {
        SignupRequest signupRequest = SignupRequest.builder()
                .name("user")
                .email("user@user.at")
                .password("password")
                .build();

        AuthResponse response = authService.signup(signupRequest);
        assertNotNull(response);
        assertEquals(AuthResponse.class, response.getClass());
    }

    @ParameterizedTest
    @MethodSource("invalidSignupRequests")
    void signupShouldReturnExceptionWhenMinOneSignupRequestVariableIsNull(String username, String email, String password, List<String> messages) {
        SignupRequest signupRequest = SignupRequest.builder()
                .name(username)
                .email(email)
                .password(password)
                .build();

        SignupRequestException signupRequestException = assertThrows(SignupRequestException.class, () -> {
            authService.signup(signupRequest);
        });
        assertTrue(signupRequestException.getMessages().size() == messages.size() && signupRequestException.getMessages().containsAll(messages));


    }
    private static Stream<Arguments> invalidSignupRequests() {
        return Stream.of(
                Arguments.of(null, "test", "null", List.of("Non Valid Username")),
                Arguments.of("test", null, "test", List.of("Non Valid Email")),
                Arguments.of("test", "test", null, List.of("Non Valid Password")),
                Arguments.of(null, null, "null", List.of("Non Valid Username", "Non Valid Email")),
                Arguments.of("null", null, null, List.of("Non Valid Email", "Non Valid Password")),
                Arguments.of(null, "test", null, List.of("Non Valid Username", "Non Valid Password")),
                Arguments.of(null, null, null, List.of("Non Valid Username", "Non Valid Email", "Non Valid Password"))

        );
    }
}