package br.com.orionpay.settlement.adapters.out.kafka.dto;



import br.com.orionpay.settlement.adapters.out.persistence.entity.BatchStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// Evento produzido após um lote ser liquidado
public record SettlementProcessedEvent(
        UUID batchId,
        String merchantId,
        BigDecimal totalNetAmount,
        BatchStatus status,
        LocalDateTime paidOutAt
) {}