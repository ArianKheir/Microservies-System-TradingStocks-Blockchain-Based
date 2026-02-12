import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { stockService } from '../../services/stockService';
import { Stock } from '../../types';
import './StockDetail.css';

const StockDetail: React.FC = () => {
  // Always derive the stock to load from the route, not from Redux state
  const { symbol } = useParams<{ symbol: string }>();

  const [stock, setStock] = useState<Stock | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchStock = async () => {
      if (!symbol) {
        setStock(null);
        setError('No stock symbol provided in URL.');
        return;
      }

      setLoading(true);
      setError(null);
      try {
        const data = await stockService.getStockBySymbol(symbol);
        setStock(data);
      } catch (err) {
        console.error('[StockDetail] Failed to fetch stock:', err);
        setStock(null);
        setError('Stock not found or failed to load.');
      } finally {
        setLoading(false);
      }
    };

    fetchStock();
  }, [symbol]);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error || !stock) {
    return <div>{error || 'Stock not found'}</div>;
  }

  return (
    <div className="stock-detail">
      <h2>
        {stock.name} ({stock.symbol})
      </h2>
      <div className="stock-info">
        <p>
          <strong>Current Price:</strong> ${stock.currentPrice}
        </p>
        <p>
          <strong>Total Supply:</strong> {stock.totalSupply}
        </p>
        <p>
          <strong>Market Cap:</strong> ${stock.marketCap}
        </p>
      </div>
      <div className="actions">
        <Link
          to={`/dashboard/orders/new?stockId=${stock.id}&symbol=${stock.symbol}&price=${stock.currentPrice}`}
        >
          <button>Buy Stock</button>
        </Link>
        <Link to="/dashboard/stocks">
          <button>Back to Stocks</button>
        </Link>
      </div>
    </div>
  );
};

export default StockDetail;
