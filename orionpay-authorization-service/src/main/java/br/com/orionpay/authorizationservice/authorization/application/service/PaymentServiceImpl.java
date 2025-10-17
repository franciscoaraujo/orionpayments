package br.com.orionpay.authorizationservice.authorization.application.service;

import br.com.orionpay.authorizationservice.authorization.adapters.out.cardbrand.CardBrandAuthorizationPayment;
import br.com.orionpay.authorizationservice.authorization.adapters.out.outbox.OutboxEventEntity;
import br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.entity.BinInfoEntity;


import br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.entity.StatusTransaction;
import br.com.orionpay.authorizationservice.authorization.adapters.out.persistence.spring.BinInfoRepository;
import br.com.orionpay.authorizationservice.authorization.adapters.out.outbox.OutboxEventRepository;
import br.com.orionpay.authorizationservice.authorization.adapters.out.outbox.OutboxStatus;
import br.com.orionpay.authorizationservice.authorization.application.domain.exception.BusinessRuleException;
import br.com.orionpay.authorizationservice.authorization.application.domain.model.Payment;

import br.com.orionpay.authorizationservice.authorization.application.ports.in.PaymentUseCase;

import br.com.orionpay.authorizationservice.authorization.application.ports.out.PaymentRepositoryPort;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentUseCase {

    private final PaymentRepositoryPort paymentRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final BinInfoRepository binInfoRepository;
    private final CardBrandAuthorizationPayment brandAuthorizationPayment;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    @SneakyThrows
    public Payment authorizePayment(Payment payment) {

        payment.setStatus(StatusTransaction.PENDING);
        String cardNumber = payment.getCardToken();

        BinInfoEntity binInfo = binInfoRepository.findBestMatch(cardNumber)
                .orElseThrow(() -> new IllegalArgumentException("Unrecognized card BIN."));

        applyPrepaidCardRules(payment, binInfo);

        boolean authorizedByScheme = brandAuthorizationPayment.authorizationPaymentExtern(binInfo.getBrand()).get();

        if (authorizedByScheme) {
            payment.markAsAuthorized("AUTH_" + new Random().nextInt(999999));
        } else {
            payment.markAsFailed();
        }
        payment.setCardBrand(binInfo.getBrand());
        payment.setLastFourDigits(cardNumber.substring(cardNumber.length() - 4));
        Payment savedPayment = paymentRepository.save(payment);


        if (savedPayment.getStatus() == StatusTransaction.AUTHORIZED) {
            OutboxEventEntity outboxEvent = OutboxEventEntity.builder()
                    .aggregateType("Payment")
                    .aggregateId(savedPayment.getId().toString())
                    .eventType("PAYMENT_AUTHORIZED")
                    .payload(objectMapper.writeValueAsString(savedPayment))
                    .status(OutboxStatus.PENDING)
                    .build();
            outboxEventRepository.save(outboxEvent);
        }

        return savedPayment;
    }

    private void applyPrepaidCardRules(Payment payment, BinInfoEntity binInfo) {
        if (!"PREPAID".equals(binInfo.getCardType())) {
            return;
        }

        if (payment.isRecurring()) {
            throw new BusinessRuleException("Prepaid cards are not accepted for subscriptions.");
        }

        BigDecimal prepaidLimit = new BigDecimal("500.00");
        if (payment.getAmount().compareTo(prepaidLimit) > 0) {
            throw new BusinessRuleException("Amount exceeds the limit for prepaid cards.");
        }
    }
}