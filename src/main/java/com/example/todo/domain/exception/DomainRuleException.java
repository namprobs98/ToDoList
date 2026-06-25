package com.example.todo.domain.exception;

/**
 * Exception thrown when a domain rule is violated.
 */
public class DomainRuleException extends BusinessException {

    public DomainRuleException(String message, String errorCode) {
        super(message, errorCode);
    }

    public DomainRuleException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}