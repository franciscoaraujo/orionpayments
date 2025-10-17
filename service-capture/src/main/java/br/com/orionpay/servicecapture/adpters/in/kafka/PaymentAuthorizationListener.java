package br.com.orionpay.servicecapture.adpters.in.kafka;

import br.com.orionpay.servicecapture.adpters.out.persistence.kafka.dto.PaymentAuthorizedEvent;
import br.com.orionpay.servicecapture.application.ports.in.CaptureUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentAuthorizationListener {

    private final CaptureUseCase captureUseCase;

    @KafkaListener(topics = "${orionpay.topics.payment-authorized}",
            groupId = "capture-group",
            concurrency = "${spring.kafka.listener.concurrency:3}")
    public void consume(PaymentAuthorizedEvent event) { // Recebe o objeto desserializado diretamente
        try {
            // O ObjectMapper não é mais necessário aqui
            captureUseCase.processCapture(event);
        } catch (Exception e) {
            log.error("Failed to process authorization event for transaction ID: {}", event.transactionId(), e);
            // A exceção será capturada pelo DefaultErrorHandler configurado
            throw e;
        }
    }
}