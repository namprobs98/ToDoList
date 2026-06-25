package com.example.todo.domain.model.todo;

/**
 * Status enum representing the current status of a Todo.
 * Values: TODO, IN_PROGRESS, COMPLETED
 */
public enum Status {
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}