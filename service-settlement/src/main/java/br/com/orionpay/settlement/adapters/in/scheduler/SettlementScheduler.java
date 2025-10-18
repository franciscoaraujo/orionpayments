package br.com.orionpay.settlement.adapters.in.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SettlementScheduler {

    // private final SettlementUseCase settlementUseCase;

    /**
     * Este método roda periodicamente (ex: a cada 5 minutos para teste)
     * para fechar e liquidar os lotes abertos.
     */
    @Scheduled(cron = "${orionpay.scheduler.cron}")
    public void processOpenBatches() {
        log.info("Running scheduled job to settle open batches...");
        // settlementUseCase.settleOpenBatches();
    }
}