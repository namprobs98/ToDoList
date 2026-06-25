package com.example.todo.shared;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Standard API response wrapper.
 *
 * @param <T> the type of data
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    boolean success,
    String message,
    T data,
    LocalDateTime timestamp,
    String errorCode
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data, LocalDateTime.now(), null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, LocalDateTime.now(), null);
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(false, message, null, LocalDateTime.now(), errorCode);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(true, "Created successfully", data, LocalDateTime.now(), null);
    }

    public static <T> ApiResponse<T> updated(T data) {
        return new ApiResponse<>(true, "Updated successfully", data, LocalDateTime.now(), null);
    }

    public static <T> ApiResponse<T> deleted() {
        return new ApiResponse<>(true, "Deleted successfully", null, LocalDateTime.now(), null);
    }
}