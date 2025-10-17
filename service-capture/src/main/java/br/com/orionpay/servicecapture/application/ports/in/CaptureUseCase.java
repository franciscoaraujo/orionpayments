package br.com.orionpay.servicecapture.application.ports.in;


import br.com.orionpay.servicecapture.adpters.out.persistence.kafka.dto.PaymentAuthorizedEvent;

public interface CaptureUseCase {
    void processCapture(PaymentAuthorizedEvent event);
}
