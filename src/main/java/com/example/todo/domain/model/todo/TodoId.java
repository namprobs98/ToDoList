package com.example.todo.domain.model.todo;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing the unique identifier of a Todo.
 * Uses UUID for distributed unique identification.
 */
public final class TodoId implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID value;

    private TodoId(UUID value) {
        this.value = value;
    }

    /**
     * Creates a new unique TodoId.
     *
     * @return a new TodoId with a randomly generated UUID
     */
    public static TodoId create() {
        return new TodoId(UUID.randomUUID());
    }

    /**
     * Creates a TodoId from an existing UUID value.
     *
     * @param value the UUID value
     * @return a new TodoId
     */
    public static TodoId of(UUID value) {
        Objects.requireNonNull(value, "UUID value cannot be null");
        return new TodoId(value);
    }

    /**
     * Creates a TodoId from a string representation.
     *
     * @param value the string representation of UUID
     * @return a new TodoId
     * @throws IllegalArgumentException if the string is not a valid UUID
     */
    public static TodoId fromString(String value) {
        Objects.requireNonNull(value, "UUID string cannot be null");
        return new TodoId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoId todoId = (TodoId) o;
        return Objects.equals(value, todoId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}