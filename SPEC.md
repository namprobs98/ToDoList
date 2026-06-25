# TodoList System Specification

## 1. System Architecture

### Overview
- **Project Name**: TodoList Application
- **Type**: Fullstack Web Application (REST API + Modern SPA)
- **Architecture**: DDD (Domain-Driven Design) + Hexagonal Architecture

### Technology Stack

#### Backend
- Java 21
- Spring Boot 3.4.x
- Maven
- Spring Data JPA
- PostgreSQL 16
- Flyway Migration
- Lombok
- MapStruct
- Bean Validation
- OpenAPI Swagger
- JUnit5 + Mockito

#### Frontend
- Next.js 15 (App Router)
- React 19
- TypeScript
- TailwindCSS
- Shadcn UI
- React Hook Form + Zod
- Axios
- TanStack Query
- Zustand
- Framer Motion
- Lucide Icons

---

## 2. Database Design

### ERD

```
┌─────────────────────────────────────────────────────────────┐
│                        todo                                  │
├─────────────────────────────────────────────────────────────┤
│ id                 UUID         PRIMARY KEY                  │
│ title              VARCHAR(255) NOT NULL                    │
│ description        TEXT           NULL                      │
│ priority           VARCHAR(20)   NOT NULL                    │
│ status             VARCHAR(20)   NOT NULL                    │
│ due_date           TIMESTAMP      NULL                       │
│ created_at         TIMESTAMP     NOT NULL                    │
│ updated_at         TIMESTAMP     NOT NULL                    │
│ is_deleted         BOOLEAN       DEFAULT FALSE              │
└─────────────────────────────────────────────────────────────┘

Priority Enum: LOW, MEDIUM, HIGH
Status Enum: TODO, IN_PROGRESS, COMPLETED
```

### Flyway Migration

```sql
-- V1__create_todo_table.sql
CREATE TABLE todo (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    due_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_todo_status ON todo(status);
CREATE INDEX idx_todo_priority ON todo(priority);
CREATE INDEX idx_todo_is_deleted ON todo(is_deleted);
CREATE INDEX idx_todo_due_date ON todo(due_date);
```

---

## 3. Domain Model

### Todo Aggregate

```
Todo (Aggregate Root)
├── TodoId (Value Object - UUID)
├── title: String (required, max 255)
├── description: String (optional, max 5000)
├── priority: Priority (LOW, MEDIUM, HIGH)
├── status: Status (TODO, IN_PROGRESS, COMPLETED)
├── dueDate: LocalDateTime (optional)
├── createdAt: LocalDateTime
├── updatedAt: LocalDateTime
└── isDeleted: Boolean
```

### Domain Rules

1. **Title Validation**: Todo title không được rỗng, tối đa 255 ký tự
2. **Completed Todo Immutable**: Todo đã hoàn thành (COMPLETED) không được chỉnh sửa
3. **Due Date Validation**: DueDate không được nhỏ hơn ngày hiện tại
4. **Soft Delete**: Todo xóa sẽ được đánh dấu is_deleted = true, không truy cập được
5. **Complete Validation**: Chỉ có thể đánh dấu hoàn thành khi todo đang ở trạng thái TODO hoặc IN_PROGRESS

---

## 4. API Contract

### REST Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/todos | Create new todo |
| GET | /api/v1/todos | Get todo list with pagination, filtering, sorting |
| GET | /api/v1/todos/{id} | Get todo detail |
| PUT | /api/v1/todos/{id} | Update todo |
| DELETE | /api/v1/todos/{id} | Delete todo (soft delete) |
| PATCH | /api/v1/todos/{id}/complete | Mark todo as completed |
| GET | /api/v1/dashboard | Get dashboard statistics |

### Request/Response Models

#### CreateTodoRequest
```json
{
  "title": "string (required)",
  "description": "string (optional)",
  "priority": "LOW|MEDIUM|HIGH",
  "dueDate": "ISO8601 datetime (optional)"
}
```

#### UpdateTodoRequest
```json
{
  "title": "string",
  "description": "string",
  "priority": "LOW|MEDIUM|HIGH",
  "status": "TODO|IN_PROGRESS|COMPLETED",
  "dueDate": "ISO8601 datetime"
}
```

#### TodoResponse
```json
{
  "id": "uuid",
  "title": "string",
  "description": "string|null",
  "priority": "LOW|MEDIUM|HIGH",
  "status": "TODO|IN_PROGRESS|COMPLETED",
  "dueDate": "ISO8601 datetime|null",
  "createdAt": "ISO8601 datetime",
  "updatedAt": "ISO8601 datetime"
}
```

#### PageResponse
```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0,
  "first": true,
  "last": true
}
```

#### DashboardResponse
```json
{
  "totalTodos": 0,
  "completedTodos": 0,
  "inProgressTodos": 0,
  "overdueTodos": 0,
  "todosByStatus": [
    { "status": "TODO", "count": 0 },
    { "status": "IN_PROGRESS", "count": 0 },
    { "status": "COMPLETED", "count": 0 }
  ],
  "todosByPriority": [
    { "priority": "LOW", "count": 0 },
    { "priority": "MEDIUM", "count": 0 },
    { "priority": "HIGH", "count": 0 }
  ]
}
```

