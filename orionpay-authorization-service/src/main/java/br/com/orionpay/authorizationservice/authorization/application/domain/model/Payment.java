package br.com.orionpay.authorizationservice.authorization.application.domain.model;

import br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.entity.StatusTransaction;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Payment {

    private final UUID id;
    private final BigDecimal amount;
    private final String currency;

    @Setter
    private StatusTransaction status;

    private final String cardToken;

    @Setter
    private  String cardBrand;

    @Setter
    private  String lastFourDigits;

    @Setter
    private String authorizationCode;

    private final String customerId;
    private final LocalDateTime createdAt;

    @Setter
    private LocalDateTime updatedAt;

    public void markAsAuthorized(String authCode) {
        if (this.status != StatusTransaction.PENDING) {
            throw new IllegalStateException("Only PENDING payments can be authorized.");
        }
        this.status = StatusTransaction.AUTHORIZED;
        this.authorizationCode = authCode;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        if (this.status != StatusTransaction.PENDING) {
            throw new IllegalStateException("Only PENDING payments can fail.");
        }
        this.status = StatusTransaction.FAILED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isRecurring() {
        // Example logic
        return false;
    }
}