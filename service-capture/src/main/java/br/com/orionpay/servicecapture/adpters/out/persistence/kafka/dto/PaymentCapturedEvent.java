package br.com.orionpay.servicecapture.adpters.out.persistence.kafka.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// This is the event produced after a successful capture
public record PaymentCapturedEvent(
        UUID transactionId,
        String customerId,
        BigDecimal amount,
        String currency,
        LocalDateTime capturedAt
) {}