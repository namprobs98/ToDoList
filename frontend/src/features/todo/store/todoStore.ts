import { create } from 'zustand';
import { Todo, TodoFilters, Status, Priority } from '../types/todo';

interface TodoStore {
  // Filters
  filters: TodoFilters;
  setFilters: (filters: Partial<TodoFilters>) => void;
  resetFilters: () => void;

  // Selected todo
  selectedTodo: Todo | null;
  setSelectedTodo: (todo: Todo | null) => void;

  // UI State
  isCreateModalOpen: boolean;
  setCreateModalOpen: (open: boolean) => void;
  isEditModalOpen: boolean;
  setEditModalOpen: (open: boolean) => void;
  isDetailOpen: boolean;
  setDetailOpen: (open: boolean) => void;
}

const defaultFilters: TodoFilters = {
  page: 0,
  size: 10,
  sortBy: 'createdAt',
  sortDir: 'desc',
};

export const useTodoStore = create<TodoStore>((set) => ({
  // Filters
  filters: defaultFilters,
  setFilters: (newFilters) =>
    set((state) => ({
      filters: { ...state.filters, ...newFilters },
    })),
  resetFilters: () => set({ filters: defaultFilters }),

  // Selected todo
  selectedTodo: null,
  setSelectedTodo: (todo) => set({ selectedTodo: todo }),

  // UI State
  isCreateModalOpen: false,
  setCreateModalOpen: (open) => set({ isCreateModalOpen: open }),
  isEditModalOpen: false,
  setEditModalOpen: (open) => set({ isEditModalOpen: open }),
  isDetailOpen: false,
  setDetailOpen: (open) => set({ isDetailOpen: open }),
}));

export default useTodoStore;