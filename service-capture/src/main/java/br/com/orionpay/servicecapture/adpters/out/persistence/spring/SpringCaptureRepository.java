package br.com.orionpay.servicecapture.adpters.out.persistence.spring;

import br.com.orionpay.servicecapture.adpters.out.persistence.entity.CaptureTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository interface for the CaptureTransactionEntity.
 *
 * By extending JpaRepository, Spring automatically provides implementations for
 * standard CRUD operations (save, findById, delete, etc.) at runtime.
 */
@Repository
public interface SpringCaptureRepository extends JpaRepository<CaptureTransactionEntity, UUID> {
    // Métodos de consulta customizados podem ser adicionados aqui se necessário.
    // Ex: Optional<CaptureTransactionEntity> findByCustomerId(String customerId);
}