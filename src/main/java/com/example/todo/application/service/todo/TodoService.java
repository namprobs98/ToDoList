package com.example.todo.application.service.todo;

import com.example.todo.application.port.in.todo.*;
import com.example.todo.application.port.out.todo.TodoRepositoryPort;
import com.example.todo.domain.exception.TodoNotFoundException;
import com.example.todo.domain.model.todo.Priority;
import com.example.todo.domain.model.todo.Status;
import com.example.todo.domain.model.todo.Todo;
import com.example.todo.domain.model.todo.TodoId;
import com.example.todo.shared.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Application service implementing the Todo use cases.
 * Coordinates domain logic and persistence.
 */
@Service
@Transactional
public class TodoService implements TodoUseCase {

    private final TodoRepositoryPort repository;

    public TodoService(TodoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Todo create(CreateTodoCommand command) {
        Todo todo = Todo.create(
            command.title(),
            command.description(),
            command.priority(),
            command.dueDate()
        );
        return repository.save(todo);
    }

    @Override
    public Todo update(String id, UpdateTodoCommand command) {
        TodoId todoId = TodoId.fromString(id);
        Todo todo = repository.findById(todoId)
            .orElseThrow(() -> new TodoNotFoundException(todoId));

        todo.update(
            command.title(),
            command.description(),
            command.priority(),
            command.status(),
            command.dueDate()
        );

        return repository.save(todo);
    }

    @Override
    public void delete(String id) {
        TodoId todoId = TodoId.fromString(id);
        Todo todo = repository.findById(todoId)
            .orElseThrow(() -> new TodoNotFoundException(todoId));

        todo.delete();
        repository.save(todo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Todo> findById(String id) {
        return repository.findById(TodoId.fromString(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Todo> findAll(int page, int size, Status status, Priority priority,
                                     String keyword, String sortBy, String sortDir) {
        List<Todo> content = repository.findAll(page, size, status, priority, keyword, sortBy, sortDir);
        long total = repository.count(status, priority, keyword);
        return PageResult.of(content, page, size, total);
    }

    @Override
    public Todo complete(String id) {
        TodoId todoId = TodoId.fromString(id);
        Todo todo = repository.findById(todoId)
            .orElseThrow(() -> new TodoNotFoundException(todoId));

        todo.complete();
        return repository.save(todo);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardData getDashboard() {
        long total = repository.countAll();
        long completed = repository.countByStatus(Status.COMPLETED);
        long inProgress = repository.countByStatus(Status.IN_PROGRESS);
        long overdue = repository.countOverdue();

        Map<Status, Long> statusCounts = Arrays.stream(Status.values())
            .collect(java.util.stream.Collectors.toMap(
                s -> s,
                s -> repository.countByStatus(s)
            ));

        Map<Priority, Long> priorityCounts = Arrays.stream(Priority.values())
            .collect(java.util.stream.Collectors.toMap(
                p -> p,
                p -> repository.countByPriority(p)
            ));

        return DashboardData.of(total, completed, inProgress, overdue, statusCounts, priorityCounts);
    }
}