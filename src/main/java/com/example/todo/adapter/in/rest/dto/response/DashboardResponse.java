package com.example.todo.adapter.in.rest.dto.response;

import com.example.todo.application.port.in.todo.DashboardData;
import com.example.todo.domain.model.todo.Priority;
import com.example.todo.domain.model.todo.Status;

import java.util.List;

/**
 * Response DTO for Dashboard.
 */
public record DashboardResponse(
    long totalTodos,
    long completedTodos,
    long inProgressTodos,
    long overdueTodos,
    List<StatusCount> todosByStatus,
    List<PriorityCount> todosByPriority
) {
    public record StatusCount(Status status, long count) {}

    public record PriorityCount(Priority priority, long count) {}

    public static DashboardResponse fromDashboardData(DashboardData data) {
        List<StatusCount> byStatus = data.todosByStatus().stream()
            .map(s -> new StatusCount(s.status(), s.count()))
            .toList();

        List<PriorityCount> byPriority = data.todosByPriority().stream()
            .map(p -> new PriorityCount(p.priority(), p.count()))
            .toList();

        return new DashboardResponse(
            data.totalTodos(),
            data.completedTodos(),
            data.inProgressTodos(),
            data.overdueTodos(),
            byStatus,
            byPriority
        );
    }
}