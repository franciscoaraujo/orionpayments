package br.com.orionpay.settlement.adapters.out.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "settlement_batches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettlementBatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String merchantId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatchStatus status;

    @Column(precision = 19, scale = 4)
    private BigDecimal totalGrossAmount;

    @Column(precision = 19, scale = 4)
    private BigDecimal totalNetAmount;

    @Column(precision = 19, scale = 4)
    private BigDecimal totalFees;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SettlementTransactionEntity> transactions = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime paidOutAt;
}