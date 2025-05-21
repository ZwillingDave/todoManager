package com.example.todoManager.controller;

import com.example.todoManager.exception.TodoNotFoundException;
import com.example.todoManager.model.Todo;
import com.example.todoManager.service.TodoService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc(addFilters = false)
class TodoControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private TodoService todoService;


    @Test
    void getTodoById_returnsTodo() throws Exception {

        String testUuid = "testUuid";
        Todo todo = Todo.builder().uuid(testUuid).title("title").description("desc").todoOwnerUuid("o1").build();

        BDDMockito.given(todoService.findById(testUuid)).willReturn(todo);
        mvc.perform(MockMvcRequestBuilders.get("/api/todos/" + testUuid))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value("testUuid"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("desc"));
    }

    @Test
    void getTodoById_InvalidUuid_returnsNotFound() throws Exception {
        BDDMockito.given(todoService.findById("nonValid")).willThrow(new TodoNotFoundException());
        mvc.perform(MockMvcRequestBuilders.get("/api/todos/nonValid"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}