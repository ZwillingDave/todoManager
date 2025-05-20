package com.example.todoManager.service;

import com.example.todoManager.dto.auth.AuthResponse;
import com.example.todoManager.dto.auth.LoginRequest;
import com.example.todoManager.dto.auth.SignupRequest;
import com.example.todoManager.exception.SignupRequestException;
import com.example.todoManager.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;


    private final String email = "user@example.com";
    private final String plainPassword = "password";
    private final String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    private final String uuid = "1234-5678";
    private final String username = "testuser";
    private final String token = "mocked.jwt.token";

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

    @Test
    void signupShouldThrowExceptionWhenUserExists() {
        Mockito.when(userService.doesUserExist(email)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.signup(SignupRequest.builder().name(username).email(email).password(plainPassword).build()));

        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void login_validCredentials_returnsSuccessResponse() {
        LoginRequest request = new LoginRequest(email, plainPassword);

        User user = User.builder()
                .email(email)
                .password(hashedPassword)
                .uuid(uuid)
                .name(username)
                .build();

        Mockito.when(userService.doesUserExist(email)).thenReturn(true);
        Mockito.when(userService.findByEmail(email)).thenReturn(user);
        Mockito.when(tokenService.createToken(Mockito.eq(uuid), Mockito.eq(username), Mockito.any())).thenReturn(token);

        AuthResponse response = authService.login(request);

        assertTrue(response.getSuccess());
        assertEquals("Successfully logged in", response.getMessage());
        assertEquals("/dashboard", response.getPath());
        assertEquals(token, response.getToken());
    }

    @Test
    void login_userDoesNotExist_returnsFailureResponse() {
        LoginRequest request = new LoginRequest(email, plainPassword);

        Mockito.when(userService.doesUserExist(email)).thenReturn(false);

        AuthResponse response = authService.login(request);

        assertFalse(response.getSuccess());
        assertEquals("User does not exist", response.getMessage());
    }

    @Test
    void login_wrongPassword_returnsFailureResponse() {
        LoginRequest request = new LoginRequest(email, "wrongpassword");

        User user = User.builder()
                .email(email)
                .password(hashedPassword)
                .build();

        Mockito.when(userService.doesUserExist(email)).thenReturn(true);
        Mockito.when(userService.findByEmail(email)).thenReturn(user);

        AuthResponse response = authService.login(request);

        assertFalse(response.getSuccess());
        assertEquals("Incorrect password", response.getMessage());
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
                Arguments.of("", "test", "null", List.of("Non Valid Username")),
                Arguments.of("test", "", "test", List.of("Non Valid Email")),
                Arguments.of("test", "test", "", List.of("Non Valid Password")),
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