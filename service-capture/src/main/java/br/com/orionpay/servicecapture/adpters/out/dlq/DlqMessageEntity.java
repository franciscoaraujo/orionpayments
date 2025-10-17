package br.com.orionpay.servicecapture.adpters.out.dlq;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "dlq_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DlqMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String originalTopic;

    @Column(columnDefinition = "TEXT")
    private String messageKey;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(columnDefinition = "TEXT")
    private String exceptionMessage;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime failedAt;

    private boolean reprocessed;
}