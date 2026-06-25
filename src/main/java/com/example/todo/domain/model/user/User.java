package com.example.todo.domain.model.user;

import com.example.todo.domain.exception.DomainRuleException;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User Domain Entity.
 */
public class User {

    private final UserId id;
    private String username;
    private String password;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(UserId id, String username, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validateUsername(username);
        validatePassword(password);

        this.id = id;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String username, String password) {
        LocalDateTime now = LocalDateTime.now();
        return new User(
                UserId.generate(),
                username,
                password,
                now,
                now
        );
    }

    public static User restore(UUID id, String username, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new User(new UserId(id), username, password, createdAt, updatedAt);
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new DomainRuleException("Username không được để trống", "INVALID_USERNAME");
        }
        if (username.length() < 3) {
            throw new DomainRuleException("Username phải có ít nhất 3 ký tự", "INVALID_USERNAME");
        }
        if (username.length() > 50) {
            throw new DomainRuleException("Username không được quá 50 ký tự", "INVALID_USERNAME");
        }
        // Username chỉ chứa chữ cái, số và dấu gạch dưới
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new DomainRuleException("Username chỉ được chứa chữ cái, số và dấu gạch dưới", "INVALID_USERNAME");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new DomainRuleException("Password không được để trống", "INVALID_PASSWORD");
        }
        if (password.length() < 6) {
            throw new DomainRuleException("Password phải có ít nhất 6 ký tự", "INVALID_PASSWORD");
        }
        if (password.length() > 100) {
            throw new DomainRuleException("Password không được quá 100 ký tự", "INVALID_PASSWORD");
        }
    }

    public void updatePassword(String newPassword) {
        validatePassword(newPassword);
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean matchesPassword(String password) {
        return this.password.equals(password);
    }

    public UserId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}