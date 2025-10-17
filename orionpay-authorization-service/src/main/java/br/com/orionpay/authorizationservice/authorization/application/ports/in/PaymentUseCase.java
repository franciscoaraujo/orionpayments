package br.com.orionpay.authorizationservice.authorization.application.ports.in;

import br.com.orionpay.authorizationservice.authorization.application.domain.model.Payment;


public interface PaymentUseCase {

    Payment authorizePayment(Payment payment);
}