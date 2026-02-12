CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    stock_symbol VARCHAR(10) NOT NULL,
    order_type VARCHAR(10) NOT NULL,
    quantity BIGINT NOT NULL,
    price DECIMAL(20,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    filled_quantity BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_symbol ON orders(stock_symbol);

