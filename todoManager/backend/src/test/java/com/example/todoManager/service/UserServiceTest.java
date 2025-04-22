package com.example.todoManager.service;

import com.example.todoManager.model.User;
import com.example.todoManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void save_shouldCallRepositorySave() {
        User user = User.builder().uuid("uuid").build();
        userService.save(user);

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        User user = User.builder().build();
        User user2 = User.builder().build();

        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        Iterable<User> result = userService.getAllUsers();

        assertNotNull(result);
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void findByUuid_shouldReturnUserIfExists() {
        User user = User.builder().uuid("uuid").build();

        Mockito.when(userRepository.findById("uuid")).thenReturn(Optional.of(user));

        var result = userService.findByUuid("uuid");

        assertEquals(user, result);
        Mockito.verify(userRepository, Mockito.times(1)).findById("uuid");
    }

    @Test
    void findById_shouldThrowExceptionIfUserNotFound() {
        Mockito.when(userRepository.findById("test")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.findByUuid("test"));

        assertEquals("User not found", exception.getMessage());
    }
}