package br.com.orionpay.authorizationservice.authorization.adapters.in.web;

import br.com.orionpay.authorizationservice.authorization.adapters.in.web.dto.PaymentRequest;
import br.com.orionpay.authorizationservice.authorization.adapters.in.web.dto.PaymentResponse;
import br.com.orionpay.authorizationservice.authorization.adapters.in.web.mapper.PaymentRestMapper;
import br.com.orionpay.authorizationservice.authorization.application.domain.model.Payment;
import br.com.orionpay.authorizationservice.authorization.application.ports.in.PaymentUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentUseCase paymentUseCase;
    private final PaymentRestMapper paymentRestMapper;

    @PostMapping
    public ResponseEntity<PaymentResponse> authorize(
            @RequestBody @Valid PaymentRequest request,
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @AuthenticationPrincipal Jwt jwt) { // Injeta o JWT validado

        // Agora podemos acessar os dados do token, como o ID do cliente (lojista)
        String clientId = jwt.getSubject(); // O "subject" do JWT geralmente é o client_id
        log.info("Authorization request received from authenticated client ${clientId}");


        Payment payment = paymentRestMapper.toDomain(request);
        Payment processedPayment = paymentUseCase.authorizePayment(payment);
        PaymentResponse response = paymentRestMapper.toResponse(processedPayment);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
