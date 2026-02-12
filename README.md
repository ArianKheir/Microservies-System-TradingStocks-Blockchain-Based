# Stock Brokerage Platform - Full-Stack Implementation

A complete online stock brokerage trading platform with microservices architecture, blockchain integration, real-time notifications, and comprehensive monitoring.

## Architecture

- **Microservices**: 8 Spring Boot services with Spring Cloud
- **Frontend**: React 18+ with TypeScript
- **Database**: PostgreSQL 15+ (separate DB per service)
- **Message Broker**: RabbitMQ
- **Blockchain**: Private Ethereum network with Solidity smart contracts
- **Service Discovery**: Eureka
- **Monitoring**: Prometheus + Grafana
- **Containerization**: Docker & Docker Compose
- **Orchestration**: Kubernetes

## Services

1. **API Gateway** (8080) - Spring Cloud Gateway with JWT validation
2. **User Service** (8081) - Authentication and user management
3. **Stock Service** (8082) - Stock listings and price management
4. **Order Service** (8083) - Order management and matching engine
5. **Transaction Service** (8084) - Blockchain transaction recording
6. **Portfolio Service** (8085) - User portfolio management
7. **Notification Service** (8086) - WebSocket real-time notifications
8. **Eureka Server** (8761) - Service discovery

## Quick Start

### Prerequisites

- Java 17+
- Node.js 18+
- Docker & Docker Compose
- Maven 3.8+

### Local Development

1. **Start Infrastructure**:
```bash
docker-compose up -d
```

2. **Build Backend Services**:
```bash
cd backend
mvn clean install
```

3. **Start Services** (in order):
   - Eureka Server
   - API Gateway
   - User Service
   - Stock Service
   - Order Service
   - Transaction Service
   - Portfolio Service
   - Notification Service

4. **Deploy Smart Contracts**:
```bash
cd blockchain
npm install
npx hardhat compile
npx hardhat deploy --network localhost
```

5. **Start Frontend**:
```bash
cd frontend
npm install
npm start
```

### Access Points

- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **RabbitMQ Management**: http://localhost:15672 (admin/admin)
- **Grafana**: http://localhost:3001 (admin/admin)
- **Prometheus**: http://localhost:9090
- **Frontend**: http://localhost:3000
- **Geth RPC**: http://localhost:8545

## API Documentation

Swagger UI available at: http://localhost:8080/swagger-ui.html

## Testing

### Backend Tests
```bash
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

### Smart Contract Tests
```bash
cd blockchain
npx hardhat test
```

## Production Deployment

See `kubernetes/` directory for Kubernetes manifests.

```bash
kubectl apply -f kubernetes/
```

## Project Structure

```
stock-brokerage-platform/
├── backend/              # Spring Boot microservices
├── frontend/             # React TypeScript application
├── blockchain/           # Solidity smart contracts
├── kubernetes/           # K8s deployment manifests
├── monitoring/           # Prometheus & Grafana configs
└── docker-compose.yml    # Local development setup
```

## License

MIT

---

## Comprehensive Documentation Overview

This repository contains not only the source code, but also **architecture diagrams**, **technical documentation**, and **API specifications** for all services. Use this section as an index to discover all relevant materials.

- **High-Level Documentation**
  - **System overview & requirements**: see `docs/README.md` (or root-level design docs) for business goals, use cases, and non-functional requirements.
  - **Decision records**: if present, see `docs/adr/` for key architectural decisions (e.g., technology choices, patterns, trade-offs).

- **Architecture Diagrams**
  - **Core diagrams are typically located under** `docs/diagrams/`:
    - **Context & Container diagrams** (C4-style) for the overall system and its microservices.
    - **Component diagrams** for each major service (User, Order, Portfolio, etc.).
    - **Sequence diagrams** for critical flows (user registration, place order, settle trade, portfolio update, notification push).
    - **Deployment diagrams** for local, staging, and production (Docker Compose, Kubernetes, monitoring stack).
  - Common formats:
    - `*.drawio` / `*.drawio.png` – editable Draw.io diagrams.
    - `*.png` / `*.svg` – rendered images for quick viewing.

- **API Specifications**
  - **Gateway-level OpenAPI/Swagger**:
    - Consolidated documentation exposed via the API Gateway at `http://localhost:8080/swagger-ui.html`.
  - **Per-service API docs** (if enabled per service):
    - `User Service`: `http://localhost:8081/swagger-ui.html`
    - `Stock Service`: `http://localhost:8082/swagger-ui.html`
    - `Order Service`: `http://localhost:8083/swagger-ui.html`
    - `Transaction Service`: `http://localhost:8084/swagger-ui.html`
    - `Portfolio Service`: `http://localhost:8085/swagger-ui.html`
    - `Notification Service`: `http://localhost:8086/swagger-ui.html` (for REST endpoints, if any)
  - For generated specs, look for:
    - `docs/apis/` for static OpenAPI JSON/YAML (e.g. `openapi-gateway.yaml`, `user-service-openapi.yaml`).

