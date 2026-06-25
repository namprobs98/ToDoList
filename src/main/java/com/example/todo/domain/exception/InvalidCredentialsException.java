package com.example.todo.domain.exception;

/**
 * Exception thrown when login credentials are invalid.
 */
public class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException() {
        super("Username hoặc password không đúng", "INVALID_CREDENTIALS");
    }
}