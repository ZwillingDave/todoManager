package com.example.todoManager.controller;

import com.example.todoManager.model.Group;
import com.example.todoManager.model.Todo;
import com.example.todoManager.model.User;
import com.example.todoManager.service.GroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GroupService groupService;

    private final String GROUP_UUID = "groupUuid";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetGroupUsers_returnsUserDTOs() throws Exception {
        User user1 = User.builder().uuid("u1").name("Alice").email("alice@example.com").build();
        User user2 = User.builder().uuid("u2").name("Bob").email("bob@example.com").build();

        Group group = Group.builder().uuid(GROUP_UUID).name("Test Group").users(List.of(user1, user2)).build();

        BDDMockito.given(groupService.findByUuid(GROUP_UUID)).willReturn(group);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/groups/" + GROUP_UUID + "/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").value("u1"))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].email").value("alice@example.com"))
                .andExpect(jsonPath("$[1].uuid").value("u2"))
                .andExpect(jsonPath("$[1].name").value("Bob"))
                .andExpect(jsonPath("$[1].email").value("bob@example.com"));
    }

    @Test
    void testGetGroupUsers_WithInvalidUuid_returnsBadRequest() throws Exception {
        BDDMockito.given(groupService.findByUuid(GROUP_UUID)).willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/groups/" + GROUP_UUID + "/users"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testGetGroupUsers_todoOwnerNotGroup_returnsNotFound() throws Exception {
        User user1 = User.builder().uuid("u1").name("Alice").email("alice@example.com").build();

        BDDMockito.given(groupService.findByUuid(GROUP_UUID)).willReturn(user1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/groups/" + GROUP_UUID + "/users"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsersFromGroup_emptyUsers_returnsBadRequest() throws Exception {
        Group group = Group.builder().uuid(GROUP_UUID).name("Empty Group").users(List.of()).build();
        BDDMockito.given(groupService.findByUuid(GROUP_UUID)).willReturn(group);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/groups/" + GROUP_UUID + "/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTodosFromGroup_returnsTodos() throws Exception {
        Todo todo = Todo.builder().uuid("t1").title("title1").description("desc1").todoOwnerUuid(GROUP_UUID).build();
        Group group = Group.builder().uuid(GROUP_UUID).todos(List.of(todo)).build();

        BDDMockito.given(groupService.findByUuid(GROUP_UUID)).willReturn(group);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/groups/" + GROUP_UUID + "/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").value("t1"))
                .andExpect(jsonPath("$[0].title").value("title1"))
                .andExpect(jsonPath("$[0].description").value("desc1"));
    }

    @Test
    void getGroupTodos_invalidUuid_returnsBadRequest() throws Exception {
        BDDMockito.given(groupService.findByUuid(GROUP_UUID)).willReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/groups/" + GROUP_UUID + "/todos"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getGroupTodos_todoOwnerNotGroup_returnsNotFound() throws Exception {
        User user1 = User.builder().uuid("u1").name("Alice").email("alice@example.com").build();

        BDDMockito.given(groupService.findByUuid(GROUP_UUID)).willReturn(user1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/groups/" + GROUP_UUID + "/todos"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getGroupTodos_emptyTodos_returnsEmptyList() throws Exception {
        Group group = Group.builder().uuid(GROUP_UUID).name("Empty Group").todos(List.of()).build();
        BDDMockito.given(groupService.findByUuid(GROUP_UUID)).willReturn(group);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/groups/" + GROUP_UUID + "/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}