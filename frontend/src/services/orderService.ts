import api from './api';
import { Order } from '../types';

export const orderService = {
  createOrder: async (order: Partial<Order>) => {
    const response = await api.post<Order>('/api/orders', order);
    return response.data;
  },

  getOrderById: async (orderId: number) => {
    const response = await api.get<Order>(`/api/orders/${orderId}`);
    return response.data;
  },

  getOrdersByUserId: async (userId: number) => {
    const response = await api.get<Order[]>(`/api/orders/user/${userId}`);
    return response.data;
  },

  cancelOrder: async (orderId: number) => {
    await api.delete(`/api/orders/${orderId}`);
  },
};

