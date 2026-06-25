import apiClient from '@/lib/axios';
import {
  Todo,
  CreateTodoRequest,
  UpdateTodoRequest,
  PageResponse,
  ApiResponse,
  DashboardData,
  TodoFilters,
} from '../types/todo';

export const todoApi = {
  // Create a new todo
  create: async (data: CreateTodoRequest): Promise<Todo> => {
    const response = await apiClient.post<ApiResponse<Todo>>('/todos', data);
    return response.data.data;
  },

  // Get all todos with pagination and filters
  getAll: async (filters: TodoFilters = {}): Promise<PageResponse<Todo>> => {
    const params = new URLSearchParams();
    if (filters.page !== undefined) params.append('page', String(filters.page));
    if (filters.size !== undefined) params.append('size', String(filters.size));
    if (filters.status) params.append('status', filters.status);
    if (filters.priority) params.append('priority', filters.priority);
    if (filters.keyword) params.append('keyword', filters.keyword);
    if (filters.sortBy) params.append('sortBy', filters.sortBy);
    if (filters.sortDir) params.append('sortDir', filters.sortDir);

    const response = await apiClient.get<ApiResponse<PageResponse<Todo>>>(`/todos?${params}`);
    return response.data.data;
  },

  // Get a single todo by ID
  getById: async (id: string): Promise<Todo> => {
    const response = await apiClient.get<ApiResponse<Todo>>(`/todos/${id}`);
    return response.data.data;
  },

  // Update a todo
  update: async (id: string, data: UpdateTodoRequest): Promise<Todo> => {
    const response = await apiClient.put<ApiResponse<Todo>>(`/todos/${id}`, data);
    return response.data.data;
  },

  // Delete a todo
  delete: async (id: string): Promise<void> => {
    await apiClient.delete(`/todos/${id}`);
  },

  // Mark a todo as completed
  complete: async (id: string): Promise<Todo> => {
    const response = await apiClient.patch<ApiResponse<Todo>>(`/todos/${id}/complete`);
    return response.data.data;
  },

  // Get dashboard data
  getDashboard: async (): Promise<DashboardData> => {
    const response = await apiClient.get<ApiResponse<DashboardData>>('/dashboard');
    return response.data.data;
  },
};

export default todoApi;