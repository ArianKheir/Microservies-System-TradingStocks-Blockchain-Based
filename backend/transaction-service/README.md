## Transaction Service

The **Transaction Service** is the bridge between the trading domain and the blockchain layer. It records executed trades on the private Ethereum network and maintains an auditable transaction history.

### Responsibilities

- Consume executed trade events from the Order Service via RabbitMQ.
- Interact with Solidity smart contracts deployed on the private Ethereum network.
- Persist blockchain transaction metadata (hash, block number, status) in PostgreSQL.
- Expose read APIs for transaction history and status lookups.

### Technology Stack

- Java, Spring Boot.
- Web3 integration (via Web3j or similar) for Ethereum interaction.
- PostgreSQL database with Flyway migrations.
- RabbitMQ for inbound trade events.

### Key Endpoints (via API Gateway)

- `GET /api/transactions` – list blockchain-recorded transactions (filtered by user or order).
- `GET /api/transactions/{id}` – show details for a single transaction.
- `GET /api/transactions/status/{txHash}` – check blockchain transaction status (if exposed).

Refer to the API Gateway Swagger UI for precise paths and parameters.

### Configuration

Key configuration in `src/main/resources/application.yml`:

- Database connection properties.
- RabbitMQ connection and queue bindings for trade events.
- Blockchain configuration:
  - Ethereum node RPC URL (e.g., Geth/Hardhat).
  - Contract addresses for the ledger/settlement contracts.
  - Wallet/private key details (provided securely via environment variables or secrets).

### Running Locally

```bash
cd backend/transaction-service
mvn spring-boot:run
```

For full functionality, ensure:

- RabbitMQ is running and receiving trade events from the Order Service.
- The private Ethereum node and smart contracts are deployed (see `blockchain/` module).

