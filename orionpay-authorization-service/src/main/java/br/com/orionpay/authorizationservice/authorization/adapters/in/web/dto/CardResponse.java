package br.com.orionpay.authorizationservice.authorization.adapters.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CardResponse(
        String brand,
        @JsonProperty("last_four_digits")
        String lastFourDigits
) {}