CREATE TABLE blockchain_transactions (
    id BIGSERIAL PRIMARY KEY,
    tx_hash VARCHAR(66) UNIQUE NOT NULL,
    from_address VARCHAR(42) NOT NULL,
    to_address VARCHAR(42) NOT NULL,
    stock_symbol VARCHAR(10) NOT NULL,
    quantity BIGINT NOT NULL,
    price DECIMAL(20,2) NOT NULL,
    block_number BIGINT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20)
);

CREATE INDEX idx_tx_hash ON blockchain_transactions(tx_hash);
CREATE INDEX idx_from_address ON blockchain_transactions(from_address);
CREATE INDEX idx_to_address ON blockchain_transactions(to_address);

