package br.com.orionpay.settlement.adapters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "settlement_transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementTransactionEntity {
    @Id
    @Column(name = "transaction_id")
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private SettlementBatchEntity batch;

    @Column(precision = 19, scale = 4)
    private BigDecimal grossAmount;

    @Column(precision = 19, scale = 4)
    private BigDecimal feeAmount;

    @Column(precision = 19, scale = 4)
    private BigDecimal netAmount;
}