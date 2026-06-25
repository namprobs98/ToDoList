package com.example.todo.application.port.in.todo;

import com.example.todo.domain.model.todo.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Command for creating a new Todo.
 */
public record CreateTodoCommand(
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title,

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    String description,

    Priority priority,

    LocalDateTime dueDate
) {
    public CreateTodoCommand {
        if (title != null) {
            title = title.trim();
        }
        if (description != null) {
            description = description.trim();
        }
    }
}