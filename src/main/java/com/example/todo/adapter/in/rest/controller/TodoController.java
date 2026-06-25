package com.example.todo.adapter.in.rest.controller;

import com.example.todo.adapter.in.rest.dto.request.CreateTodoRequest;
import com.example.todo.adapter.in.rest.dto.request.UpdateTodoRequest;
import com.example.todo.adapter.in.rest.dto.response.DashboardResponse;
import com.example.todo.adapter.in.rest.dto.response.PageResponse;
import com.example.todo.adapter.in.rest.dto.response.TodoResponse;
import com.example.todo.application.port.in.todo.CreateTodoCommand;
import com.example.todo.application.port.in.todo.TodoUseCase;
import com.example.todo.application.port.in.todo.UpdateTodoCommand;
import com.example.todo.domain.model.todo.Priority;
import com.example.todo.domain.model.todo.Status;
import com.example.todo.domain.model.todo.Todo;
import com.example.todo.shared.ApiResponse;
import com.example.todo.shared.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Todo operations.
 */
@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "Todo", description = "Todo management APIs")
public class TodoController {

    private final TodoUseCase todoUseCase;

    public TodoController(TodoUseCase todoUseCase) {
        this.todoUseCase = todoUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a new todo", description = "Creates a new todo with the provided details")
    public ResponseEntity<ApiResponse<TodoResponse>> createTodo(
            @Valid @RequestBody CreateTodoRequest request) {
        CreateTodoCommand command = new CreateTodoCommand(
            request.title(),
            request.description(),
            request.priority(),
            request.dueDate()
        );
        Todo todo = todoUseCase.create(command);
        TodoResponse response = TodoResponse.fromDomain(todo);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(response));
    }

    @GetMapping
    @Operation(summary = "Get all todos", description = "Retrieves todos with pagination, filtering, and sorting")
    public ResponseEntity<ApiResponse<PageResponse<TodoResponse>>> getTodos(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Filter by status") @RequestParam(required = false) Status status,
            @Parameter(description = "Filter by priority") @RequestParam(required = false) Priority priority,
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        PageResult<Todo> result = todoUseCase.findAll(page, size, status, priority, keyword, sortBy, sortDir);

        List<TodoResponse> content = result.content().stream()
            .map(TodoResponse::fromDomain)
            .toList();

        PageResponse<TodoResponse> response = new PageResponse<>(
            content,
            result.page(),
            result.size(),
            result.totalElements(),
            result.totalPages(),
            result.first(),
            result.last()
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get todo by ID", description = "Retrieves a specific todo by its ID")
    public ResponseEntity<ApiResponse<TodoResponse>> getTodoById(
            @Parameter(description = "Todo ID") @PathVariable String id) {
        return todoUseCase.findById(id)
            .map(todo -> {
                TodoResponse response = TodoResponse.fromDomain(todo);
                return ResponseEntity.ok(ApiResponse.success(response));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a todo", description = "Updates an existing todo with the provided details")
    public ResponseEntity<ApiResponse<TodoResponse>> updateTodo(
            @Parameter(description = "Todo ID") @PathVariable String id,
            @Valid @RequestBody UpdateTodoRequest request) {
        UpdateTodoCommand command = new UpdateTodoCommand(
            request.title(),
            request.description(),
            request.priority(),
            request.status(),
            request.dueDate()
        );
        Todo todo = todoUseCase.update(id, command);
        TodoResponse response = TodoResponse.fromDomain(todo);
        return ResponseEntity.ok(ApiResponse.updated(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a todo", description = "Soft deletes a todo by its ID")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @Parameter(description = "Todo ID") @PathVariable String id) {
        todoUseCase.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Mark todo as completed", description = "Marks a todo as completed")
    public ResponseEntity<ApiResponse<TodoResponse>> completeTodo(
            @Parameter(description = "Todo ID") @PathVariable String id) {
        Todo todo = todoUseCase.complete(id);
        TodoResponse response = TodoResponse.fromDomain(todo);
        return ResponseEntity.ok(ApiResponse.success("Marked as completed", response));
    }
}