---

## Service Landscape (Detailed)

All backend services are Spring Boot microservices, following a **clean architecture** and **hexagonal** style where applicable. Each service is independently deployable, owns its data, and communicates primarily via HTTP (through the API Gateway) and RabbitMQ.

1. **API Gateway (Spring Cloud Gateway, port 8080)**
   - **Responsibilities**
     - Single entry point for all external clients.
     - Request routing, load balancing, and path-based routing to underlying services.
     - JWT validation and authentication/authorization checks.
     - Rate limiting, CORS configuration, and basic observability (request logs, metrics).
   - **Key Endpoints**
     - `/api/users/**` → User Service
     - `/api/stocks/**` → Stock Service
     - `/api/orders/**` → Order Service
     - `/api/portfolios/**` → Portfolio Service
     - `/api/notifications/**` → Notification Service

2. **User Service (port 8081)**
   - **Domain**
     - User accounts, authentication, authorization, KYC profile data.
   - **Capabilities**
     - Registration, login, password management.
     - JWT issuing/refresh.
     - Role and permission management (e.g., admin vs. retail user).
   - **Data**
     - `users`, `roles`, `sessions` tables in a dedicated PostgreSQL schema/DB.

3. **Stock Service (port 8082)**
   - **Domain**
     - Reference data for stocks, instruments, and market metadata.
   - **Capabilities**
     - CRUD for stock listings (admin).
     - Query stock prices, tickers, symbols, and related metadata.
     - Optionally provide price feeds or integrate with an external pricing source.
   - **Data**
     - `stocks`, `markets`, `exchanges`, and optional historical quotes.

4. **Order Service (port 8083)**
   - **Domain**
     - Order lifecycle: placement, validation, matching, cancellation.
   - **Capabilities**
     - Accept buy/sell orders from authenticated users via the gateway.
     - Validate orders (balance checks, risk rules, instrument availability).
     - Match compatible orders and produce trades/executions.
     - Publish domain events (e.g., `OrderPlaced`, `OrderMatched`, `OrderCancelled`) via RabbitMQ.
   - **Data**
     - `orders`, `executions`, `order_events`.

5. **Transaction Service (port 8084)**
   - **Domain**
     - Recording trades and settlements on the **private Ethereum blockchain**.
   - **Capabilities**
     - Receive executed trade events from the Order Service (via RabbitMQ).
     - Interact with Solidity smart contracts to persist immutable transaction records.
     - Store blockchain transaction hashes, block numbers, and status in PostgreSQL.
   - **Smart Contracts**
     - Typically managed within the `blockchain/` directory using Hardhat/Truffle.
     - Contracts might include:
       - `TradeLedger.sol` – tracks trades, ownership transfers.
       - `Settlement.sol` – handles settlement flows.
     - See `blockchain/README.md` for contract details and deployment procedures.

6. **Portfolio Service (port 8085)**
   - **Domain**
     - User positions, holdings, and realized/unrealized P&L.
   - **Capabilities**
     - Maintain up-to-date portfolios based on executions and blockchain records.
     - Provide portfolio snapshots and performance metrics to the frontend.
     - Subscribe to `OrderMatched` / `TransactionRecorded` events to reconcile holdings.
   - **Data**
     - `portfolios`, `positions`, `cash_accounts`, `valuation_snapshots`.

7. **Notification Service (port 8086)**
   - **Domain**
     - Real-time user notifications via WebSockets, push, or email.
   - **Capabilities**
     - WebSocket channels for order status updates, trade confirmations, price alerts.
     - Subscribes to domain events from other services and fans out notifications.
   - **Integration**
     - Exposes WebSocket endpoints to the frontend.
     - Optionally integrates with external providers (email/SMS) via adapters.

8. **Eureka Server (port 8761)**
   - **Domain**
     - Service discovery and registration.
   - **Capabilities**
     - Maintains dynamic registry of running microservices.
     - Supports load balancing and resilience for internal calls.
   - **UI**
     - Dashboard at `http://localhost:8761` to inspect registered instances.

---

## Frontend Application

- **Stack**
  - React 18+, TypeScript, and a modern UI library (e.g., MUI, Ant Design, or custom components).
  - State management via Redux/RTK, React Query, or similar (depending on implementation).
  - Routing via React Router (SPA).

