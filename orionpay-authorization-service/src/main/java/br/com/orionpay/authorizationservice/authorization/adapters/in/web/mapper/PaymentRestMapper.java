package br.com.orionpay.authorizationservice.authorization.adapters.in.web.mapper;

import br.com.orionpay.authorizationservice.authorization.adapters.in.web.dto.CardResponse;
import br.com.orionpay.authorizationservice.authorization.adapters.in.web.dto.PaymentRequest;
import br.com.orionpay.authorizationservice.authorization.adapters.in.web.dto.PaymentResponse;
import br.com.orionpay.authorizationservice.authorization.application.domain.model.Payment;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class PaymentRestMapper {

    public Payment toDomain(@Valid PaymentRequest request) {
        return Payment.builder()
                .amount(request.amount())
                .currency(request.currency())
                .cardToken(request.card().token())
                .customerId(request.customer().id())
                .build();
    }

    public PaymentResponse toResponse(Payment processedPayment) {
        CardResponse cardResponse = new CardResponse(processedPayment.getCardBrand(), processedPayment.getLastFourDigits());
        return new PaymentResponse(
                processedPayment.getId().toString(),
                processedPayment.getStatus().name(),
                processedPayment.getAmount(),
                processedPayment.getCurrency(),
                cardResponse,
                processedPayment.getAuthorizationCode()
        );
    }
}
