package br.com.orionpay.servicecapture.adpters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "captured_transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptureTransactionEntity {

    @Id
    @Column(name = "transaction_id", updatable = false, nullable = false)
    private UUID transactionId; // Using the ID from the authorization event

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false, length = 3)
    private String currency;

    @Column(name = "card_token", nullable = false, updatable = false)
    private String cardToken;

    @Column(name = "customer_id", nullable = false, updatable = false)
    private String customerId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
