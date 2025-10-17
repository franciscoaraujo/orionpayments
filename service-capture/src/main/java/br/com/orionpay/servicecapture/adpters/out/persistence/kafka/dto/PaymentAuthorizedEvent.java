package br.com.orionpay.servicecapture.adpters.out.persistence.kafka.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentAuthorizedEvent(
        UUID transactionId,
        String customerId,
        BigDecimal amount,
        String currency,
        String cardToken
) {
}
