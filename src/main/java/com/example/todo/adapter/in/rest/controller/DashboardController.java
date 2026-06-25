package com.example.todo.adapter.in.rest.controller;

import com.example.todo.adapter.in.rest.dto.response.DashboardResponse;
import com.example.todo.application.port.in.todo.TodoUseCase;
import com.example.todo.shared.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Dashboard operations.
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "Dashboard", description = "Dashboard and statistics APIs")
public class DashboardController {

    private final TodoUseCase todoUseCase;

    public DashboardController(TodoUseCase todoUseCase) {
        this.todoUseCase = todoUseCase;
    }

    @GetMapping
    @Operation(summary = "Get dashboard", description = "Retrieves dashboard statistics")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {
        var dashboard = todoUseCase.getDashboard();
        var response = DashboardResponse.fromDashboardData(dashboard);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}