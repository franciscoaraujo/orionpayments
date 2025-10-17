package br.com.orionpay.authorizationservice.authorization.adapters.out.outbox;

import br.com.orionpay.authorizationservice.authorization.adapters.out.kafka.PaymentAuthorizationProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxProcessScheduler {

    private final OutboxEventRepository outboxEventRepository;
    private final
    PaymentAuthorizationProducer kafkaProducer;

    // Executa a cada 10 segundos. Em produção, este valor deve ser configurável.
    @Scheduled(fixedDelayString = "${orionpay.outbox.scheduler.delay:10000}")
    @Transactional
    public void processEventsOutbox(){
        log.debug("Executando scheduler do Outbox...");

        List<OutboxEventEntity> eventosPendentes = outboxEventRepository
                .findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        if (eventosPendentes.isEmpty()) {
            return; // Nenhum evento para processar
        }
        log.info("Encontrados {} eventos pendentes no Outbox. Processando...", eventosPendentes.size());

        for (OutboxEventEntity evento : eventosPendentes) {
            try {
                // Tenta publicar o evento no Kafka
                kafkaProducer.publicarEvento(evento.getPayload(), evento.getAggregateId());

                // Se publicou com sucesso, atualiza o status
                evento.setStatus(OutboxStatus.SENT);
                evento.setSentAt(LocalDateTime.now());
                outboxEventRepository.save(evento);
                log.info("Evento do Outbox [ID={}] enviado com sucesso.", evento.getId());
            } catch (Exception e) {
                log.error("Falha ao processar evento do Outbox [ID={}]. A tentativa será feita novamente no próximo ciclo.", evento.getId(), e);
                // Não fazemos nada com o evento. Ele continuará como PENDENTE
                // e será pego novamente na próxima execução do scheduler.
                // Poderíamos adicionar lógica para marcar como FALHA após X tentativas.
            }
        }
    }



}
