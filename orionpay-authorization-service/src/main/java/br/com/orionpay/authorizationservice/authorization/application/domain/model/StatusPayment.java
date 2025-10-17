package br.com.orionpay.authorizationservice.authorization.application.domain.model;

public enum StatusPayment {
    PENDING,
    AUTHORIZED,
    FAILED,
    CAPTURED,
    CANCELLED
}
