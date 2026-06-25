-- V1__create_todo_table.sql
-- Flyway migration script for Todo table

CREATE TABLE IF NOT EXISTS todo (
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

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_todo_status ON todo(status);
CREATE INDEX IF NOT EXISTS idx_todo_priority ON todo(priority);
CREATE INDEX IF NOT EXISTS idx_todo_is_deleted ON todo(is_deleted);
CREATE INDEX IF NOT EXISTS idx_todo_due_date ON todo(due_date);
CREATE INDEX IF NOT EXISTS idx_todo_created_at ON todo(created_at);

-- Insert sample data for development
INSERT INTO todo (id, title, description, priority, status, due_date, created_at, updated_at, is_deleted) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Learn Spring Boot', 'Complete Spring Boot tutorial', 'HIGH', 'IN_PROGRESS', '2026-07-01 10:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('b1ffc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Build React App', 'Create a modern React application', 'MEDIUM', 'TODO', '2026-07-15 10:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('c2ggc99-9c0b-4ef8-bb6d-6bb9bd380a33', 'Write Unit Tests', 'Add comprehensive unit tests', 'LOW', 'TODO', '2026-07-20 10:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('d3hhc99-9c0b-4ef8-bb6d-6bb9bd380a44', 'Deploy to Production', 'Setup CI/CD pipeline and deploy', 'HIGH', 'TODO', '2026-07-25 10:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
('e4iic99-9c0b-4ef8-bb6d-6bb9bd380a55', 'Code Review', 'Review pull requests', 'MEDIUM', 'COMPLETED', '2026-06-20 10:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);