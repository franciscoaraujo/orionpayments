package br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.mapper;

import br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.entity.PaymentEntity;
import br.com.orionpay.authorizationservice.authorization.application.domain.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentEntity toEntity(Payment payment) {
        PaymentEntity entity = new PaymentEntity();
        entity.setIdPayment(payment.getId()); // Set ID for existing payments
        entity.setAmount(payment.getAmount());
        entity.setCurrency(payment.getCurrency());
        entity.setStatus(payment.getStatus());
        entity.setCardToken(payment.getCardToken());
        entity.setCardBrand(payment.getCardBrand());
        entity.setLastFourDigits(payment.getLastFourDigits());
        entity.setAuthorizationCode(payment.getAuthorizationCode());
        entity.setCustomerId(payment.getCustomerId());
        entity.setCreatedAt(payment.getCreatedAt()); // Set createdAt for existing payments
        entity.setUpdatedAt(payment.getUpdatedAt()); // Set updatedAt for existing payments
        return entity;
    }

    public Payment toDomain(PaymentEntity savedEntity) {
        return Payment.builder()
                .id(savedEntity.getIdPayment())
                .amount(savedEntity.getAmount())
                .currency(savedEntity.getCurrency())
                .status(savedEntity.getStatus())
                .cardToken(savedEntity.getCardToken())
                .cardBrand(savedEntity.getCardBrand())
                .lastFourDigits(savedEntity.getLastFourDigits())
                .authorizationCode(savedEntity.getAuthorizationCode())
                .customerId(savedEntity.getCustomerId())
                .createdAt(savedEntity.getCreatedAt())
                .updatedAt(savedEntity.getUpdatedAt())
                .build();
    }
}
