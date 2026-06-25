package com.example.todo.adapter.in.rest.dto.response;

/**
 * Response DTO for authentication.
 */
public record AuthResponse(
        String userId,
        String username,
        String token
) {}