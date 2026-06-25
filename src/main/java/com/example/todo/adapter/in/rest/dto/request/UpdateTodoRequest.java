package com.example.todo.adapter.in.rest.dto.request;

import com.example.todo.domain.model.todo.Priority;
import com.example.todo.domain.model.todo.Status;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Request DTO for updating an existing Todo.
 */
public record UpdateTodoRequest(
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title,

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    String description,

    Priority priority,

    Status status,

    LocalDateTime dueDate
) {
    public UpdateTodoRequest {
        if (title != null) {
            title = title.trim();
        }
        if (description != null) {
            description = description.trim();
        }
    }
}