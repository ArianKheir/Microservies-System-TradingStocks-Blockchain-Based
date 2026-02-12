export interface User {
  id: number;
  email: string;
  firstName?: string;
  lastName?: string;
  ethereumAddress?: string;
}

export interface Stock {
  id: number;
  symbol: string;
  name: string;
  currentPrice: number;
  totalSupply?: number;
  marketCap?: number;
}

export interface Order {
  id: number;
  userId: number;
  stockSymbol: string;
  orderType: 'BUY' | 'SELL';
  quantity: number;
  price: number;
  status: 'PENDING' | 'MATCHED' | 'COMPLETED' | 'CANCELLED' | 'PARTIALLY_FILLED';
  filledQuantity: number;
  createdAt: string;
}

export interface Portfolio {
  id: number;
  userId: number;
  totalValue: number;
}

export interface Holding {
  id: number;
  portfolioId: number;
  stockSymbol: string;
  quantity: number;
  averageBuyPrice: number;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  userId: number;
  email: string;
}

