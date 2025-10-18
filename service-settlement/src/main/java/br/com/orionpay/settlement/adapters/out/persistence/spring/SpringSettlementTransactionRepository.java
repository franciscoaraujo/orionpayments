package br.com.orionpay.settlement.adapters.out.persistence.spring;

import br.com.orionpay.settlement.adapters.out.persistence.entity.SettlementTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringSettlementTransactionRepository extends JpaRepository<SettlementTransactionEntity, UUID> {
    // Métodos de consulta customizados para transações podem ser adicionados aqui se necessário.
}
