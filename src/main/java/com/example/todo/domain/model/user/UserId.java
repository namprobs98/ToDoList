package com.example.todo.domain.model.user;

import java.util.UUID;

/**
 * User ID Value Object.
 */
public record UserId(UUID id) {

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(String id) {
        return new UserId(UUID.fromString(id));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}