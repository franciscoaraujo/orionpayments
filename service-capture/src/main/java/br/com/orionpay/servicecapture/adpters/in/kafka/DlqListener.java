package br.com.orionpay.servicecapture.adpters.in.kafka;

import br.com.orionpay.servicecapture.adpters.out.dlq.DlqMessageEntity;
import br.com.orionpay.servicecapture.adpters.out.dlq.SpringDlqRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class DlqListener {

    private final SpringDlqRepository dlqRepository;

    @KafkaListener(topics = "${orionpay.topics.capture-dlq}", groupId = "dlq-manager-group")
    public void consumeDlq(ConsumerRecord<String, String> record) {
        String exceptionMessage = new String(record.headers().lastHeader("dlq-exception-message").value(), StandardCharsets.UTF_8);
        String originalTopic = new String(record.headers().lastHeader("dlq-original-topic").value(), StandardCharsets.UTF_8);

        DlqMessageEntity dlqMessage = DlqMessageEntity.builder()
                .originalTopic(originalTopic)
                .messageKey(record.key())
                .payload(record.value())
                .exceptionMessage(exceptionMessage)
                .reprocessed(false)
                .build();

        dlqRepository.save(dlqMessage);
    }
}