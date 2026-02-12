import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { RootState } from './store/store';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import Dashboard from './components/Layout/Dashboard';
import StockList from './components/Stock/StockList';
import StockDetail from './components/Stock/StockDetail';
import OrderForm from './components/Order/OrderForm';
import OrderList from './components/Order/OrderList';
import PortfolioSummary from './components/Portfolio/PortfolioSummary';
import './App.css';

function App() {
  const isAuthenticated = useSelector((state: RootState) => state.auth.isAuthenticated);

  return (
    <Routes>
      {/* Public Routes */}
      <Route 
        path="/login" 
        element={isAuthenticated ? <Navigate to="/dashboard" replace /> : <Login />} 
      />
      <Route 
        path="/register" 
        element={isAuthenticated ? <Navigate to="/dashboard" replace /> : <Register />} 
      />
      
      {/* Protected Dashboard Routes */}
      <Route 
        path="/dashboard" 
        element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" replace />}
      >
        {/* THIS IS THE KEY FIX - Default child route */}
        <Route index element={<Navigate to="stocks" replace />} />
        
        {/* Stocks Routes */}
        <Route path="stocks" element={<StockList />} />
        {/* Use stock symbol (e.g. GOOGL) in the URL */}
        <Route path="stocks/:symbol" element={<StockDetail />} />
        
        {/* Orders Routes */}
        <Route path="orders" element={<OrderList />} />
        <Route path="orders/new" element={<OrderForm />} />
        
        {/* Portfolio Route */}
        <Route path="portfolio" element={<PortfolioSummary />} />
      </Route>
      
      {/* Root redirect */}
      <Route 
        path="/" 
        element={<Navigate to={isAuthenticated ? "/dashboard" : "/login"} replace />} 
      />
    </Routes>
  );
}

export default App;
