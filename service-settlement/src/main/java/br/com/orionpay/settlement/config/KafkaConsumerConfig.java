package br.com.orionpay.settlement.config;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.time.Instant;
import java.util.function.BiFunction;

@Configuration
@Slf4j
public class KafkaConsumerConfig {


    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> kafkaTemplate, MeterRegistry meterRegistry) {

        BiFunction<ConsumerRecord<?, ?>, Exception, Headers> addFailureHeaders = (record, exception) -> {
            log.error("Sending message to DLQ. Topic: {}, Key: {}", record.topic(), record.key());
            meterRegistry.counter("kafka.consumer.dlq.messages.sent.total", "topic", record.topic()).increment();

            Headers headers = record.headers();
            headers.add("dlq-original-topic", record.topic().getBytes());
            headers.add("dlq-original-partition", String.valueOf(record.partition()).getBytes());
            headers.add("dlq-original-offset", String.valueOf(record.offset()).getBytes());
            headers.add("dlq-exception-class", exception.getClass().getName().getBytes());
            headers.add("dlq-exception-message", exception.getMessage().getBytes());
            headers.add("dlq-failure-timestamp", String.valueOf(Instant.now().toEpochMilli()).getBytes());
            return headers;
        };

        var deadLetterRecoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
        deadLetterRecoverer.setHeadersFunction(addFailureHeaders);

        var backOff = new FixedBackOff(1000L, 2L);

        var errorHandler = new DefaultErrorHandler(deadLetterRecoverer, backOff);

        errorHandler.setRetryListeners((consumerRecord, ex, deliveryAttempt) -> {
            log.warn(
                    "Failed to process message. Delivery attempt: {}, Topic: {}, Partition: {}, Offset: {}",
                    deliveryAttempt,
                    consumerRecord.topic(),
                    consumerRecord.partition(),
                    consumerRecord.offset()
            );
            meterRegistry.counter("kafka.consumer.retries.total", "topic", consumerRecord.topic()).increment();
        });

        return errorHandler;
    }

    /**
     * Cria uma factory customizada de listeners para aplicar nosso Error Handler.
     * Esta abordagem usa o ConcurrentKafkaListenerContainerFactoryConfigurer do Spring Boot
     * para garantir que todas as configurações do application.yml sejam aplicadas antes
     * de adicionarmos nossa customização.
     *
     * @param configurer           O configurador padrão do Spring Boot.
     * @param kafkaConsumerFactory A consumer factory padrão.
     * @param errorHandler         Nosso Error Handler customizado.
     * @return A factory configurada.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<Object, Object> kafkaConsumerFactory,
            DefaultErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}