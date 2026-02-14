# Security Architecture

## Threat Model (STRIDE)

| Threat | Mitigation |
|--------|-----------|
| Spoofing | OAuth2 + JWT validation |
| Tampering | Signed tokens + TLS |
| Repudiation | Immutable ledger |
| Information Disclosure | Encryption at rest |
| Denial of Service | Rate limiting |
| Privilege Escalation | Scope-based access |

---

## Zero Trust Model

- No implicit trust between services
- Token-based service communication
- Future: mTLS

---

## Payment Security

- No PAN storage
- Tokenized card data
- PCI-ready design
- Encryption in transit and at rest

---

## Secrets Management

- Kubernetes Secrets
- External Vault (recommended)

---

## Future Security Roadmap

- mTLS between services
- HSM-backed JWT signing
- PCI-DSS Level 1
- Fraud detection pipeline
