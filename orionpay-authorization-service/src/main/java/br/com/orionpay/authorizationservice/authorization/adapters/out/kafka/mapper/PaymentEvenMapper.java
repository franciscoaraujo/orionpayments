package br.com.orionpay.authorizationservice.authorization.adapters.out.kafka.mapper;

import br.com.orionpay.authorizationservice.authorization.adapters.out.kafka.dto.PaymentAuthorizedEvent;
import br.com.orionpay.authorizationservice.authorization.application.domain.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentEvenMapper {

    @Mapping(source = "id", target = "transactionId") // Mapeado de 'id' para 'transactionId'
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "currency", target = "currency")
    PaymentAuthorizedEvent toPaymentAuthorizationEvent(Payment payment);
}