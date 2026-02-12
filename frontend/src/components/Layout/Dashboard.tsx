import React from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../store/store';
import { clearAuth } from '../../store/slices/authSlice';
import { authService } from '../../services/authService';
import './Dashboard.css';

const Dashboard: React.FC = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const userId = useSelector((state: RootState) => state.auth.userId);

  const handleLogout = () => {
    authService.logout();
    dispatch(clearAuth());
    navigate('/login');
  };

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Stock Brokerage Platform</h1>
        <div className="header-actions">
          <span>User ID: {userId}</span>
          <button onClick={handleLogout}>Logout</button>
        </div>
      </header>
      <div className="dashboard-content">
        <nav className="sidebar">
          <Link to="/dashboard/stocks">Stocks</Link>
          <Link to="/dashboard/orders">Orders</Link>
          <Link to="/dashboard/portfolio">Portfolio</Link>
        </nav>
        <main className="main-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default Dashboard;

