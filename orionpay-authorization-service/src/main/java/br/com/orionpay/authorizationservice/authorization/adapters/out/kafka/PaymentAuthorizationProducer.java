package br.com.orionpay.authorizationservice.authorization.adapters.out.kafka;


import br.com.orionpay.authorizationservice.authorization.adapters.out.kafka.dto.PaymentAuthorizedEvent;

import br.com.orionpay.authorizationservice.authorization.adapters.out.kafka.mapper.PaymentEvenMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


/**
 * Adaptador de Saída que implementa a porta de eventos de pagamento.
 * É responsável por publicar mensagens em um tópico do Apache Kafka.
 */
@Slf4j
@Component
public class PaymentAuthorizationProducer {

    // Template do Spring Kafka para facilitar o envio de mensagens.
    // A chave da mensagem será uma String (o ID da transação) e o valor será nosso DTO.
    private final KafkaTemplate<String, PaymentAuthorizedEvent> kafkaTemplateEvent;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // Mapper para converter do nosso modelo de domínio para o DTO do evento.
    private final PaymentEvenMapper mapper;

    @Value("${orionpay.topics.payment-authorized}")
    private String topicName;

    // Construtor para injeção de dependências com @Qualifier
    public PaymentAuthorizationProducer(
            @Qualifier("kafkaTemplateEvent") KafkaTemplate<String, PaymentAuthorizedEvent> kafkaTemplateEvent,
            @Qualifier("kafkaTemplateString") KafkaTemplate<String, String> kafkaTemplate,
            PaymentEvenMapper mapper) {
        this.kafkaTemplateEvent = kafkaTemplateEvent;
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    /**
     * Publica um evento genérico no Kafka.
     *
     * @param payload O conteúdo da mensagem, já em formato JSON.
     * @param key     A chave da mensagem (ex: ID da transação).
     */
    public void publicarEvento(String payload, String key) {
        log.info("Publicando evento no tópico '{}' com a chave '{}'", topicName, key);
        try {
            kafkaTemplate.send(topicName, key, payload).get();
        } catch (Exception e) {
            log.error("Erro ao publicar evento no Kafka. Key: {}", key, e);
            throw new RuntimeException("Falha na publicação do evento para o Kafka", e);
        }

    }
}
