package com.example.todo.application.port.in.todo;

import com.example.todo.domain.model.todo.Priority;
import com.example.todo.domain.model.todo.Status;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Command for updating an existing Todo.
 */
public record UpdateTodoCommand(
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title,

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    String description,

    Priority priority,

    Status status,

    LocalDateTime dueDate
) {
    public UpdateTodoCommand {
        if (title != null) {
            title = title.trim();
        }
        if (description != null) {
            description = description.trim();
        }
    }
}