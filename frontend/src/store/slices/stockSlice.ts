import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { Stock } from '../../types';

interface StockState {
  stocks: Stock[];
  selectedStock: Stock | null;
  loading: boolean;
}

const initialState: StockState = {
  stocks: [],
  selectedStock: null,
  loading: false,
};

const stockSlice = createSlice({
  name: 'stocks',
  initialState,
  reducers: {
    setStocks: (state, action: PayloadAction<Stock[]>) => {
      state.stocks = action.payload;
    },
    setSelectedStock: (state, action: PayloadAction<Stock | null>) => {
      state.selectedStock = action.payload;
    },
    updateStockPrice: (state, action: PayloadAction<{ symbol: string; price: number }>) => {
      const stock = state.stocks.find(s => s.symbol === action.payload.symbol);
      if (stock) {
        stock.currentPrice = action.payload.price;
      }
      if (state.selectedStock?.symbol === action.payload.symbol) {
        state.selectedStock.currentPrice = action.payload.price;
      }
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload;
    },
  },
});

export const { setStocks, setSelectedStock, updateStockPrice, setLoading } = stockSlice.actions;
export default stockSlice.reducer;

