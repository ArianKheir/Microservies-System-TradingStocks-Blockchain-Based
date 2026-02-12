## API Gateway

The **API Gateway** is the single entry point for all external clients of the Stock Brokerage Platform.

### Responsibilities

- Route and load-balance incoming HTTP requests to the appropriate microservices.
- Perform authentication and basic authorization using JWT tokens.
- Centralize cross-cutting concerns such as CORS, rate limiting, and request logging.
- Provide a consolidated Swagger/OpenAPI view of the platform APIs (where configured).

### Technology Stack

- Java, Spring Boot.
- Spring Cloud Gateway.
- Spring Security with JWT support.

### Routing Overview

Typical routes (subject to configuration):

- `/api/users/**` → User Service.
- `/api/stocks/**` → Stock Service.
- `/api/orders/**` → Order Service.
- `/api/transactions/**` → Transaction Service.
- `/api/portfolios/**` → Portfolio Service.
- `/api/notifications/**` → Notification Service.

Routes and filters are configured in `src/main/resources/application.yml`.

### API Documentation

The gateway usually exposes consolidated API documentation at:

- `http://<gateway-host>:8080/swagger-ui.html`

Depending on configuration, per-service Swagger UIs may also be directly reachable.

### Configuration

Key configuration in `application.yml`:

- Route definitions and filters.
- JWT secret keys / public keys (for token validation).
- CORS settings.

In non-local environments, sensitive values (keys, secrets) must be injected via environment variables or Kubernetes secrets.

### Running Locally

```bash
cd backend/api-gateway
mvn spring-boot:run
```

Ensure Eureka and the target microservices are running to resolve routes successfully.

