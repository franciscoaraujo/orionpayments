package br.com.orionpay.settlement.application.ports;

import br.com.orionpay.settlement.adapters.out.persistence.entity.BatchStatus;
import br.com.orionpay.settlement.adapters.out.persistence.entity.SettlementBatchEntity;

import java.util.List;
import java.util.Optional;

public interface SettlementRepositoryPort {
    Optional<SettlementBatchEntity> findOpenBatchByMerchantId(String merchantId);

    SettlementBatchEntity save(SettlementBatchEntity batch);

    List<SettlementBatchEntity> findByStatus(BatchStatus status);
}