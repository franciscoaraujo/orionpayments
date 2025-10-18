package br.com.orionpay.settlement.adapters.out.fee;

import br.com.orionpay.settlement.application.ports.out.FeeCalculatorPort;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FeeCalculatorAdapter implements FeeCalculatorPort {

    // Em um sistema real, essa taxa viria de uma configuração do lojista.
    private static final BigDecimal FEE_PERCENTAGE = new BigDecimal("0.025"); // 2.5%

    @Override
    public BigDecimal calculateFee(BigDecimal grossAmount) {
        return grossAmount.multiply(FEE_PERCENTAGE).setScale(4, RoundingMode.HALF_UP);
    }
}