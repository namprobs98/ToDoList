package com.example.todo.domain.model.todo;

import com.example.todo.domain.exception.DomainRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Todo aggregate root.
 */
@DisplayName("Todo Domain Model")
class TodoTest {

    @Nested
    @DisplayName("Create")
    class CreateTests {

        @Test
        @DisplayName("Should create todo with valid data")
        void shouldCreateTodoWithValidData() {
            LocalDateTime dueDate = LocalDateTime.now().plusDays(7);

            Todo todo = Todo.create("Test Title", "Test Description", Priority.HIGH, dueDate);

            assertNotNull(todo.getId());
            assertEquals("Test Title", todo.getTitle());
            assertEquals("Test Description", todo.getDescription());
            assertEquals(Priority.HIGH, todo.getPriority());
            assertEquals(Status.TODO, todo.getStatus());
            assertEquals(dueDate, todo.getDueDate());
            assertFalse(todo.isDeleted());
        }

        @Test
        @DisplayName("Should create todo with default priority")
        void shouldCreateTodoWithDefaultPriority() {
            Todo todo = Todo.create("Test Title", null, null, null);

            assertEquals(Priority.MEDIUM, todo.getPriority());
        }

        @Test
        @DisplayName("Should throw exception when title is blank")
        void shouldThrowExceptionWhenTitleIsBlank() {
            DomainRuleException exception = assertThrows(DomainRuleException.class,
                () -> Todo.create("", "Description", Priority.HIGH, null));

            assertEquals("TITLE_REQUIRED", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should throw exception when title is null")
        void shouldThrowExceptionWhenTitleIsNull() {
            DomainRuleException exception = assertThrows(DomainRuleException.class,
                () -> Todo.create(null, "Description", Priority.HIGH, null));

            assertEquals("TITLE_REQUIRED", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should throw exception when title exceeds 255 characters")
        void shouldThrowExceptionWhenTitleExceeds255Characters() {
            String longTitle = "a".repeat(256);

            DomainRuleException exception = assertThrows(DomainRuleException.class,
                () -> Todo.create(longTitle, "Description", Priority.HIGH, null));

            assertEquals("TITLE_TOO_LONG", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should throw exception when due date is in the past")
        void shouldThrowExceptionWhenDueDateIsInThePast() {
            LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

            DomainRuleException exception = assertThrows(DomainRuleException.class,
                () -> Todo.create("Title", "Description", Priority.HIGH, pastDate));

            assertEquals("INVALID_DUE_DATE", exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Update")
    class UpdateTests {

        @Test
        @DisplayName("Should update todo with valid data")
        void shouldUpdateTodoWithValidData() {
            Todo todo = Todo.create("Original Title", "Original Description", Priority.LOW, null);

            todo.update("New Title", "New Description", Priority.HIGH, Status.IN_PROGRESS, null);

            assertEquals("New Title", todo.getTitle());
            assertEquals("New Description", todo.getDescription());
            assertEquals(Priority.HIGH, todo.getPriority());
            assertEquals(Status.IN_PROGRESS, todo.getStatus());
        }

        @Test
        @DisplayName("Should throw exception when updating completed todo")
        void shouldThrowExceptionWhenUpdatingCompletedTodo() {
            Todo todo = Todo.create("Title", "Description", Priority.HIGH, null);
            todo.complete();

            DomainRuleException exception = assertThrows(DomainRuleException.class,
                () -> todo.update("New Title", null, null, null, null));

            assertEquals("TODO_COMPLETED", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should throw exception when updating deleted todo")
        void shouldThrowExceptionWhenUpdatingDeletedTodo() {
            Todo todo = Todo.create("Title", "Description", Priority.HIGH, null);
            todo.delete();

            DomainRuleException exception = assertThrows(DomainRuleException.class,
                () -> todo.update("New Title", null, null, null, null));

            assertEquals("TODO_DELETED", exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Complete")
    class CompleteTests {

        @Test
        @DisplayName("Should mark todo as completed")
        void shouldMarkTodoAsCompleted() {
            Todo todo = Todo.create("Title", "Description", Priority.HIGH, null);

            todo.complete();

            assertEquals(Status.COMPLETED, todo.getStatus());
        }

        @Test
        @DisplayName("Should throw exception when completing already completed todo")
        void shouldThrowExceptionWhenCompletingAlreadyCompletedTodo() {
            Todo todo = Todo.create("Title", "Description", Priority.HIGH, null);
            todo.complete();

            DomainRuleException exception = assertThrows(DomainRuleException.class,
                () -> todo.complete());

            assertEquals("ALREADY_COMPLETED", exception.getErrorCode());
        }

        @Test
        @DisplayName("Should throw exception when completing deleted todo")
        void shouldThrowExceptionWhenCompletingDeletedTodo() {
            Todo todo = Todo.create("Title", "Description", Priority.HIGH, null);
            todo.delete();

            DomainRuleException exception = assertThrows(DomainRuleException.class,
                () -> todo.complete());

            assertEquals("TODO_DELETED", exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Delete")
    class DeleteTests {

        @Test
        @DisplayName("Should mark todo as deleted")
        void shouldMarkTodoAsDeleted() {
            Todo todo = Todo.create("Title", "Description", Priority.HIGH, null);

            todo.delete();

            assertTrue(todo.isDeleted());
        }

        @Test
        @DisplayName("Should throw exception when deleting already deleted todo")
        void shouldThrowExceptionWhenDeletingAlreadyDeletedTodo() {
            Todo todo = Todo.create("Title", "Description", Priority.HIGH, null);
            todo.delete();

            DomainRuleException exception = assertThrows(DomainRuleException.class,
                () -> todo.delete());

            assertEquals("TODO_DELETED", exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("Overdue Check")
    class OverdueTests {

        @Test
        @DisplayName("Should return true when todo is overdue")
        void shouldReturnTrueWhenTodoIsOverdue() {
            LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
            Todo todo = Todo.create("Title", "Description", Priority.HIGH, pastDate);

            assertTrue(todo.isOverdue());
        }

        @Test
        @DisplayName("Should return false when todo has no due date")
        void shouldReturnFalseWhenTodoHasNoDueDate() {
            Todo todo = Todo.create("Title", "Description", Priority.HIGH, null);

            assertFalse(todo.isOverdue());
        }

        @Test
        @DisplayName("Should return false when todo is completed")
        void shouldReturnFalseWhenTodoIsCompleted() {
            LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
            Todo todo = Todo.create("Title", "Description", Priority.HIGH, pastDate);
            todo.complete();

            assertFalse(todo.isOverdue());
        }

        @Test
        @DisplayName("Should return false when due date is in the future")
        void shouldReturnFalseWhenDueDateIsInTheFuture() {
            LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
            Todo todo = Todo.create("Title", "Description", Priority.HIGH, futureDate);

            assertFalse(todo.isOverdue());
        }
    }
}