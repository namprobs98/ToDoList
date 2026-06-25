package com.example.todo.application.port.in.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * Command for user login.
 */
public record LoginCommand(
        @NotBlank(message = "Username không được để trống")
        String username,

        @NotBlank(message = "Password không được để trống")
        String password
) {}