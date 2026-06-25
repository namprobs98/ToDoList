import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { AuthResponse } from '../types/auth';

interface AuthStore {
  // Auth state
  user: AuthResponse | null;
  isAuthenticated: boolean;

  // Actions
  setAuth: (user: AuthResponse) => void;
  logout: () => void;
  initialize: () => void;
}

export const useAuthStore = create<AuthStore>()(
  persist(
    (set) => ({
      user: null,
      isAuthenticated: false,

      setAuth: (user) =>
        set({
          user,
          isAuthenticated: true,
        }),

      logout: () =>
        set({
          user: null,
          isAuthenticated: false,
        }),

      initialize: () => {
        // Initialization handled by persist middleware
      },
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        user: state.user,
        isAuthenticated: state.isAuthenticated
      }),
    }
  )
);

export default useAuthStore;