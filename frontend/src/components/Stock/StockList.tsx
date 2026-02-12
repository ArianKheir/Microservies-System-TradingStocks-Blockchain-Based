import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import { RootState } from '../../store/store';
import { setStocks, setLoading } from '../../store/slices/stockSlice';
import { stockService } from '../../services/stockService';
import './StockList.css';

const StockList: React.FC = () => {
  const dispatch = useDispatch();
  const { stocks, loading } = useSelector((state: RootState) => state.stocks);

  useEffect(() => {
    const fetchStocks = async () => {
      dispatch(setLoading(true));
      try {
        const data = await stockService.getAllStocks();
        dispatch(setStocks(data));
      } catch (error) {
        console.error('Failed to fetch stocks:', error);
      } finally {
        dispatch(setLoading(false));
      }
    };

    fetchStocks();
  }, [dispatch]);

  if (loading) {
    return <div>Loading stocks...</div>;
  }

  return (
    <div className="stock-list">
      <h2>Available Stocks</h2>
      <table className="stock-table">
        <thead>
          <tr>
            <th>Symbol</th>
            <th>Name</th>
            <th>Price</th>
            <th>Market Cap</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {stocks.map((stock) => (
            <tr key={stock.id}>
              <td>{stock.symbol}</td>
              <td>{stock.name}</td>
              <td>${stock.currentPrice.toFixed(2)}</td>
              <td>{stock.marketCap ? `$${stock.marketCap.toLocaleString()}` : 'N/A'}</td>
              <td>
                <Link to={`/dashboard/stocks/${stock.symbol}`}>View</Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default StockList;

