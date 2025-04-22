package com.example.todoManager.service;

import com.example.todoManager.model.TodoOwner;
import com.example.todoManager.model.User;
import com.example.todoManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public TodoOwner findByUuid(String uuid) {
        Optional<User> user = userRepository.findById(uuid);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return user.get();
    }
}
