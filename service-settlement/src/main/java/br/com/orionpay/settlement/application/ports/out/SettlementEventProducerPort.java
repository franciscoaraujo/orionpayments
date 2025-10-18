package br.com.orionpay.settlement.application.ports.out;


import br.com.orionpay.settlement.adapters.out.persistence.entity.SettlementBatchEntity;

public interface SettlementEventProducerPort {
    void sendSettlementProcessedEvent(SettlementBatchEntity batch);
}