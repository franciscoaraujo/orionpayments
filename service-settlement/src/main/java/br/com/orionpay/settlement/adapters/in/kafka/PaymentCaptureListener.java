package br.com.orionpay.liquidacao.adapters.in.kafka;

import br.com.orionpay.settlement.adapters.in.kafka.dto.PaymentCapturedEvent;
import br.com.orionpay.settlement.application.ports.in.SettlementUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCaptureListener {

    private final SettlementUseCase settlementUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${orionpay.topics.payment-captured}", groupId = "settlement-group")
    public void consume(String message) {
        try {
            PaymentCapturedEvent event = objectMapper.readValue(message, PaymentCapturedEvent.class);
            settlementUseCase.processCapturedPayment(event);
        } catch (JsonProcessingException e) {
            // Erros de parsing de JSON não devem ser retentados. Logamos e ignoramos.
            log.error("Failed to parse event message. Discarding message. Content: {}", message, e);
            // Não relançamos a exceção para não ir para a DLQ
        } catch (Exception e) {
            // Para qualquer outro erro (ex: banco de dados indisponível),
            // nós relançamos a exceção.
            log.error("An unexpected error occurred while processing captured event. Message: {}", message, e);
            // AO RELANÇAR A EXCEÇÃO, ACIONAMOS O DEFAULT_ERROR_HANDLER (RETRIES + DLQ)
            throw e;
        }
    }
}