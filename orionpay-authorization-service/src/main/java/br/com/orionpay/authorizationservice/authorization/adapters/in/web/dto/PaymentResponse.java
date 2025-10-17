package br.com.orionpay.authorizationservice.authorization.adapters.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record PaymentResponse(
        @JsonProperty("transaction_id")
        String transactionId,
        String status,
        BigDecimal amount,
        String currency,
        CardResponse card,
        @JsonProperty("authorization_code")
        String authorizationCode
) {}