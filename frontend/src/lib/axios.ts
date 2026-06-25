import axios from 'axios';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1';

export const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

// Request interceptor to add auth token
apiClient.interceptors.request.use(
  (config) => {
    if (typeof window !== 'undefined') {
      // Try to get token from zustand persist storage
      let token: string | null = null;

      const authData = localStorage.getItem('auth-storage');
      if (authData) {
        try {
          const parsed = JSON.parse(authData);
          // Zustand persist stores as { state: {...} }
          token = parsed?.state?.user?.token || parsed?.user?.token || null;
        } catch (e) {
          // Try direct storage
        }
      }

      // Also check for direct token storage
      if (!token) {
        token = localStorage.getItem('auth_token');
      }

      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // Server responded with error
      const data = error.response.data;
      console.error('API Error:', data || error.response.statusText);
      // Enhance error with message from server
      if (data?.message) {
        error.message = data.message;
      } else if (data?.errorCode) {
        error.message = data.message || data.errorCode;
      } else {
        error.message = error.response.statusText || 'Có lỗi xảy ra';
      }
    } else if (error.request) {
      // Request made but no response
      console.error('Network Error:', error.message);
      error.message = 'Không thể kết nối đến server';
    }
    return Promise.reject(error);
  }
);

export default apiClient;