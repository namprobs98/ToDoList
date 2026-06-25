// Todo types matching the backend API

export type Priority = 'LOW' | 'MEDIUM' | 'HIGH';
export type Status = 'TODO' | 'IN_PROGRESS' | 'COMPLETED';

export interface Todo {
  id: string;
  title: string;
  description: string | null;
  priority: Priority;
  status: Status;
  dueDate: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateTodoRequest {
  title: string;
  description?: string;
  priority?: Priority;
  dueDate?: string;
}

export interface UpdateTodoRequest {
  title?: string;
  description?: string;
  priority?: Priority;
  status?: Status;
  dueDate?: string;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
  errorCode?: string;
}

export interface DashboardData {
  totalTodos: number;
  completedTodos: number;
  inProgressTodos: number;
  overdueTodos: number;
  todosByStatus: { status: Status; count: number }[];
  todosByPriority: { priority: Priority; count: number }[];
}

export interface TodoFilters {
  status?: Status;
  priority?: Priority;
  keyword?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: 'asc' | 'desc';
}