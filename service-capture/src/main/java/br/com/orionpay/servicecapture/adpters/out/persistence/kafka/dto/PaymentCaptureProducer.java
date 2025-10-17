package br.com.orionpay.servicecapture.adpters.out.persistence.kafka.dto;

import br.com.orionpay.servicecapture.adpters.out.persistence.entity.CaptureTransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PaymentCaptureProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${orionpay.topics.payment-captured}")
    private String topic;

    public void sendPaymentCapturedEvent(CaptureTransactionEntity transaction) {
        PaymentCapturedEvent event = new PaymentCapturedEvent(
                transaction.getTransactionId(),
                transaction.getCustomerId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                LocalDateTime.now()
        );
        kafkaTemplate.send(topic, transaction.getTransactionId().toString(), event);
    }
}