package com.example.todo.adapter.in.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for user registration.
 */
public record RegisterRequest(
        @NotBlank(message = "Username không được để trống")
        @Size(min = 3, max = 50, message = "Username phải từ 3-50 ký tự")
        String username,

        @NotBlank(message = "Password không được để trống")
        @Size(min = 6, max = 100, message = "Password phải từ 6-100 ký tự")
        String password
) {}