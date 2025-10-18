package br.com.orionpay.settlement.adapters.out.persistence;


import br.com.orionpay.settlement.adapters.out.persistence.entity.BatchStatus;
import br.com.orionpay.settlement.adapters.out.persistence.entity.SettlementBatchEntity;
import br.com.orionpay.settlement.adapters.out.persistence.spring.SpringSettlementBatchRepository;
import br.com.orionpay.settlement.application.ports.SettlementRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SettlementRepositoryAdapter implements SettlementRepositoryPort {

    private final SpringSettlementBatchRepository springRepository;

    @Override
    public Optional<SettlementBatchEntity> findOpenBatchByMerchantId(String merchantId) {
        return springRepository.findFirstByMerchantIdAndStatus(merchantId, BatchStatus.OPEN);
    }

    @Override
    public SettlementBatchEntity save(SettlementBatchEntity batch) {
        return springRepository.save(batch);
    }

    @Override
    public List<SettlementBatchEntity> findByStatus(BatchStatus status) {
        return springRepository.findByStatus(status);
    }
}