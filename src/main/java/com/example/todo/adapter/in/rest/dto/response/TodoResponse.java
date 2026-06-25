package com.example.todo.adapter.in.rest.dto.response;

import com.example.todo.domain.model.todo.Priority;
import com.example.todo.domain.model.todo.Status;
import com.example.todo.domain.model.todo.Todo;

import java.time.LocalDateTime;

/**
 * Response DTO for Todo.
 */
public record TodoResponse(
    String id,
    String title,
    String description,
    Priority priority,
    Status status,
    LocalDateTime dueDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static TodoResponse fromDomain(Todo todo) {
        return new TodoResponse(
            todo.getId().toString(),
            todo.getTitle(),
            todo.getDescription(),
            todo.getPriority(),
            todo.getStatus(),
            todo.getDueDate(),
            todo.getCreatedAt(),
            todo.getUpdatedAt()
        );
    }
}