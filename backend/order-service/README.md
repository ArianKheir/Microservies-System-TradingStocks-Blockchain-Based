## Order Service

The **Order Service** manages the full lifecycle of trading orders within the Stock Brokerage Platform.

### Responsibilities

- Accept and validate buy/sell orders from authenticated users (via the API Gateway).
- Apply business rules and risk checks (balance, instrument availability, etc.).
- Match compatible orders and create executions.
- Persist orders, executions, and order events.
- Publish domain events (e.g. `OrderPlaced`, `OrderMatched`, `OrderCancelled`) to RabbitMQ for downstream services (Portfolio, Transaction, Notification).

### Technology Stack

- Java, Spring Boot, Spring Web.
- PostgreSQL database with Flyway migrations.
- RabbitMQ for event-driven communication.

### Key Endpoints (via API Gateway)

- `POST /api/orders` – place a new buy/sell order.
- `GET /api/orders` – list orders for the authenticated user.
- `GET /api/orders/{id}` – get details for a specific order.
- `POST /api/orders/{id}/cancel` – request order cancellation (if supported).

Refer to the API Gateway Swagger UI for authoritative endpoint definitions.

### Configuration

Configuration is defined in `src/main/resources/application.yml`:

- Database connection properties.
- RabbitMQ host, credentials, exchanges, and queues.
- Any matching/engine-specific settings and thresholds.

Secrets (DB credentials, RabbitMQ passwords, etc.) should be provided via environment variables or external secrets in non-local environments.

### Running Locally

```bash
cd backend/order-service
mvn spring-boot:run
```

Make sure the User, Stock, and infrastructure services (PostgreSQL, RabbitMQ) are available when exercising full order flows.

