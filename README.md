# ToDoList Application

A modern TodoList application built with **DDD (Domain-Driven Design)** and **Hexagonal Architecture** using Spring Boot 3.x (Backend) and Next.js 15 (Frontend).

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Project Structure](#project-structure)
- [Contributing](#contributing)

---

## ✨ Features

- ✅ Create, Read, Update, Delete todos
- ✅ Priority levels (LOW, MEDIUM, HIGH)
- ✅ Status tracking (TODO, IN_PROGRESS, COMPLETED)
- ✅ Due date management
- ✅ Dashboard statistics
- ✅ Soft delete support
- ✅ RESTful API with Swagger documentation
- ✅ Modern, responsive UI with dark mode support

---

## 🛠 Technology Stack

### Backend

| Technology | Description |
|------------|-------------|
| Java 21 | Programming language |
| Spring Boot 3.4.x | Framework |
| Maven | Build tool |
| Spring Data JPA | ORM |
| PostgreSQL 16 | Database |
| Flyway | Database migration |
| Lombok | Boilerplate reduction |
| MapStruct | Object mapping |
| Bean Validation | Input validation |
| OpenAPI Swagger | API documentation |
| JUnit5 + Mockito | Testing |

### Frontend

| Technology | Description |
|------------|-------------|
| Next.js 15 | Framework (App Router) |
| React 19 | UI library |
| TypeScript | Type safety |
| TailwindCSS | Styling |
| Shadcn UI | UI components |
| React Hook Form + Zod | Form handling |
| Axios | HTTP client |
| TanStack Query | Data fetching |
| Zustand | State management |
| Framer Motion | Animations |
| Lucide Icons | Icons |

---

## 🏗 Architecture

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

## 🔧 Prerequisites

- **Backend**: Java 21, Maven 3.9+, PostgreSQL 16
- **Frontend**: Node.js 18+, npm 9+
- **Docker** (optional): Docker Desktop

---

## 🚀 Getting Started

### Option 1: Using Docker Compose

```bash
# Build and run all services
docker-compose up --build

# Stop all services
docker-compose down
```

### Option 2: Manual Setup

#### Backend

```bash
# Navigate to backend directory
cd backend

# Build the project
mvn clean package

# Run locally
mvn spring-boot:run
```

The backend will start at: **http://localhost:8080**

#### Frontend

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Run development server
npm run dev
```

The frontend will start at: **http://localhost:3000**

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/todos` | Create new todo |
| GET | `/api/v1/todos` | Get todos with pagination/filtering |
| GET | `/api/v1/todos/{id}` | Get todo by ID |
| PUT | `/api/v1/todos/{id}` | Update todo |
| DELETE | `/api/v1/todos/{id}` | Delete todo (soft delete) |
| PATCH | `/api/v1/todos/{id}/complete` | Mark as completed |
| GET | `/api/v1/dashboard` | Get dashboard statistics |

### Swagger Documentation

Access the API documentation at: **http://localhost:8080/swagger-ui.html**

---

## 💾 Database Schema

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

- **Priority**: `LOW`, `MEDIUM`, `HIGH`
- **Status**: `TODO`, `IN_PROGRESS`, `COMPLETED`

---

## 📁 Project Structure

```
ToDoList/
├── backend/                 # Spring Boot application
│   ├── src/
│   │   └── main/
│   │       ├── java/      # Java source code
│   │       └── resources/
│   │           ├── application.properties
│   │           └── db/migration/  # Flyway migrations
│   ├── pom.xml
│   └── Dockerfile
├── frontend/               # Next.js application
│   ├── src/
│   │   ├── app/          # Pages
│   │   ├── components/   # UI components
│   │   ├── features/    # Feature modules
│   │   └── lib/         # Utilities
│   ├── package.json
│   ├── tailwind.config.ts
│   └── Dockerfile
├── docker-compose.yml
├── pom.xml                 # Parent POM
├── README.md
└── CLAUDE.md              # Project documentation
```

---

## 📝 Domain Rules

1. **Title Validation**: Title cannot be empty, max 255 characters
2. **Completed Todo Immutable**: Completed todos cannot be edited
3. **Due Date Validation**: Due date cannot be in the past
4. **Soft Delete**: Deleted todos are marked with `is_deleted = true`
5. **Complete Validation**: Only TODO or IN_PROGRESS status can be marked as completed

---

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report
```

---

## 🎨 UI/UX Design

### Color Palette

| Color | Hex | Usage |
|-------|-----|-------|
| Primary | `#6366f1` | Main actions, links |
| Secondary | `#8b5cf6` | Secondary elements |
| Success | `#22c55e` | Completed state |
| Warning | `#f59e0b` | Pending/in-progress |
| Error | `#ef4444` | Errors, delete actions |

### Responsive Breakpoints

- **Mobile**: < 640px
- **Tablet**: 640px - 1024px
- **Desktop**: > 1024px

---

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit your changes with descriptive messages
3. Push to remote: `git push origin feature/your-feature`
4. Create a pull request for review

---

## 📄 License

This project is for learning purposes.