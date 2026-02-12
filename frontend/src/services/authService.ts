import axios from 'axios';
import api from './api';
import { AuthResponse } from '../types';

function getErrorMessage(err: unknown): string {
  if (axios.isAxiosError(err) && err.response?.data?.message) {
    return err.response.data.message;
  }
  if (err instanceof Error) return err.message;
  return 'Request failed';
}

export const authService = {
  register: async (email: string, password: string, firstName?: string, lastName?: string) => {
    try {
      const response = await api.post<AuthResponse>('/api/auth/register', {
        email,
        password,
        firstName,
        lastName,
      });
      if (response.data.accessToken) {
        localStorage.setItem('accessToken', response.data.accessToken);
        localStorage.setItem('refreshToken', response.data.refreshToken);
        localStorage.setItem('userId', response.data.userId.toString());
      }
      return response.data;
    } catch (err) {
      throw new Error(getErrorMessage(err));
    }
  },

  login: async (email: string, password: string) => {
    try {
      const response = await api.post<AuthResponse>('/api/auth/login', {
        email,
        password,
      });
      if (response.data.accessToken) {
        localStorage.setItem('accessToken', response.data.accessToken);
        localStorage.setItem('refreshToken', response.data.refreshToken);
        localStorage.setItem('userId', response.data.userId.toString());
      }
      return response.data;
    } catch (err) {
      throw new Error(getErrorMessage(err));
    }
  },

  logout: () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userId');
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('accessToken');
  },
};