---

## 5. Backend Architecture (DDD + Hexagonal)

### Package Structure

```
com.example.todo/
├── domain/
│   ├── model/
│   │   └── todo/
│   │       ├── Todo.java
│   │       ├── TodoId.java
│   │       ├── Priority.java
│   │       └── Status.java
│   └── exception/
│       ├── TodoNotFoundException.java
│       ├── BusinessException.java
│       └── DomainRuleException.java
├── application/
│   ├── port/
│   │   ├── in/
│   │   │   └── todo/
│   │   │       ├── TodoUseCase.java
│   │   │       ├── CreateTodoCommand.java
│   │   │       ├── UpdateTodoCommand.java
│   │   │       └── CompleteTodoCommand.java
│   │   └── out/
│   │       └── todo/
│   │           └── TodoRepositoryPort.java
│   └── service/
│       └── todo/
│           ├── TodoService.java
│           ├── CreateTodoService.java
│           ├── UpdateTodoService.java
│           ├── DeleteTodoService.java
│           ├── CompleteTodoService.java
│           └── QueryTodoService.java
├── adapter/
│   ├── in/
│   │   └── rest/
│   │       ├── controller/
│   │       │   └── TodoController.java
│   │       ├── dto/
│   │       │   ├── request/
│   │       │   └── response/
│   │       └── mapper/
│   │           ├── TodoMapper.java
│   │           └── PageMapper.java
│   └── out/
│       └── persistence/
│           ├── entity/
│           │   └── TodoEntity.java
│           ├── repository/
│           │   └── TodoJpaRepository.java
│           ├── mapper/
│           │   └── TodoEntityMapper.java
│           └── adapter/
│               └── TodoRepositoryAdapter.java
├── shared/
│   ├── PageQuery.java
│   ├── PageResult.java
│   └── ApiResponse.java
├── config/
│   ├── SwaggerConfig.java
│   ├── JpaConfig.java
│   └── ApplicationConfig.java
├── exception/
│   └── GlobalExceptionHandler.java
└── TodoApplication.java
```

---

## 6. Frontend Architecture

### Folder Structure

```
src/
├── app/
│   ├── layout.tsx
│   ├── page.tsx
│   ├── providers.tsx
│   ├── dashboard/
│   │   └── page.tsx
│   └── todos/
│       ├── page.tsx
│       ├── [id]/
│       │   └── page.tsx
│       └── create/
│           └── page.tsx
├── components/
│   ├── ui/           (shadcn components)
│   ├── layout/
│   │   ├── Sidebar.tsx
│   │   ├── Header.tsx
│   │   └── MobileNav.tsx
│   └── shared/
│       ├── DataTable.tsx
│       ├── Pagination.tsx
│       ├── FilterBar.tsx
│       └── LoadingSkeleton.tsx
├── features/
│   └── todo/
│       ├── api/
│       │   └── todoApi.ts
│       ├── hooks/
│       │   ├── useTodos.ts
│       │   ├── useTodo.ts
│       │   ├── useCreateTodo.ts
│       │   ├── useUpdateTodo.ts
│       │   └── useDeleteTodo.ts
│       ├── components/
│       │   ├── TodoForm.tsx
│       │   ├── TodoCard.tsx
│       │   ├── TodoList.tsx
│       │   └── TodoFilters.tsx
│       ├── types/
│       │   └── todo.ts
│       └── store/
│           └── todoStore.ts
├── dashboard/
│   ├── components/
│   │   ├── StatCard.tsx
│   │   ├── StatusPieChart.tsx
│   │   └── PriorityBarChart.tsx
│   └── hooks/
│       └── useDashboard.ts
├── lib/
│   ├── axios.ts
│   ├── utils.ts
│   └── constants.ts
├── hooks/
│   └── useDebounce.ts
└── types/
    └── index.ts
```

---

## 7. UI/UX Design

### Color Palette
- **Primary**: Indigo (#6366f1)
- **Secondary**: Purple (#8b5cf6)
- **Background**: White (#ffffff) / Dark (#0f172a)
- **Surface**: Gray (#f8fafc) / Dark (#1e293b)
- **Text**: Slate (#1e293b) / Dark (#f8fafc)
- **Success**: Green (#22c55e)
- **Warning**: Amber (#f59e0b)
- **Error**: Red (#ef4444)

### Responsive Breakpoints
- Mobile: < 640px
- Tablet: 640px - 1024px
- Desktop: > 1024px

### Components
- Card-based design
- Smooth transitions with Framer Motion
- Loading skeletons for data fetching
- Empty states with illustrations
- Error states with retry options

---

## 8. Testing Strategy

### Backend Tests
- **Unit Tests**: Domain logic, Application services
- **Integration Tests**: REST API endpoints with @SpringBootTest

### Test Coverage Goals
- Minimum 80% coverage
- All domain rules tested
- All API endpoints tested

---

## 9. Docker Configuration

### Services
1. **Backend**: Spring Boot application on port 8080
2. **Frontend**: Next.js application on port 3000
3. **PostgreSQL**: Database on port 5432

### docker-compose.yml
- All services defined with proper health checks
- Environment variables configured
- Volume mounts for persistence