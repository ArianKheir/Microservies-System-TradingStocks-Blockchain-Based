## User Service

The **User Service** is responsible for authentication, authorization, and user account management for the Stock Brokerage Platform.

### Responsibilities

- Manage user registration and login.
- Issue and validate JWT access tokens.
- Maintain user profiles and (optionally) KYC-related data.
- Expose user-facing and admin-facing endpoints via the API Gateway.

### Technology Stack

- Java, Spring Boot, Spring Web, Spring Security.
- JWT-based authentication.
- PostgreSQL database with Flyway migrations.
- RabbitMQ (if user-related events are published).

### Key Endpoints (via API Gateway)

- `POST /api/users/auth/register` – register a new user.
- `POST /api/users/auth/login` – authenticate and obtain a token.
- `GET /api/users/me` – fetch current user profile (authenticated).

Refer to the gateway Swagger UI for the latest paths and models.

### Configuration

Main configuration is in `src/main/resources/application.yml`. Common settings:

- Database connection (URL, username, password).
- JWT secret / signing key and token expiration.
- RabbitMQ connection (if used).

These values are typically provided via environment variables in non-local environments.

### Running Locally

```bash
cd backend/user-service
mvn spring-boot:run
```

When running as part of the full stack, use Docker and/or Kubernetes as described in the root `README.md`.

