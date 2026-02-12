import api from './api';
import { Portfolio, Holding } from '../types';

export const portfolioService = {
  getPortfolio: async (userId: number) => {
    const response = await api.get<Portfolio>(`/api/portfolio/${userId}`);
    return response.data;
  },

  getHoldings: async (userId: number) => {
    const response = await api.get<Holding[]>(`/api/portfolio/${userId}/holdings`);
    return response.data;
  },

  getPerformance: async (userId: number) => {
    const response = await api.get<number>(`/api/portfolio/${userId}/performance`);
    return response.data;
  },
};

