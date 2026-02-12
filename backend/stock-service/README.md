## Stock Service

The **Stock Service** provides stock reference data, current prices, and historical price information for the Stock Brokerage Platform.

### Responsibilities

- Manage stock instruments and related metadata.
- Expose endpoints to query available stocks and their prices.
- Maintain basic price history and support scheduled price updates.
- Publish price-related events to other services via RabbitMQ (if configured).

### Technology Stack

- Java, Spring Boot, Spring Web.
- PostgreSQL database with Flyway migrations.
- RabbitMQ for messaging.
- Scheduled tasks for price updates.

### Key Endpoints (via API Gateway)

- `GET /api/stocks` – list all available stocks.
- `GET /api/stocks/{symbol}` – get details and latest price for a single stock.
- `GET /api/stocks/{symbol}/history` – get historical price data (if exposed).

Refer to the gateway Swagger UI for the exact paths and response models.

### Configuration

Configuration is defined in `src/main/resources/application.yml`:

- Database connection properties.
- RabbitMQ connection and exchange/queue names.
- Scheduler configuration for price updates.

Environment-specific overrides are provided via environment variables or external configuration in staging/production.

### Running Locally

```bash
cd backend/stock-service
mvn spring-boot:run
```

Ensure PostgreSQL and RabbitMQ are available (e.g., via `docker-compose`) before starting the service.

