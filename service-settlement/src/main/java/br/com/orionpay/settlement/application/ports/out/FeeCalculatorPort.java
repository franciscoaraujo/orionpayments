package br.com.orionpay.settlement.application.ports.out;

import java.math.BigDecimal;

public interface FeeCalculatorPort {
    BigDecimal calculateFee(BigDecimal grossAmount);
}