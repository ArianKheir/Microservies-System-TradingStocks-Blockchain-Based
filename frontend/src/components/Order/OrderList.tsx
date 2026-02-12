import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Link } from 'react-router-dom';
import { RootState } from '../../store/store';
import { setOrders, setLoading } from '../../store/slices/orderSlice';
import { orderService } from '../../services/orderService';
import './OrderList.css';

const OrderList: React.FC = () => {
  const dispatch = useDispatch();
  const userId = useSelector((state: RootState) => state.auth.userId);
  const { orders, loading } = useSelector((state: RootState) => state.orders);

  useEffect(() => {
    const fetchOrders = async () => {
      if (userId) {
        dispatch(setLoading(true));
        try {
          const data = await orderService.getOrdersByUserId(userId);
          dispatch(setOrders(data));
        } catch (error) {
          console.error('Failed to fetch orders:', error);
        } finally {
          dispatch(setLoading(false));
        }
      }
    };

    fetchOrders();
  }, [userId, dispatch]);

  if (loading) {
    return <div>Loading orders...</div>;
  }

  return (
    <div className="order-list">
      <div className="order-list-header">
        <h2>My Orders</h2>
        <Link to="/dashboard/orders/new" className="new-order-button">New Order</Link>
      </div>
      <table className="order-table">
        <thead>
          <tr>
            <th>Symbol</th>
            <th>Type</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Status</th>
            <th>Filled</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => (
            <tr key={order.id}>
              <td>{order.stockSymbol}</td>
              <td className={order.orderType.toLowerCase()}>{order.orderType}</td>
              <td>{order.quantity}</td>
              <td>${order.price.toFixed(2)}</td>
              <td>{order.status}</td>
              <td>{order.filledQuantity}</td>
              <td>
                {order.createdAt
                  ? new Date(order.createdAt).toLocaleDateString()
                  : '-'}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default OrderList;

