package br.com.orionpay.servicecapture.application.service;


import br.com.orionpay.servicecapture.adpters.out.persistence.entity.CaptureTransactionEntity;
import br.com.orionpay.servicecapture.adpters.out.persistence.entity.TransactionStatus;
import br.com.orionpay.servicecapture.adpters.out.persistence.kafka.dto.PaymentAuthorizedEvent;
import br.com.orionpay.servicecapture.adpters.out.persistence.kafka.dto.PaymentCaptureProducer;
import br.com.orionpay.servicecapture.adpters.out.persistence.spring.SpringCaptureRepository;
import br.com.orionpay.servicecapture.application.ports.in.CaptureUseCase;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
//Adicionanado Métricas de Negócio (Observabilidade)
@Service
@RequiredArgsConstructor
@Slf4j
public class CaptureServiceImpl implements CaptureUseCase {

    private final SpringCaptureRepository captureRepository;
    private final PaymentCaptureProducer captureProducer;
    private final MeterRegistry meterRegistry; // INJETAR O REGISTRO DE MÉTRICAS

    @Override
    public void processCapture(PaymentAuthorizedEvent event) {
        // Inicia um timer para medir a duração do processamento
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            log.info("Processing capture for transaction ID: ${event.transactionId()}");

            CaptureTransactionEntity transaction = CaptureTransactionEntity.builder()
                    .transactionId(event.transactionId())
                    .status(TransactionStatus.AUTHORIZED)
                    .amount(event.amount())
                    .currency(event.currency())
                    .cardToken(event.cardToken())
                    .customerId(event.customerId())
                    .build();
            captureRepository.save(transaction);

            transaction.setStatus(TransactionStatus.CAPTURED);
            CaptureTransactionEntity capturedTransaction = captureRepository.save(transaction);

            captureProducer.sendPaymentCapturedEvent(capturedTransaction);

            // Incrementa o contador de sucesso
            meterRegistry.counter("captures.processed.total", "status", "success").increment();
            log.info("Transaction ID ${event.transactionId()} captured successfully.");

        } catch (Exception e) {
            // Incrementa o contador de falhas inesperadas
            meterRegistry.counter("captures.processed.total", "status", "error").increment();
            log.error("An unexpected error occurred during capture processing for transaction ${event.transactionId()}", e);
            throw e; // Lança a exceção para que o ErrorHandler do Kafka a capture
        } finally {
            // Para o timer e registra a duração, com uma tag de sucesso ou falha
            sample.stop(meterRegistry.timer("capture.processing.duration"));
        }
    }
}