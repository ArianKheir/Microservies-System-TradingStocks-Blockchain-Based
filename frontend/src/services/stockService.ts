import api from './api';
import { Stock } from '../types';

export const stockService = {
  getAllStocks: async () => {
    const response = await api.get<Stock[]>('/api/stocks');
    return response.data;
  },

  getStockBySymbol: async (symbol: string) => {
    const response = await api.get<Stock>(`/api/stocks/${symbol}`);
    return response.data;
  },
  
  getStockById: async (id: number) => {
    const response = await api.get(`/stocks/${id}`);
    return response.data;
  },  

  searchStocks: async (name?: string, symbol?: string) => {
    const params = new URLSearchParams();
    if (name) params.append('name', name);
    if (symbol) params.append('symbol', symbol);
    const response = await api.get<Stock[]>(`/api/stocks?${params.toString()}`);
    return response.data;
  },
};

