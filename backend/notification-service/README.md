## Notification Service

The **Notification Service** delivers real-time and asynchronous notifications to users of the Stock Brokerage Platform.

### Responsibilities

- Subscribe to domain events (orders, transactions, portfolio updates, price alerts) via RabbitMQ.
- Push real-time updates to clients using WebSockets.
- Optionally integrate with email/SMS or other external notification channels via adapters.

### Technology Stack

- Java, Spring Boot.
- WebSocket/STOMP (or similar) for real-time delivery.
- RabbitMQ for event intake.

### Key Endpoints (via API Gateway)

- WebSocket endpoint (example): `ws://<gateway-host>/api/notifications/ws` – used by the frontend to subscribe to channels.
- REST endpoints for notification preferences (if implemented), for example:
  - `GET /api/notifications` – list recent notifications.
  - `POST /api/notifications/preferences` – update user notification preferences.

Consult the Gateway’s Swagger UI and the service configuration for exact paths.

### Configuration

Core settings are defined in `src/main/resources/application.yml`:

- RabbitMQ host, credentials, and queues for the different event types.
- WebSocket endpoint configuration and allowed origins.

Environment-specific overrides and credentials are supplied via environment variables or Kubernetes secrets.

### Running Locally

```bash
cd backend/notification-service
mvn spring-boot:run
```

To see real activity, run the Notification Service alongside the other microservices and the frontend, then perform trading actions from the UI.

