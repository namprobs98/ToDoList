package com.example.todo.domain.model.todo;

import com.example.todo.domain.exception.BusinessException;
import com.example.todo.domain.exception.DomainRuleException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Todo Aggregate Root.
 * Represents a todo item with all business rules encapsulated.
 */
public final class Todo {

    private final TodoId id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDateTime dueDate;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;

    private Todo(TodoId id, String title, String description, Priority priority,
                 Status status, LocalDateTime dueDate, LocalDateTime createdAt,
                 LocalDateTime updatedAt, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    /**
     * Factory method to create a new Todo.
     *
     * @param title       the title (required)
     * @param description the description (optional)
     * @param priority    the priority
     * @param dueDate     the due date (optional)
     * @return a new Todo instance
     * @throws DomainRuleException if title is blank
     */
    public static Todo create(String title, String description, Priority priority, LocalDateTime dueDate) {
        validateTitle(title);
        validateDueDate(dueDate);

        LocalDateTime now = LocalDateTime.now();
        return new Todo(
            TodoId.create(),
            title.trim(),
            description != null ? description.trim() : null,
            priority != null ? priority : Priority.MEDIUM,
            Status.TODO,
            dueDate,
            now,
            now,
            false
        );
    }

    /**
     * Factory method to reconstruct a Todo from persistence.
     */
    public static Todo reconstitute(TodoId id, String title, String description, Priority priority,
                                    Status status, LocalDateTime dueDate, LocalDateTime createdAt,
                                    LocalDateTime updatedAt, boolean isDeleted) {
        return new Todo(id, title, description, priority, status, dueDate, createdAt, updatedAt, isDeleted);
    }

    /**
     * Updates the Todo with new values.
     */
    public void update(String title, String description, Priority priority, Status status, LocalDateTime dueDate) {
        checkNotDeleted();
        checkNotCompleted();

        if (title != null && !title.trim().isEmpty()) {
            validateTitle(title);
            this.title = title.trim();
        }
        if (description != null) {
            this.description = description.trim().isEmpty() ? null : description.trim();
        }
        if (priority != null) {
            this.priority = priority;
        }
        if (status != null) {
            this.status = status;
        }
        if (dueDate != null) {
            validateDueDate(dueDate);
            this.dueDate = dueDate;
        }
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marks the Todo as completed.
     * Can only be completed if status is TODO or IN_PROGRESS.
     */
    public void complete() {
        checkNotDeleted();
        checkCanComplete();

        this.status = Status.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marks the Todo as deleted (soft delete).
     */
    public void delete() {
        checkNotDeleted();
        this.isDeleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if the Todo is overdue.
     */
    public boolean isOverdue() {
        if (dueDate == null || status == Status.COMPLETED) {
            return false;
        }
        return LocalDateTime.now().isAfter(dueDate);
    }

    // Validation methods
    private static void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new DomainRuleException("Title cannot be empty", "TITLE_REQUIRED");
        }
        if (title.trim().length() > 255) {
            throw new DomainRuleException("Title cannot exceed 255 characters", "TITLE_TOO_LONG");
        }
    }

    private static void validateDueDate(LocalDateTime dueDate) {
        if (dueDate != null && dueDate.isBefore(LocalDateTime.now())) {
            throw new DomainRuleException("Due date cannot be in the past", "INVALID_DUE_DATE");
        }
    }

    private void checkNotDeleted() {
        if (isDeleted) {
            throw new DomainRuleException("Todo has been deleted", "TODO_DELETED");
        }
    }

    private void checkNotCompleted() {
        if (status == Status.COMPLETED) {
            throw new DomainRuleException("Cannot update a completed todo", "TODO_COMPLETED");
        }
    }

    private void checkCanComplete() {
        if (status == Status.COMPLETED) {
            throw new DomainRuleException("Todo is already completed", "ALREADY_COMPLETED");
        }
    }

    // Getters
    public TodoId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}