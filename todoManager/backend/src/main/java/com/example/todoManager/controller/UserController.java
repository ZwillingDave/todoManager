package com.example.todoManager.controller;

import com.example.todoManager.dto.GroupDTO;
import com.example.todoManager.dto.TodoDTO;
import com.example.todoManager.dto.UserDTO;
import com.example.todoManager.model.TodoOwner;
import com.example.todoManager.model.User;
import com.example.todoManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        try {
            Iterable<User> allUsers = userService.getAllUsers();
            List<UserDTO> userDTOs = new ArrayList<>();


            allUsers.forEach(user -> userDTOs.add(UserDTO.builder()
                    .uuid(user.getUuid())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build()));


            return ResponseEntity.ok(userDTOs);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GetMapping("/{uuid}/groups")
    public ResponseEntity<List<GroupDTO>> getUserGroups(@PathVariable String uuid) {
        try {
            TodoOwner todoOwner = userService.findByUuid(uuid);

            if (!(todoOwner instanceof User user)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
            List<GroupDTO> groupDTOs = user.getGroups().stream()
                    .map(group -> GroupDTO.builder()
                            .uuid(group.getUuid())
                            .name(group.getName())
                            .build())
                    .toList();

            return ResponseEntity.ok(groupDTOs);


        } catch (RuntimeException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GetMapping("/{uuid}/todos")
    public ResponseEntity<List<TodoDTO>> getUserTodos(@PathVariable String uuid) {
        try {
            TodoOwner todoOwner = userService.findByUuid(uuid);

            if (!(todoOwner instanceof User user)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            List<TodoDTO> todoDTOs = user.getTodos().stream()
                    .map(todo -> TodoDTO.builder()
                            .uuid(todo.getUuid())
                            .title(todo.getTitle())
                            .description(todo.getDescription())
                            .build())
                    .toList();

            return ResponseEntity.ok(todoDTOs);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
