package br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.spring;

import br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SpringPaymentRepository extends JpaRepository<PaymentEntity, UUID> {
}