- **Key Features**
  - Secure authentication and session management (JWT in HTTP-only cookies or Authorization headers).
  - Dashboard with portfolio overview, recent orders, and notifications.
  - Market view: browse stocks, view prices and basic analytics.
  - Order entry forms (market/limit) and order status tracking.
  - Real-time updates via WebSockets for fills, price changes, and alerts.

- **Developer Experience**
  - Type-safe API clients generated from OpenAPI (if configured).
  - Linting and formatting via ESLint/Prettier.
  - Unit tests (Jest/React Testing Library) and basic E2E tests (e.g., Cypress/Playwright) as configured in the codebase.

---

## Blockchain Layer

- **Technology**
  - Private Ethereum network (e.g., Geth or Hardhat network).
  - Solidity smart contracts for trade recording and settlement flows.
  - Hardhat (or Truffle) for compilation, testing, and deployment.

- **Key Concepts**
  - **On-chain trade log**: ensures immutability and auditability of executed trades.
  - **Off-chain vs. on-chain state**: PostgreSQL stores operational data; blockchain stores authoritative transaction proofs.
  - **Gas & costs**: for private networks, gas is still used but cost is virtual; in production-like environments, you may integrate with real or consortium networks.

- **Relevant Docs**
  - See `blockchain/README.md` or per-contract documentation within `blockchain/contracts/`.
  - Test scenarios under `blockchain/test/` describing edge cases and invariants.

---

## Operational Documentation

- **Infrastructure as Code**
  - `docker-compose.yml`: spins up local dependencies (PostgreSQL instances, RabbitMQ, Prometheus, Grafana, potentially a local Ethereum node).
  - `kubernetes/`: manifests/Helm charts for deploying all services and infrastructure to a Kubernetes cluster.

- **Monitoring & Observability**
  - `monitoring/` typically contains:
    - Prometheus configuration for scraping metrics from services (via `/actuator/prometheus` or custom endpoints).
    - Grafana dashboards for:
      - Service health and latency.
      - Order throughput and error rates.
      - Blockchain transaction metrics.
  - Logs collected via standard logging frameworks (e.g., Logback) and can be integrated with ELK/EFK if configured.

- **Security & Compliance**
  - JWT-based authentication at the gateway.
  - TLS termination and secure headers (configure at ingress/proxy level for production).
  - Principle of least privilege for service DB users and message broker credentials.
  - Optional audit logging for user actions and admin operations.

---

## Environments & Configuration

- **Configuration Management**
  - Standard Spring Boot configuration via `application.yml` / `application-*.yml`.
  - Environment variables or config maps/secrets (in Kubernetes) for sensitive values:
    - DB credentials
    - JWT secrets or keys
    - RabbitMQ credentials
    - Blockchain node endpoints and private keys

- **Typical Environments**
  - **Local development**: Docker Compose + local Kubernetes (optional).
  - **Staging**: Full stack deployed to a shared cluster with test data.
  - **Production**: Hardened cluster with proper scaling, backups, and monitoring.

---

## Testing Strategy

In addition to the commands already listed above, the testing strategy generally includes:

- **Unit tests**
  - For services, covering domain logic, aggregates, and adapters.
  - For smart contracts, covering all state transitions and failure modes.

- **Integration tests**
  - Service-to-DB and service-to-broker interactions.
  - Contract tests between services where applicable.

- **End-to-end tests**
  - From frontend through gateway to backend and (optionally) blockchain.
  - Cover critical user journeys: sign-up, login, place order, receive confirmation, view updated portfolio.

Refer to:
- `backend/**/src/test/` for Java tests.
- `frontend/src/__tests__/` (or similar) for UI tests.
- `blockchain/test/` for smart contract tests.

---

## Contribution & Code Quality

- **Coding Standards**
  - Backend: Follows standard Spring Boot and Java best practices (layered architecture, DTOs, validation, etc.).
  - Frontend: Follows React/TypeScript patterns with strict typing where possible.
  - Smart contracts: Follows Solidity security best practices (checks-effects-interactions, proper access control, etc.).

- **How to Contribute**
  1. Fork the repository and create a feature branch from `Development` (or the active dev branch).
  2. Implement your changes, including tests and documentation updates.
  3. Ensure all tests pass and linters are clean.
  4. Open a Pull Request with a clear description, referencing any related issues.

---

## Quick Reference

- **Source code**
  - Backend services: `backend/`
  - Frontend app: `frontend/`
  - Smart contracts: `blockchain/`
- **Operations**
  - Local stack: `docker-compose.yml`
  - Kubernetes deployment: `kubernetes/`
  - Monitoring: `monitoring/`
- **Documentation & diagrams**
  - High-level docs: `docs/`
  - Diagrams: `docs/diagrams/`
  - API specs: `docs/apis/`

Use this README as the **entry point** to the project. For deep dives, follow the links into the `docs/` folders and per-service `README.md` files where available.