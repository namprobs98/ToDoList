package com.example.todo.application.port.in.todo;

import com.example.todo.domain.model.todo.Priority;
import com.example.todo.domain.model.todo.Status;

import java.util.List;
import java.util.Map;

/**
 * Dashboard data containing statistics about Todos.
 */
public record DashboardData(
    long totalTodos,
    long completedTodos,
    long inProgressTodos,
    long overdueTodos,
    List<StatusCount> todosByStatus,
    List<PriorityCount> todosByPriority
) {
    public record StatusCount(Status status, long count) {}

    public record PriorityCount(Priority priority, long count) {}

    public static DashboardData of(long total, long completed, long inProgress, long overdue,
                                   Map<Status, Long> statusCounts, Map<Priority, Long> priorityCounts) {
        List<StatusCount> byStatus = Status.values().stream()
            .map(s -> new StatusCount(s, statusCounts.getOrDefault(s, 0L)))
            .toList();

        List<PriorityCount> byPriority = Priority.values().stream()
            .map(p -> new PriorityCount(p, priorityCounts.getOrDefault(p, 0L)))
            .toList();

        return new DashboardData(total, completed, inProgress, overdue, byStatus, byPriority);
    }
}