package br.com.orionpay.authorizationservice.authorization.adapters.out.kafka.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentAuthorizedEvent(
        UUID transactionId,
        String customerId,
        BigDecimal amount,
        String currency,
        String cardToken
) {}