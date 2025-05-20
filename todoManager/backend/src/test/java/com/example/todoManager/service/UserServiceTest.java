package com.example.todoManager.service;

import com.example.todoManager.exception.UserNotFoundException;
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

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findByUuid("test"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void findByEmail_shouldReturnUser() {
        String mail = "user@user.at";
        User user = User.builder().email(mail).build();

        Mockito.when(userRepository.findByEmail(mail)).thenReturn(Optional.of(user));

        User resultUser = userService.findByEmail(mail);
        assertEquals(user, resultUser);
    }

    @Test
    void findByEmail_InvalidEmail_throwsUserNotFoundException() {
        Mockito.when(userRepository.findByEmail(null)).thenReturn(Optional.empty());

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> {
            userService.findByEmail(null);
        });
        assertEquals("User not found", userNotFoundException.getMessage());

    }

    @Test
    void doesUserExist_whenUserExist_returnTrue() {
        String mail = "user@user.at";
        Mockito.when(userRepository.findByEmail(mail)).thenReturn(Optional.of(new User()));

        Boolean result = userService.doesUserExist(mail);

        assertTrue(result);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(mail);
    }

    @Test
    void doesUserExist_whenUserDoesNotExist_returnFalse() {
        String mail = "nonexistent@email.com";
        Mockito.when(userRepository.findByEmail(mail)).thenReturn(Optional.empty());

        Boolean result = userService.doesUserExist(mail);

        assertFalse(result);
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(mail);
    }
}