package br.com.orionpay.authorizationservice.authorization.adapters.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        @NotBlank(message = "Customer ID cannot be blank")
        String id,

        @NotBlank(message = "Customer email cannot be blank")
        @Email(message = "Invalid email format")
        String email
) {}