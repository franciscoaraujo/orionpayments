package br.com.orionpay.authorizationservice.authorization.adapters.out.persistence;


import br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.entity.PaymentEntity;
import br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.mapper.PaymentMapper;
import br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.spring.SpringPaymentRepository;
import br.com.orionpay.authorizationservice.authorization.application.domain.model.Payment;
import br.com.orionpay.authorizationservice.authorization.application.ports.out.PaymentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Persistence Adapter that implements the PaymentRepositoryPort.
 * It acts as a bridge between the application's core (domain model)
 * and the persistence technology (Spring Data JPA).
 */
@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryPort {

    private final SpringPaymentRepository springRepository;
    private final PaymentMapper mapper; // MapStruct mapper for Domain <-> Entity conversion

    /**
     * Saves a Payment domain object.
     * It converts the domain object to a JPA entity before persisting.
     * @param payment The domain object to save.
     * @return The saved domain object, potentially with updated fields (like ID or timestamps).
     */
    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = mapper.toEntity(payment);
        PaymentEntity savedEntity = springRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    /**
     * Finds a Payment by its unique ID.
     * It fetches the JPA entity from the database and converts it back to a domain object.
     * @param id The UUID of the payment.
     * @return An Optional containing the Payment domain object if found.
     */
    @Override
    public Optional<Payment> findById(UUID id) {
        return springRepository.findById(id).map(mapper::toDomain);
    }
}