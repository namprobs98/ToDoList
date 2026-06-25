package com.example.todo.application.port.in.todo;

import com.example.todo.domain.model.todo.Priority;
import com.example.todo.domain.model.todo.Status;
import com.example.todo.domain.model.todo.Todo;
import com.example.todo.shared.PageResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Input port (Use Case) interface for Todo operations.
 * Defines all operations that can be performed on Todos.
 */
public interface TodoUseCase {

    /**
     * Creates a new Todo.
     *
     * @param command the create command
     * @return the created Todo
     */
    Todo create(CreateTodoCommand command);

    /**
     * Updates an existing Todo.
     *
     * @param id      the Todo ID
     * @param command the update command
     * @return the updated Todo
     */
    Todo update(String id, UpdateTodoCommand command);

    /**
     * Deletes a Todo.
     *
     * @param id the Todo ID
     */
    void delete(String id);

    /**
     * Finds a Todo by ID.
     *
     * @param id the Todo ID
     * @return the Todo if found
     */
    Optional<Todo> findById(String id);

    /**
     * Finds all Todos with pagination, filtering, and sorting.
     *
     * @param page     the page number
     * @param size     the page size
     * @param status   the status filter (optional)
     * @param priority the priority filter (optional)
     * @param keyword  the keyword search (optional)
     * @param sortBy    the field to sort by
     * @param sortDir   the sort direction (asc/desc)
     * @return page result of Todos
     */
    PageResult<Todo> findAll(int page, int size, Status status, Priority priority,
                             String keyword, String sortBy, String sortDir);

    /**
     * Marks a Todo as completed.
     *
     * @param id the Todo ID
     * @return the completed Todo
     */
    Todo complete(String id);

    /**
     * Gets dashboard statistics.
     *
     * @return dashboard data
     */
    DashboardData getDashboard();
}