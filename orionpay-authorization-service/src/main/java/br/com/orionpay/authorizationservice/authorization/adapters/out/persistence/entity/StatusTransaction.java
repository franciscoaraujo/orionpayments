package br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.entity;

public enum StatusTransaction {
    PENDING,
    AUTHORIZED,
    FAILED,
    CAPTURED,
    CANCELLED
}
