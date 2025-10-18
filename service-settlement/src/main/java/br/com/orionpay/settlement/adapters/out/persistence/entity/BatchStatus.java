package br.com.orionpay.settlement.adapters.out.persistence.entity;

public enum BatchStatus {
    OPEN,      // Lote aberto, recebendo novas transações
    CLOSED,    // Lote fechado, aguardando pagamento
    PAID_OUT   // Lote liquidado, pagamento efetuado
}