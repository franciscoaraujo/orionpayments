package br.com.orionpay.settlement.adapters.in.kafka.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// Evento consumido do 'servico-captura'
public record PaymentCapturedEvent(
        UUID transactionId,
        String customerId, // Aqui usaremos como 'merchantId' para simplificar
        BigDecimal amount,
        String currency,
        LocalDateTime capturedAt
) {}