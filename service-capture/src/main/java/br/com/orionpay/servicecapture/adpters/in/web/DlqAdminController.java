package br.com.orionpay.servicecapture.adpters.in.web;


import br.com.orionpay.servicecapture.adpters.out.dlq.DlqMessageEntity;
import br.com.orionpay.servicecapture.adpters.out.dlq.SpringDlqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/dlq")
@RequiredArgsConstructor
public class DlqAdminController {

    private final SpringDlqRepository dlqRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Lista todas as mensagens atualmente na DLQ (persistidas no banco).
     */
    @GetMapping("/messages")
    public List<DlqMessageEntity> listDlqMessages() {
        return dlqRepository.findAll();
    }

    /**
     * Re-enfileira uma mensagem específica da DLQ para o seu tópico original para reprocessamento.
     */
    @PostMapping("/reprocess/{id}")
    public ResponseEntity<Void> reprocessMessage(@PathVariable UUID id) {
        return dlqRepository.findById(id)
                .map(message -> {
                    // Publica a mensagem de volta ao seu tópico original
                    kafkaTemplate.send(message.getOriginalTopic(), message.getMessageKey(), message.getPayload());
                    // Marca a mensagem como reprocessada
                    message.setReprocessed(true);
                    dlqRepository.save(message);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Descarta uma mensagem da DLQ (remove do banco) que não pode ser reprocessada.
     */
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Void> discardMessage(@PathVariable UUID id) {
        if (!dlqRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        dlqRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}