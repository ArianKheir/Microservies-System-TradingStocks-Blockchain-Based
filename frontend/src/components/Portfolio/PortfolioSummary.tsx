import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '../../store/store';
import { setPortfolio, setHoldings, setLoading } from '../../store/slices/portfolioSlice';
import { portfolioService } from '../../services/portfolioService';
import './PortfolioSummary.css';

const PortfolioSummary: React.FC = () => {
  const dispatch = useDispatch();
  const userId = useSelector((state: RootState) => state.auth.userId);
  const { portfolio, holdings, loading } = useSelector((state: RootState) => state.portfolio);

  useEffect(() => {
    const fetchPortfolio = async () => {
      if (userId) {
        dispatch(setLoading(true));
        try {
          const portfolioData = await portfolioService.getPortfolio(userId);
          const holdingsData = await portfolioService.getHoldings(userId);
          dispatch(setPortfolio(portfolioData));
          dispatch(setHoldings(holdingsData));
        } catch (error) {
          console.error('Failed to fetch portfolio:', error);
        } finally {
          dispatch(setLoading(false));
        }
      }
    };

    fetchPortfolio();
  }, [userId, dispatch]);

  if (loading) {
    return <div>Loading portfolio...</div>;
  }

  return (
    <div className="portfolio-summary">
      <h2>Portfolio Summary</h2>
      {portfolio && (
        <div className="portfolio-value">
          <label>Total Value:</label>
          <span className="value">${portfolio.totalValue.toFixed(2)}</span>
        </div>
      )}
      <h3>Holdings</h3>
      <table className="holdings-table">
        <thead>
          <tr>
            <th>Symbol</th>
            <th>Quantity</th>
            <th>Average Buy Price</th>
            <th>Total Value</th>
          </tr>
        </thead>
        <tbody>
          {holdings.map((holding) => (
            <tr key={holding.id}>
              <td>{holding.stockSymbol}</td>
              <td>{holding.quantity}</td>
              <td>${holding.averageBuyPrice.toFixed(2)}</td>
              <td>${(holding.quantity * holding.averageBuyPrice).toFixed(2)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default PortfolioSummary;

