## Portfolio Service

The **Portfolio Service** tracks user holdings, cash balances, and performance metrics in the Stock Brokerage Platform.

### Responsibilities

- Maintain up-to-date portfolios and positions for each user.
- React to order executions and blockchain transaction confirmations.
- Calculate current valuations and basic performance indicators.
- Expose read APIs used by the frontend dashboard and reporting views.

### Technology Stack

- Java, Spring Boot.
- PostgreSQL database with Flyway migrations.
- RabbitMQ for consuming domain events (orders, transactions).

### Key Endpoints (via API Gateway)

- `GET /api/portfolios` – list portfolios for the authenticated user.
- `GET /api/portfolios/{id}` – detailed view of a specific portfolio.
- `GET /api/portfolios/{id}/positions` – list individual positions.
- `GET /api/portfolios/{id}/performance` – basic performance/time-series data (if implemented).

Refer to the API Gateway Swagger UI for the authoritative contract.

### Configuration

Configuration is kept in `src/main/resources/application.yml`:

- Database connection properties.
- RabbitMQ connection details and event queue bindings.
- Any valuation-related configuration (e.g., which price sources to prefer).

Sensitive values (passwords, secrets) are provided via environment variables or Kubernetes secrets in non-local environments.

### Running Locally

```bash
cd backend/portfolio-service
mvn spring-boot:run
```

For realistic data, the service should be run alongside Order, Transaction, Stock services and the messaging infrastructure.

