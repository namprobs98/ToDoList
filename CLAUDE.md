# TodoList Application - Project Documentation

## Project Overview

This is a modern TodoList application built with **DDD (Domain-Driven Design)** and **Hexagonal Architecture** using Spring Boot 3.x (Backend) and Next.js 15 (Frontend).

## Technology Stack

### Backend
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

### Frontend
- Next.js 15 (App Router)
- React 19
- TypeScript
- TailwindCSS
- Shadcn UI components
- React Hook Form + Zod
- Axios
- TanStack Query
- Zustand
- Framer Motion
- Lucide Icons

---

## Architecture

### Backend: DDD + Hexagonal Architecture

```
com.example.todo/
├── domain/              # Domain Layer - Business Logic
│   ├── model/todo/     # Domain Models (Todo, TodoId, Priority, Status)
│   └── exception/      # Domain Exceptions
├── application/         # Application Layer - Use Cases
│   ├── port/
│   │   ├── in/         # Input Ports (Use Case Interfaces)
│   │   └── out/        # Output Ports (Repository Interfaces)
│   └── service/        # Application Services
├── adapter/            # Adapters Layer
│   ├── in/rest/        # REST API (Controllers, DTOs)
│   └── out/           # Persistence (JPA, Repository Implementations)
├── shared/             # Shared utilities
├── config/             # Configuration classes
└── exception/         # Global Exception Handling
```

### Frontend: Clean Architecture

```
src/
├── app/               # Next.js App Router pages
├── components/        # Shared UI components
├── features/todo/     # Feature-based organization
│   ├── api/          # API functions
│   ├── hooks/        # React Query hooks
│   ├── store/        # Zustand store
│   └── types/        # TypeScript types
├── lib/              # Utilities and config
└── providers/        # React providers
```

---

## Database Design

### PostgreSQL Schema

```sql
CREATE TABLE todo (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    due_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL
);
```

### Enum Values
- **Priority**: LOW, MEDIUM, HIGH
- **Status**: TODO, IN_PROGRESS, COMPLETED

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/todos | Create new todo |
| GET | /api/v1/todos | Get todos with pagination/filtering |
| GET | /api/v1/todos/{id} | Get todo by ID |
| PUT | /api/v1/todos/{id} | Update todo |
| DELETE | /api/v1/todos/{id} | Delete todo (soft delete) |
| PATCH | /api/v1/todos/{id}/complete | Mark as completed |
| GET | /api/v1/dashboard | Get dashboard statistics |

---

## Domain Rules

1. **Title Validation**: Title không được rỗng, tối đa 255 ký tự
2. **Completed Todo Immutable**: Todo đã COMPLETED không được chỉnh sửa
3. **Due Date Validation**: DueDate không được nhỏ hơn ngày hiện tại
4. **Soft Delete**: Todo xóa được đánh dấu is_deleted = true
5. **Complete Validation**: Chỉ TODO hoặc IN_PROGRESS mới có thể complete

---

## Coding Conventions

### Naming Conventions
- **Classes**: PascalCase (e.g., `TodoService`, `TodoController`)
- **Methods**: camelCase (e.g., `createTodo`, `findById`)
- **Variables**: camelCase (e.g., `todoList`, `isDeleted`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `DEFAULT_PAGE_SIZE`)
- **Packages**: lowercase (e.g., `com.example.todo.domain.model`)

### Package Structure
- Use singular for domain models (`todo` not `todos`)
- Group by layer: `domain/`, `application/`, `adapter/`
- Use descriptive names: `CreateTodoCommand` not `CreateTodoDTO`

### Code Style
- Use Lombok to reduce boilerplate
- Use Java records for DTOs and commands
- Use builder pattern for entity creation
- Follow SOLID principles
- Write meaningful comments for complex logic

---

## Building and Running

### Backend

```bash
# Build
mvn clean package

# Run locally
mvn spring-boot:run

# Run with Docker
docker build -f Dockerfile.backend -t todo-backend .
docker run -p 8080:8080 todo-backend
```

### Frontend

```bash
# Install dependencies
cd frontend
npm install

# Run development
npm run dev

# Build
npm run build

# Run production
npm start
```

### Docker Compose

```bash
# Build and run all services
docker-compose up --build

# Stop all services
docker-compose down
```

---

## Testing Strategy

### Unit Tests
- Test domain logic in isolation
- Test application services with mocked repositories
- Minimum 80% coverage target

### Integration Tests
- Test REST API endpoints
- Use @SpringBootTest with H2 in-memory database
- Test CRUD operations

### Test Commands
```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

---

## Environment Variables

### Backend (application.properties)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todo_db
spring.datasource.username=todo_user
spring.datasource.password=todo_password
```

### Frontend
```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
```

---

## UI/UX Design

### Color Palette
- Primary: Indigo (#6366f1)
- Secondary: Purple (#8b5cf6)
- Success: Green (#22c55e)
- Warning: Amber (#f59e0b)
- Error: Red (#ef4444)

### Responsive Breakpoints
- Mobile: < 640px
- Tablet: 640px - 1024px
- Desktop: > 1024px

### Features
- Framer Motion animations
- Loading skeletons
- Empty states
- Error states
- Dark mode support

---

## Git Workflow

1. Create feature branch: `git checkout -b feature/todo-feature`
2. Commit changes with descriptive messages
3. Push to remote: `git push origin feature/todo-feature`
4. Create pull request for review

---

## Guidelines for Adding New Features

When adding new code to this project:

1. **Follow DDD**: Place business logic in the domain layer
2. **Use Ports**: Define interfaces in application/port/ before implementing
3. **Keep Controllers Thin**: Never put business logic in controllers
4. **Validate Input**: Use Bean Validation in DTOs
5. **Write Tests**: Add unit tests for new domain logic
6. **Update API Docs**: Document new endpoints in Swagger

---

## Quick Reference

- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Frontend**: http://localhost:3000
- **PostgreSQL**: localhost:5432 (user: todo_user, password: todo_password)