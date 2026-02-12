# Kubernetes Deployment

This directory contains Kubernetes manifests for deploying the **Stock Brokerage Platform** into a Kubernetes cluster. It is intended for **staging** and **production-like** environments, and complements the local `docker-compose.yml` setup described in the root `README.md`.

---

## Prerequisites

- **Cluster & Tooling**
  - Kubernetes cluster v1.24+ (managed service such as AKS/EKS/GKE, or on-prem).
  - `kubectl` configured with appropriate context.
  - Optional: `helm` if you use Helm charts in addition to raw manifests.
- **Container Images**
  - All service images (backend services, frontend, supporting infrastructure) built and pushed to a container registry accessible from the cluster.
  - Image names and tags configured in the deployment manifests (or overridden via Kustomize/Helm values).
- **Configuration & Secrets**
  - Database credentials, JWT secrets, RabbitMQ credentials, blockchain node configuration, etc. available as Kubernetes **Secrets**.
  - Non-sensitive configuration (service URLs, feature flags, environment names) available as **ConfigMaps**.

---

## Directory Structure

Typical structure of this directory:

- `configmaps/` – non-secret configuration for services.
- `secrets/` – secret manifests (ideally templated or managed via external secret management).
- `deployments/` – `Deployment` resources for all microservices, infrastructure components, and frontend.
- `services/` – `Service` resources (ClusterIP, NodePort, or LoadBalancer) exposing each component internally/externally.
- `ingress/` (if present) – `Ingress` definitions or gateway/ingress controller configuration.
- `namespaces/` (if present) – namespace definitions to logically isolate environments or concerns.
- `kustomization.yaml` or Helm charts (if present) – used for environment overlays and reuse.

Use the actual subdirectories in your project as the single source of truth.

---

## Deployment Order

Although many resources can be applied together, the **logical order** of dependencies is:

1. **Namespaces** (optional)
   - Create namespaces (e.g., `trading-dev`, `trading-staging`, `trading-prod`).
2. **Infrastructure**
   - PostgreSQL, RabbitMQ, Prometheus, Grafana, and any supporting components.
3. **Discovery / Coordination**
   - Eureka Server and any config-server components (if used).
4. **Core Microservices**
   - User, Stock, Order, Transaction, Portfolio, Notification services.
5. **API Gateway**
   - Exposed via `Service` (NodePort/LoadBalancer) and `Ingress` if required.
6. **Frontend**
   - Configured to call the API gateway base URL (internal or external).

In practice, you can usually apply manifests in batches, but understanding this dependency order is helpful for troubleshooting.

---

## Quick Start (All-in-One Apply)

From the `kubernetes/` directory:

```bash
# Apply configuration and secrets (ensure secret values are set/templated correctly)
kubectl apply -f configmaps/
kubectl apply -f secrets/

# Apply core application components
kubectl apply -f deployments/
kubectl apply -f services/

# (Optional) Apply ingress rules
kubectl apply -f ingress/
```

To verify:

```bash
kubectl get pods
kubectl get svc
kubectl get ingress
```

Wait until all pods are in `Running` or `Ready` state before hitting the endpoints.

---

## Service Access

Access patterns will depend on your cluster setup and how `Service` and `Ingress` resources are defined:

- **API Gateway**
  - Typically exposed as a `LoadBalancer` or via `Ingress`.
  - Retrieve external IP:
    ```bash
    kubectl get svc api-gateway -o wide
    ```
  - Or check assigned hostname if behind an ingress controller.

- **Eureka Dashboard**
  - Usually internal; use `kubectl port-forward`:
    ```bash
    kubectl port-forward svc/eureka-server 8761:8761
    ```
  - Access at `http://localhost:8761`.

- **Frontend**
  - Typically a `LoadBalancer` or `Ingress` service.
  - Retrieve external IP or hostname:
    ```bash
    kubectl get svc frontend -o wide
    # or
    kubectl get ingress
    ```

Consult the specific `Service` and `Ingress` YAMLs for exact names and ports.

---

## Scaling & Resilience

To scale a specific microservice:

```bash
kubectl scale deployment user-service --replicas=5
```

General guidelines:

- Configure **resource requests/limits** on all deployments to enable effective scheduling and cluster autoscaling.
- Use **readiness** and **liveness probes** to ensure Kubernetes only routes traffic to healthy pods.
- Consider **PodDisruptionBudgets (PDBs)** and **HorizontalPodAutoscalers (HPAs)** for critical services (API Gateway, Order Service, Transaction Service, etc.).

---

## Monitoring & Logging

- **Monitoring**
  - Prometheus and Grafana can be deployed via:
    - Helm charts (e.g., `prometheus-community/kube-prometheus-stack`).
    - Custom manifests under a `monitoring/` directory.
  - Ensure:
    - Services expose metrics endpoints (e.g., Spring Boot Actuator `/actuator/prometheus`).
    - Prometheus scrapes these endpoints using appropriate `ServiceMonitor`/`PodMonitor` or scrape configs.

- **Logging**
  - Logs are written to `stdout`/`stderr` and collected via the cluster’s logging solution.
  - Integrate with ELK/EFK or your provider’s logging stack for centralized, searchable logs.

For details on dashboards and metrics, refer to the root `README.md` and any documentation in `monitoring/`.

---

## Environment-Specific Configuration

You can maintain multiple environments with:

- **Separate namespaces** (e.g., `trading-dev`, `trading-staging`, `trading-prod`).
- **Overlay tools**:
  - **Kustomize**: `kustomization.yaml` files per environment.
  - **Helm**: `values-dev.yaml`, `values-staging.yaml`, `values-prod.yaml`.

Common differences per environment:

- Image tags (e.g., `:dev`, `:staging`, `:prod`).
- External URLs (gateway, frontend).
- Resource limits and replica counts.
- Feature flags and integration endpoints (e.g., external price feeds).

---

## Security Considerations

- Store sensitive values **only** in `Secret` objects (or external secret managers integrated with Kubernetes).
- Restrict `kubectl` access via RBAC, and use dedicated service accounts for CI/CD pipelines.
- Use **TLS** for:
  - Ingress controllers terminating HTTPS.
  - Internal traffic if required by compliance.
- Enforce **network policies** to restrict traffic between namespaces and services where applicable.

---

## Troubleshooting

Common commands:

```bash
# Check pod status
kubectl get pods

# Describe a problematic pod
kubectl describe pod <pod-name>

# View logs
kubectl logs <pod-name>
kubectl logs deployment/user-service

# Check events in the namespace
kubectl get events --sort-by=.lastTimestamp
```

If services are not reachable:

- Verify `Service` selectors match `Deployment` labels.
- Confirm `Ingress` rules and ingress controller configuration.
- Check that environment variables and secrets are correctly mounted into pods.

---

## Relationship to Other Documentation

For a broader view of the platform (microservices, blockchain, diagrams, and APIs), refer to the **root `README.md`**. This `kubernetes/README.md` focuses specifically on **deploying and operating** the system on Kubernetes.

