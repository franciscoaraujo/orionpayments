package br.com.orionpay.servicecapture.adpters.out.persistence.entity;

public enum TransactionStatus {
    AUTHORIZED, // Initial state when consumed
    CAPTURED,
    CAPTURE_FAILED
}