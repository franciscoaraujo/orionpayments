# Resilience Engineering

## Failure Types Tested

- Kafka outage
- DB connection loss
- Pod crash
- Network latency
- Partial cluster failure

## Mechanisms

- Retry with backoff
- Circuit breaker
- Idempotency
- DLQ
- Event replay

## Chaos Testing

- Pod kill
- Network delay injection
- Kafka partition loss
