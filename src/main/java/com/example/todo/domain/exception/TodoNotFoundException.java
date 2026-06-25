package com.example.todo.domain.exception;

import com.example.todo.domain.model.todo.TodoId;

/**
 * Exception thrown when a Todo is not found.
 */
public class TodoNotFoundException extends BusinessException {

    public TodoNotFoundException(TodoId id) {
        super("Todo not found with id: " + id, "TODO_NOT_FOUND");
    }

    public TodoNotFoundException(String message) {
        super(message, "TODO_NOT_FOUND");
    }
}