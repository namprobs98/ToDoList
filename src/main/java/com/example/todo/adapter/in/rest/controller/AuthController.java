package com.example.todo.adapter.in.rest.controller;

import com.example.todo.adapter.in.rest.dto.request.LoginRequest;
import com.example.todo.adapter.in.rest.dto.request.RegisterRequest;
import com.example.todo.adapter.in.rest.dto.response.AuthResponse;
import com.example.todo.application.port.in.auth.AuthResult;
import com.example.todo.application.service.auth.AuthService;
import com.example.todo.shared.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResult result = authService.register(
                new com.example.todo.application.port.in.auth.RegisterCommand(
                        request.username(),
                        request.password()
                )
        );

        AuthResponse response = new AuthResponse(
                result.userId(),
                result.username(),
                result.token()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Đăng ký thành công", response));
    }

    /**
     * Login with username and password.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResult result = authService.login(
                new com.example.todo.application.port.in.auth.LoginCommand(
                        request.username(),
                        request.password()
                )
        );

        AuthResponse response = new AuthResponse(
                result.userId(),
                result.username(),
                result.token()
        );

        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
    }
}