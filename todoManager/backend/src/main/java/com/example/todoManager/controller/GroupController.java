package com.example.todoManager.controller;

import com.example.todoManager.dto.UserDTO;
import com.example.todoManager.model.Group;
import com.example.todoManager.model.Todo;
import com.example.todoManager.model.TodoOwner;
import com.example.todoManager.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/{uuid}/users")
    public ResponseEntity<List<UserDTO>> getUserGroups(@PathVariable String uuid) {
        TodoOwner todoOwner = groupService.findByUuid(uuid);

        if (todoOwner == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if (todoOwner instanceof Group group) {
            List<UserDTO> userDTOs = group.getUsers().stream()
                    .map(user -> UserDTO.builder()
                            .uuid(user.getUuid())
                            .name(user.getName())
                            .email(user.getEmail())
                            .build())
                    .toList();

            return ResponseEntity.ok(userDTOs);

        } else {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }
    @GetMapping("/{uuid}/todos")
    public Iterable<Todo> getGroupTodos(@PathVariable String uuid) {
        return groupService.findByUuid(uuid).getTodos();
    }
}
