package br.com.orionpay.authorizationservice.authorization.adapters.in.web.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record CardRequest(
        @NotBlank(message = "Cardholder name is required")
        @JsonProperty("holder_name")
        String holderName,

        @NotBlank(message = "Card number is required")
        @Pattern(regexp = "^[0-9]{13,16}$", message = "Invalid card number")
        String token,

        @NotNull(message = "Expiration month is required")
        @Min(value = 1) @Max(value = 12)
        @JsonProperty("expiry_month")
        Integer expiryMonth,

        @NotNull(message = "Expiration year is required")
        @JsonProperty("expiry_year")
        Integer expiryYear,

        @NotBlank(message = "CVV is required")
        @Size(min = 3, max = 4)
        String cvv
) {}