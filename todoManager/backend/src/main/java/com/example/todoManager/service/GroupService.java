package com.example.todoManager.service;

import com.example.todoManager.model.Group;
import com.example.todoManager.model.TodoOwner;
import com.example.todoManager.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void save(Group group) {
        groupRepository.save(group);
    }

    public TodoOwner findByUuid(String uuid) {
        Optional<Group> group = groupRepository.findById(uuid);
        if (group.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        return group.get();
    }
}
