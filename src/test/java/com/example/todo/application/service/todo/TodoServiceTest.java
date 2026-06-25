package com.example.todo.application.service.todo;

import com.example.todo.application.port.in.todo.CreateTodoCommand;
import com.example.todo.application.port.in.todo.DashboardData;
import com.example.todo.application.port.in.todo.UpdateTodoCommand;
import com.example.todo.application.port.out.todo.TodoRepositoryPort;
import com.example.todo.domain.exception.TodoNotFoundException;
import com.example.todo.domain.model.todo.Priority;
import com.example.todo.domain.model.todo.Status;
import com.example.todo.domain.model.todo.Todo;
import com.example.todo.shared.PageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TodoService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TodoService")
class TodoServiceTest {

    @Mock
    private TodoRepositoryPort repository;

    @InjectMocks
    private TodoService todoService;

    private Todo sampleTodo;

    @BeforeEach
    void setUp() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(7);
        sampleTodo = Todo.create("Sample Title", "Sample Description", Priority.HIGH, dueDate);
    }

    @Test
    @DisplayName("Should create a new todo")
    void shouldCreateNewTodo() {
        CreateTodoCommand command = new CreateTodoCommand(
            "New Title", "New Description", Priority.HIGH, LocalDateTime.now().plusDays(7));

        when(repository.save(any(Todo.class))).thenReturn(sampleTodo);

        Todo result = todoService.create(command);

        assertNotNull(result);
        verify(repository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should update an existing todo")
    void shouldUpdateExistingTodo() {
        String todoId = sampleTodo.getId().toString();
        UpdateTodoCommand command = new UpdateTodoCommand(
            "Updated Title", null, Priority.LOW, null, null);

        when(repository.findById(any())).thenReturn(Optional.of(sampleTodo));
        when(repository.save(any(Todo.class))).thenReturn(sampleTodo);

        Todo result = todoService.update(todoId, command);

        assertNotNull(result);
        verify(repository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent todo")
    void shouldThrowExceptionWhenUpdatingNonExistentTodo() {
        String todoId = "non-existent-id";
        UpdateTodoCommand command = new UpdateTodoCommand(
            "Updated Title", null, Priority.LOW, null, null);

        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.update(todoId, command));
    }

    @Test
    @DisplayName("Should delete a todo")
    void shouldDeleteTodo() {
        String todoId = sampleTodo.getId().toString();

        when(repository.findById(any())).thenReturn(Optional.of(sampleTodo));
        doNothing().when(repository).save(any(Todo.class));

        todoService.delete(todoId);

        verify(repository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent todo")
    void shouldThrowExceptionWhenDeletingNonExistentTodo() {
        String todoId = "non-existent-id";

        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.delete(todoId));
    }

    @Test
    @DisplayName("Should find todo by id")
    void shouldFindTodoById() {
        String todoId = sampleTodo.getId().toString();

        when(repository.findById(any())).thenReturn(Optional.of(sampleTodo));

        Optional<Todo> result = todoService.findById(todoId);

        assertTrue(result.isPresent());
        assertEquals(sampleTodo.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Should return empty when todo not found")
    void shouldReturnEmptyWhenTodoNotFound() {
        String todoId = "non-existent-id";

        when(repository.findById(any())).thenReturn(Optional.empty());

        Optional<Todo> result = todoService.findById(todoId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should get all todos with pagination")
    void shouldGetAllTodosWithPagination() {
        List<Todo> todos = List.of(sampleTodo);
        PageResult<Todo> pageResult = PageResult.of(todos, 0, 10, 1);

        when(repository.findAll(anyInt(), anyInt(), any(), any(), any(), any(), any()))
            .thenReturn(todos);
        when(repository.count(any(), any(), any())).thenReturn(1L);

        PageResult<Todo> result = todoService.findAll(0, 10, null, null, null, "createdAt", "desc");

        assertEquals(1, result.content().size());
        assertEquals(1, result.totalElements());
    }

    @Test
    @DisplayName("Should complete a todo")
    void shouldCompleteTodo() {
        String todoId = sampleTodo.getId().toString();

        when(repository.findById(any())).thenReturn(Optional.of(sampleTodo));
        when(repository.save(any(Todo.class))).thenReturn(sampleTodo);

        Todo result = todoService.complete(todoId);

        assertNotNull(result);
        verify(repository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should throw exception when completing non-existent todo")
    void shouldThrowExceptionWhenCompletingNonExistentTodo() {
        String todoId = "non-existent-id";

        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.complete(todoId));
    }

    @Test
    @DisplayName("Should get dashboard data")
    void shouldGetDashboardData() {
        when(repository.countAll()).thenReturn(10L);
        when(repository.countByStatus(Status.COMPLETED)).thenReturn(5L);
        when(repository.countByStatus(Status.IN_PROGRESS)).thenReturn(3L);
        when(repository.countOverdue()).thenReturn(2L);
        when(repository.countByStatus(Status.TODO)).thenReturn(2L);

        for (Priority priority : Priority.values()) {
            when(repository.countByPriority(priority)).thenReturn(3L);
        }

        DashboardData result = todoService.getDashboard();

        assertEquals(10L, result.totalTodos());
        assertEquals(5L, result.completedTodos());
        assertEquals(3L, result.inProgressTodos());
        assertEquals(2L, result.overdueTodos());
    }
}