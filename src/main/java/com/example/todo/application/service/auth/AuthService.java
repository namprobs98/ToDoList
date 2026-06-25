package com.example.todo.application.service.auth;

import com.example.todo.application.port.in.auth.AuthResult;
import com.example.todo.application.port.in.auth.LoginCommand;
import com.example.todo.application.port.in.auth.RegisterCommand;
import com.example.todo.application.port.out.user.UserRepositoryPort;
import com.example.todo.domain.exception.DuplicateUsernameException;
import com.example.todo.domain.exception.InvalidCredentialsException;
import com.example.todo.domain.model.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication service - handles user registration and login.
 */
@Service
public class AuthService {

    private final UserRepositoryPort userRepositoryPort;
    private final JwtService jwtService;

    public AuthService(UserRepositoryPort userRepositoryPort, JwtService jwtService) {
        this.userRepositoryPort = userRepositoryPort;
        this.jwtService = jwtService;
    }

    /**
     * Register a new user.
     */
    @Transactional
    public AuthResult register(RegisterCommand command) {
        // Check for duplicate username
        if (userRepositoryPort.existsByUsername(command.username())) {
            throw new DuplicateUsernameException(command.username());
        }

        // Create and save user
        User user = User.create(command.username(), command.password());
        User savedUser = userRepositoryPort.save(user);

        // Generate token
        String token = jwtService.generateToken(
                savedUser.getId().toString(),
                savedUser.getUsername()
        );

        return new AuthResult(
                savedUser.getId().toString(),
                savedUser.getUsername(),
                token
        );
    }

    /**
     * Login with username and password.
     */
    @Transactional(readOnly = true)
    public AuthResult login(LoginCommand command) {
        User user = userRepositoryPort.findByUsername(command.username())
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.matchesPassword(command.password())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(
                user.getId().toString(),
                user.getUsername()
        );

        return new AuthResult(
                user.getId().toString(),
                user.getUsername(),
                token
        );
    }
}