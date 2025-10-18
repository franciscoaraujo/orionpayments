package br.com.orionpay.settlement.application.service;


import br.com.orionpay.settlement.adapters.in.kafka.dto.PaymentCapturedEvent;
import br.com.orionpay.settlement.adapters.out.persistence.entity.BatchStatus;
import br.com.orionpay.settlement.adapters.out.persistence.entity.SettlementBatchEntity;
import br.com.orionpay.settlement.adapters.out.persistence.entity.SettlementTransactionEntity;
import br.com.orionpay.settlement.application.ports.SettlementRepositoryPort;
import br.com.orionpay.settlement.application.ports.in.SettlementUseCase;
import br.com.orionpay.settlement.application.ports.out.FeeCalculatorPort;
import br.com.orionpay.settlement.application.ports.out.SettlementEventProducerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementServiceImpl implements SettlementUseCase {

    private final SettlementRepositoryPort settlementRepository;
    private final FeeCalculatorPort feeCalculator;
    private final SettlementEventProducerPort eventProducer;

    @Override
    @Transactional
    public void processCapturedPayment(PaymentCapturedEvent event) {
        log.info("Processing captured payment event for transactionId: {}", event.transactionId());

        // 1. Encontra um lote aberto para o lojista ou cria um novo.
        String merchantId = event.customerId(); // Usando customerId como merchantId para simplificar
        SettlementBatchEntity batch = settlementRepository.findOpenBatchByMerchantId(merchantId)
                .orElseGet(() -> createNewBatch(merchantId));

        // 2. Calcula as taxas e o valor líquido da transação.
        BigDecimal grossAmount = event.amount();
        BigDecimal feeAmount = feeCalculator.calculateFee(grossAmount);
        BigDecimal netAmount = grossAmount.subtract(feeAmount);

        // 3. Cria a entidade da transação de liquidação.
        SettlementTransactionEntity transaction = SettlementTransactionEntity.builder()
                .transactionId(event.transactionId())
                .batch(batch)
                .grossAmount(grossAmount)
                .feeAmount(feeAmount)
                .netAmount(netAmount)
                .build();

        // 4. Adiciona a transação ao lote e atualiza os totais.
        batch.getTransactions().add(transaction);
        batch.setTotalGrossAmount(batch.getTotalGrossAmount().add(grossAmount));
        batch.setTotalFees(batch.getTotalFees().add(feeAmount));
        batch.setTotalNetAmount(batch.getTotalNetAmount().add(netAmount));

        // 5. Salva o lote atualizado no banco de dados.
        settlementRepository.save(batch);
        log.info("Transaction {} added to batch {} for merchant {}", event.transactionId(), batch.getId(), merchantId);
    }

    @Override
    @Transactional
    public void settleOpenBatches() {
        log.info("Looking for open settlement batches to process...");
        List<SettlementBatchEntity> openBatches = settlementRepository.findByStatus(BatchStatus.OPEN);

        if (openBatches.isEmpty()) {
            log.info("No open settlement batches found.");
            return;
        }

        for (SettlementBatchEntity batch : openBatches) {
            log.info("Processing batchId: {} for merchantId: {}", batch.getId(), batch.getMerchantId());

            // 1. Fecha o lote para novas transações
            batch.setStatus(BatchStatus.CLOSED);
            settlementRepository.save(batch);

            // 2. Simula o processo de pagamento ao lojista.
            // Em um sistema real, aqui haveria a integração com um sistema bancário.
            log.info("Initiating payout of R$ {} for batchId: {}", batch.getTotalNetAmount(), batch.getId());
            batch.setStatus(BatchStatus.PAID_OUT);
            batch.setPaidOutAt(LocalDateTime.now());
            SettlementBatchEntity paidOutBatch = settlementRepository.save(batch);

            // 3. Publica um evento notificando que o lote foi liquidado.
            eventProducer.sendSettlementProcessedEvent(paidOutBatch);
            log.info("Batch {} successfully paid out.", batch.getId());
        }
    }

    private SettlementBatchEntity createNewBatch(String merchantId) {
        log.info("No open batch found for merchant {}. Creating a new one.", merchantId);
        return SettlementBatchEntity.builder()
                .merchantId(merchantId)
                .status(BatchStatus.OPEN)
                .totalGrossAmount(BigDecimal.ZERO)
                .totalFees(BigDecimal.ZERO)
                .totalNetAmount(BigDecimal.ZERO)
                .transactions(List.of())
                .build();
    }
}