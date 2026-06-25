package com.example.todo.application.port.in.auth;

/**
 * Authentication result containing user info and token.
 */
public record AuthResult(
        String userId,
        String username,
        String token
) {}