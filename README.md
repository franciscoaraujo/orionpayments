
# OrionPay — Cloud-Native Payment Processing Ecosystem

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3-green)
![Kafka](https://img.shields.io/badge/Kafka-Event--Driven-black)
![Kubernetes](https://img.shields.io/badge/Kubernetes-Cloud--Native-blue)
![License](https://img.shields.io/badge/License-MIT-lightgrey)
![Architecture](https://img.shields.io/badge/Architecture-Microservices-orange)
![Status](https://img.shields.io/badge/Status-Production--Ready-success)

---

## Overview

**OrionPay** is a cloud-native, distributed **Payment Gateway Infrastructure** designed to process the complete lifecycle of credit card transactions at scale.

The platform follows **modern FinTech-grade architecture standards**:

- Microservices & Hexagonal Architecture
- Event-Driven & Eventually Consistent
- Secure-by-Design (OAuth2 / OIDC / JWT)
- High Availability & Fault Tolerant
- Cloud-Native Kubernetes Deployment

Designed for **high-volume asynchronous processing**, financial reconciliation, and horizontal scalability.
 ## Sequential Diagram
<img width="1026" height="432" alt="image" src="https://github.com/user-attachments/assets/9991a02b-ee1b-4cd7-83de-aea62d2c48bc" />

---

## Architecture Principles

- Hexagonal Architecture (Ports & Adapters)
- Database per Service
- Event-Driven Microservices
- Idempotent Consumers
- Eventually Consistent Financial State
- Zero-Downtime Deployment (K8s Probes + Init Containers)
- Secure-by-Design (OIDC + JWT + Token Validation)

---

## Technology Stack

| Category | Technology |
|----------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Gateway | Spring Cloud Gateway |
| Security | Spring Security, OAuth2, JWT, OIDC |
| Messaging | Apache Kafka |
| Database | PostgreSQL |
| Cache | Redis |
| Orchestration | Kubernetes, Minikube, Skaffold |
| Observability | Health Probes, Logs |
| Architecture | Microservices + Hexagonal |

---

## Services

| Service | Port | Responsibility |
|---------|------|----------------|
| api-gateway | 8080 | Entry point, routing, token validation |
| identity-service | 9090 | OAuth2 Identity Provider, JWT issuance |
| authorization-service | 8081 | Real-time payment authorization |
| capture-service | 8082 | Async capture processing |
| settlement-service | 8083 | Financial settlement & reconciliation |

---

## End-to-End Flow

1. Client authenticates → receives JWT
2. Payment Authorization (Sync)
3. Transaction persisted
4. Event published to Kafka
5. Capture Service processes asynchronously
6. Settlement Service finalizes transaction

---

## Running Locally

### Requirements

- Docker Desktop (Kubernetes enabled)
- Minikube
- Skaffold
- kubectl

### Start Environment


minikube start --memory 8192 --cpus 4
skaffold dev --port-forward --status-check=false --cleanup=false


curl http://localhost:8080/api/v1/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"admin","password":"password123"}'


curl http://localhost:8080/api/v1/payments/authorize \
-H "Authorization: Bearer TOKEN" \
-H "Content-Type: application/json" \
-d '{ "amount":125.50, "currency":"BRL" }'



---


