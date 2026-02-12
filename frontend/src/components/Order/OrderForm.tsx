import React, { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { RootState } from '../../store/store';
import { orderService } from '../../services/orderService';
import './OrderForm.css';

const OrderForm: React.FC = () => {
  const [searchParams] = useSearchParams();
  const urlStockId = searchParams.get('stockId') || '';
  const urlSymbol = searchParams.get('symbol') || '';
  const urlPrice = searchParams.get('price') || '';
  
  const [stockId, setStockId] = useState(urlStockId);
  const [stockSymbol, setStockSymbol] = useState(urlSymbol);
  const [orderType, setOrderType] = useState<'BUY' | 'SELL'>('BUY');
  const [quantity, setQuantity] = useState('');
  const [price, setPrice] = useState(urlPrice);
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const userId = useSelector((state: RootState) => state.auth.userId);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!userId || !stockId) {
      setError('Missing required information');
      return;
    }

    try {
      await orderService.createOrder({
        userId,
        // Backend expects stockSymbol, not stockId
        stockSymbol,
        orderType,
        quantity: parseInt(quantity),
        price: parseFloat(price),
      });
      navigate('/dashboard/orders');
    } catch (err) {
      setError('Failed to create order');
      console.error(err);
    }
  };

  return (
    <div className="order-form">
      <h2>Create Order</h2>
      {error && <div className="error">{error}</div>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Stock ID:</label>
          <input
            type="number"
            value={stockId}
            onChange={(e) => setStockId(e.target.value)}
            required
          />
        </div>
        
        <div className="form-group">
          <label>Stock Symbol (info only):</label>
          <input
            type="text"
            value={stockSymbol}
            onChange={(e) => setStockSymbol(e.target.value)}
            placeholder="e.g., AAPL"
          />
        </div>

        <div className="form-group">
          <label>Order Type:</label>
          <select value={orderType} onChange={(e) => setOrderType(e.target.value as 'BUY' | 'SELL')}>
            <option value="BUY">Buy</option>
            <option value="SELL">Sell</option>
          </select>
        </div>

        <div className="form-group">
          <label>Quantity:</label>
          <input
            type="number"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value)}
            min="1"
            required
          />
        </div>

        <div className="form-group">
          <label>Price per Share:</label>
          <input
            type="number"
            step="0.01"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            required
          />
        </div>

        <button type="submit">Place Order</button>
        <button type="button" onClick={() => navigate('/dashboard/orders')}>
          Cancel
        </button>
      </form>
    </div>
  );
};

export default OrderForm;
