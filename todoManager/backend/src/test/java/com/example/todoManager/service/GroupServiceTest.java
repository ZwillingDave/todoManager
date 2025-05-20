package com.example.todoManager.service;

import com.example.todoManager.exception.GroupNotFoundException;
import com.example.todoManager.model.Group;
import com.example.todoManager.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        groupService = new GroupService(groupRepository);
    }

    @Test
    void save_shouldCallRepositorySave() {
        Group group = Group.builder().build();
        groupService.save(group);

        Mockito.verify(groupRepository, Mockito.times(1)).save(group);
    }

    @Test
    void findByUuid_shouldReturnGroupIfExists() {
        Group group = Group.builder().uuid("uuid").build();

        Mockito.when(groupRepository.findById("uuid")).thenReturn(Optional.of(group));
        groupService.findByUuid("uuid");

        Mockito.verify(groupRepository, Mockito.times(1)).findById("uuid");
    }

    @Test
    void findById_shouldThrowExceptionWhenGroupNotFound() {
        Mockito.when(groupRepository.findById("test")).thenReturn(Optional.empty());

        GroupNotFoundException exception = assertThrows(GroupNotFoundException.class, () -> groupService.findByUuid("test"));

        assertEquals("Group not found", exception.getMessage());
    }
}