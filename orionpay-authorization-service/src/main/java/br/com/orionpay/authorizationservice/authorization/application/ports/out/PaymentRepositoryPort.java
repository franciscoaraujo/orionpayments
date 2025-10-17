package br.com.orionpay.authorizationservice.authorization.application.ports.out;



import br.com.orionpay.authorizationservice.authorization.application.domain.model.Payment;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepositoryPort {

    Payment save(Payment payment);

    Optional<Payment> findById(UUID id);
}