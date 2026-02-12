# Kubernetes Deployment

This directory contains Kubernetes manifests for deploying the Stock Brokerage Platform.

## Prerequisites

- Kubernetes cluster (v1.24+)
- kubectl configured
- Docker images built and pushed to registry

## Deployment Order

1. **Infrastructure** (PostgreSQL, RabbitMQ, etc.)
2. **Eureka Server**
3. **Microservices** (User, Stock, Order, Transaction, Portfolio, Notification)
4. **API Gateway**
5. **Frontend**

## Quick Start

```bash
# Apply all configurations
kubectl apply -f configmaps/
kubectl apply -f secrets/
kubectl apply -f deployments/
kubectl apply -f services/
```

## Service Access

- **API Gateway**: LoadBalancer service (check external IP)
- **Eureka Dashboard**: Port-forward to eureka service
- **Frontend**: LoadBalancer service

## Scaling

To scale a service:
```bash
kubectl scale deployment user-service --replicas=5
```

## Monitoring

Prometheus and Grafana can be deployed using Helm charts or similar manifests.

