package br.com.orionpay.settlement.application.ports.in;


import br.com.orionpay.settlement.adapters.in.kafka.dto.PaymentCapturedEvent;

/**
 * Porta de entrada para os casos de uso de liquidação.
 * Define as operações que o mundo exterior pode solicitar ao core da aplicação.
 */
public interface SettlementUseCase {

    /**
     * Processa um evento de pagamento capturado, adicionando-o a um lote de liquidação.
     * @param event O evento consumido do Kafka.
     */
    void processCapturedPayment(PaymentCapturedEvent event);

    /**
     * Processa todos os lotes de liquidação que estão abertos, fechando-os
     * e iniciando o processo de pagamento.
     */
    void settleOpenBatches();
}