package br.com.orionpay.servicecapture.config.health;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaHealthIndicator implements HealthIndicator {

    private final KafkaAdmin kafkaAdmin;

    @Override
    public Health health() {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            // Tenta descrever o cluster com um timeout curto
            adminClient.describeCluster(new DescribeClusterOptions().timeoutMs(1000)).clusterId().get();
            return Health.up().withDetail("message", "Kafka broker is reachable").build();
        } catch (Exception e) {
            return Health.down(e).withDetail("message", "Kafka broker is unreachable").build();
        }
    }
}