package com.example.todoManager.controller;

import com.example.todoManager.model.Group;
import com.example.todoManager.model.Todo;
import com.example.todoManager.model.TodoOwner;
import com.example.todoManager.model.User;
import com.example.todoManager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllUsers_returnsUserDTOs() throws Exception {
        User user = User.builder()
                .uuid("uuid")
                .name("name")
                .email("email@email.at")
                .password("password")
                .build();

        BDDMockito.given(userService.getAllUsers()).willReturn(List.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(result -> assertEquals(200, result.getResponse().getStatus()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()))
                .andExpect(result -> assertEquals("""
                        [{"uuid":"uuid","name":"name","email":"email@email.at"}]""", result.getResponse().getContentAsString()));
    }
    @Test
    void testGetAllUsers_internalServerError() throws Exception {
        Mockito.when(userService.getAllUsers()).thenThrow(new RuntimeException("Unknown error"));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(result -> assertEquals(500, result.getResponse().getStatus()));

    }

    @Test
    void testGetUserGroups_returnsGroupDTOs() throws Exception {
        User user = User.builder().uuid("uuid").build();

        Group group = Group.builder().uuid("group1").name("Group1").build();

        user.setGroups(List.of(group));

        BDDMockito.given(userService.findByUuid("uuid")).willReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/uuid/groups"))
                .andExpect(result -> assertEquals(200, result.getResponse().getStatus()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()))
                .andExpect(result -> assertEquals("""
                        [{"uuid":"group1","name":"Group1"}]""", result.getResponse().getContentAsString()));



    }
    @Test
    void testGetUserGroups_userNotFound_returnsNotFound() throws Exception {
        Mockito.when(userService.findByUuid("uuid")).thenThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/uuid/groups"))
                .andExpect(result -> assertEquals(404, result.getResponse().getStatus()));
    }
    @Test
    void testGetUserGroups_withInvalidUser_returnsBadRequest() throws Exception {
        TodoOwner other = Mockito.mock(Group.class);

        Mockito.when(userService.findByUuid("uuid")).thenReturn(other);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/uuid/groups"))
                .andExpect(result -> assertEquals(400, result.getResponse().getStatus()));

    }

    @Test
    void testGetUserTodos_returnsTodoDTOs() throws Exception {
        User user = User.builder().uuid("uuid").build();

        Todo todo = Todo.builder().uuid("todo1").title("Todo1").build();

        user.setTodos(List.of(todo));

        BDDMockito.given(userService.findByUuid("uuid")).willReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/uuid/todos"))
                .andExpect(result -> assertEquals(200, result.getResponse().getStatus()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()))
                .andExpect(result -> assertEquals("""
                        [{"uuid":"todo1","title":"Todo1","description":null}]""", result.getResponse().getContentAsString()));
    }


    @Test
    void testGetUserTodos_withInvalidUser_returnsBadRequest() throws Exception {
        TodoOwner other = Mockito.mock(Group.class);

        Mockito.when(userService.findByUuid("uuid")).thenReturn(other);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/uuid/todos"))
                .andExpect(result -> assertEquals(400, result.getResponse().getStatus()));
    }

    @Test
    void testGetUserTodos_userNotFound_returnsNotFound() throws Exception {
        Mockito.when(userService.findByUuid("missing")).thenThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/missing/todos"))
                .andExpect(result -> assertEquals(404, result.getResponse().getStatus()));
    }
}