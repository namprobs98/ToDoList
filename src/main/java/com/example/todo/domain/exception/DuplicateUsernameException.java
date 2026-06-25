package com.example.todo.domain.exception;

/**
 * Exception thrown when attempting to create a user with a duplicate username.
 */
public class DuplicateUsernameException extends BusinessException {

    public DuplicateUsernameException(String username) {
        super("Username '" + username + "' đã tồn tại", "DUPLICATE_USERNAME");
    }

    public DuplicateUsernameException() {
        super("Username đã tồn tại", "DUPLICATE_USERNAME");
    }
}