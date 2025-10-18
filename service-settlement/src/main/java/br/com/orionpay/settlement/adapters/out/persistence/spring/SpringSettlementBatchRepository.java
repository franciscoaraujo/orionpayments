package br.com.orionpay.settlement.adapters.out.persistence.spring;


import br.com.orionpay.settlement.adapters.out.persistence.entity.BatchStatus;
import br.com.orionpay.settlement.adapters.out.persistence.entity.SettlementBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringSettlementBatchRepository extends JpaRepository<SettlementBatchEntity, UUID> {

    /**
     * Encontra o primeiro lote de liquidação para um determinado lojista
     * que ainda esteja com o status especificado (ex: OPEN).
     *
     * @param merchantId O ID do lojista.
     * @param status O status do lote.
     * @return Um Optional contendo o lote, se encontrado.
     */
    Optional<SettlementBatchEntity> findFirstByMerchantIdAndStatus(String merchantId, BatchStatus status);

    /**
     * Encontra todos os lotes que possuem um determinado status.
     * Usado pelo nosso processo agendado para encontrar todos os lotes ABERTOS.
     *
     * @param status O status a ser buscado.
     * @return Uma lista de lotes.
     */
    List<SettlementBatchEntity> findByStatus(BatchStatus status);
}