package br.com.orionpay.settlement.adapters.out.kafka;



import br.com.orionpay.settlement.adapters.out.kafka.dto.SettlementProcessedEvent;
import br.com.orionpay.settlement.adapters.out.persistence.entity.SettlementBatchEntity;
import br.com.orionpay.settlement.application.ports.out.SettlementEventProducerPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementEventProducerAdapter implements SettlementEventProducerPort {

    private final KafkaTemplate<String, SettlementProcessedEvent> kafkaTemplate;

    @Value("${orionpay.topics.settlement-processed}")
    private String topic;

    @Override
    public void sendSettlementProcessedEvent(SettlementBatchEntity batch) {
        var event = new SettlementProcessedEvent(
                batch.getId(),
                batch.getMerchantId(),
                batch.getTotalNetAmount(),
                batch.getStatus(),
                batch.getPaidOutAt()
        );

        kafkaTemplate.send(topic, batch.getMerchantId(), event);
    }
}