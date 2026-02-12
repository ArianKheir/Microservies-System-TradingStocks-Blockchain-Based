## Eureka Server

The **Eureka Server** provides service discovery for the microservices in the Stock Brokerage Platform.

### Responsibilities

- Act as a registry where all microservices register themselves.
- Allow clients and the API Gateway to discover service instances dynamically.
- Improve resilience and scalability by avoiding hard-coded service endpoints.

### Technology Stack

- Java, Spring Boot.
- Spring Cloud Netflix Eureka Server.

### Dashboard

- Eureka dashboard is available at:
  - `http://<eureka-host>:8761`
- The dashboard shows all registered instances and their status.

### Configuration

Configuration is in `src/main/resources/application.yml`:

- Eureka server properties.
- Optional security configuration if access to the dashboard is restricted.

Client services are configured with `eureka.client.*` properties to register with this server.

### Running Locally

```bash
cd backend/eureka-server
mvn spring-boot:run
```

Start Eureka **before** the other microservices so they can register successfully.

