package com.example.todo.domain.model.todo;

/**
 * Priority enum representing the priority level of a Todo.
 * Values: LOW, MEDIUM, HIGH
 */
public enum Priority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}