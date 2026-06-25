package com.example.todo.adapter.in.rest.controller;

import com.example.todo.adapter.in.rest.dto.request.CreateTodoRequest;
import com.example.todo.adapter.in.rest.dto.request.UpdateTodoRequest;
import com.example.todo.adapter.in.rest.dto.response.DashboardResponse;
import com.example.todo.adapter.in.rest.dto.response.TodoResponse;
import com.example.todo.domain.model.todo.Priority;
import com.example.todo.domain.model.todo.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for TodoController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create a new todo")
    void shouldCreateNewTodo() throws Exception {
        CreateTodoRequest request = new CreateTodoRequest(
            "Test Todo",
            "Test Description",
            Priority.HIGH,
            LocalDateTime.now().plusDays(7)
        );

        MvcResult result = mockMvc.perform(post("/api/v1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.title").value("Test Todo"))
            .andExpect(jsonPath("$.data.priority").value("HIGH"))
            .andExpect(jsonPath("$.data.status").value("TODO"))
            .andReturn();

        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("id"));
    }

    @Test
    @DisplayName("Should return validation error when title is blank")
    void shouldReturnValidationErrorWhenTitleIsBlank() throws Exception {
        CreateTodoRequest request = new CreateTodoRequest(
            "",
            "Test Description",
            Priority.HIGH,
            null
        );

        mockMvc.perform(post("/api/v1/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Should get all todos with pagination")
    void shouldGetAllTodosWithPagination() throws Exception {
        mockMvc.perform(get("/api/v1/todos")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.page").value(0))
            .andExpect(jsonPath("$.data.size").value(10));
    }

    @Test
    @DisplayName("Should filter todos by status")
    void shouldFilterTodosByStatus() throws Exception {
        mockMvc.perform(get("/api/v1/todos")
                .param("status", "TODO"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should filter todos by priority")
    void shouldFilterTodosByPriority() throws Exception {
        mockMvc.perform(get("/api/v1/todos")
                .param("priority", "HIGH"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should search todos by keyword")
    void shouldSearchTodosByKeyword() throws Exception {
        mockMvc.perform(get("/api/v1/todos")
                .param("keyword", "test"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should sort todos by createdAt")
    void shouldSortTodosByCreatedAt() throws Exception {
        mockMvc.perform(get("/api/v1/todos")
                .param("sortBy", "createdAt")
                .param("sortDir", "desc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should get dashboard data")
    void shouldGetDashboardData() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.totalTodos").isNumber())
            .andExpect(jsonPath("$.data.completedTodos").isNumber())
            .andExpect(jsonPath("$.data.inProgressTodos").isNumber())
            .andExpect(jsonPath("$.data.todosByStatus").isArray())
            .andExpect(jsonPath("$.data.todosByPriority").isArray());
    }
}