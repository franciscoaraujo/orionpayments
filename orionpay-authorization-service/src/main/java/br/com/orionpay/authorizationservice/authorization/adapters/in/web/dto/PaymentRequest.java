package br.com.orionpay.authorizationservice.authorization.adapters.in.web.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record   PaymentRequest(

        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotBlank(message = "Currency cannot be blank")
        String currency,

        @NotNull(message = "Card data cannot be null")
        @Valid
        CardRequest card,

        @NotNull(message = "Customer data cannot be null")
        @Valid
        CustomerRequest customer
) {}