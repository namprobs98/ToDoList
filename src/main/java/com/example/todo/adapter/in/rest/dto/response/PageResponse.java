package com.example.todo.adapter.in.rest.dto.response;

import com.example.todo.shared.PageResult;

import java.util.List;

/**
 * Generic page response wrapper.
 */
public record PageResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last
) {
    public static <T> PageResponse<T> fromPageResult(PageResult<T> result) {
        return new PageResponse<>(
            result.content(),
            result.page(),
            result.size(),
            result.totalElements(),
            result.totalPages(),
            result.first(),
            result.last()
        );
    }
}