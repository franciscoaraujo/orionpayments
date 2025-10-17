package br.com.orionpay.authorizationservice.authorization.adapters.out.cardbrand;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class CardBrandAuthorizationPayment {


    private final Counter fallbackCounter;

    public CardBrandAuthorizationPayment(MeterRegistry meterRegistry) {
        this.fallbackCounter = Counter.builder("pagamentos.bandeira.fallback.total")
                .description("Total de vezes que o fallback de comunicação com a bandeira foi acionado")
                .tag("bandeira", "geral") // Poderíamos ter tags para "visa", "mastercard", etc.
                .register(meterRegistry);
    }

    @CircuitBreaker(name = "bandeira", fallbackMethod = "autorizacaoFallback")
    @Retry(name = "bandeira")
    @TimeLimiter(name = "bandeira")
    public CompletableFuture<Boolean> authorizationPaymentExtern(String bandeira) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Contactando sistema da bandeira", bandeira);
            try {
                // Simula uma chamada de rede que pode demorar
                int delay = ThreadLocalRandom.current().nextInt(100, 5000);
                Thread.sleep(delay);

                // Simula uma falha em 30% das vezes
                if (ThreadLocalRandom.current().nextInt(10) < 3) {
                    throw new RuntimeException("Falha de comunicação com a bandeira");
                }
                log.info("Pagamento autorizado pela bandeira e banco emissor.");

                return true;


            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Fallback agora com logging e métricas.
     */
    private CompletableFuture<Boolean> autorizacaoFallback(String bandeira, Throwable t) { // Corrected signature
        log.warn("Fallback de autorização da bandeira acionado para bandeira {}. Motivo: {}", bandeira, t.getMessage());

        // 2. Emitir Métrica
        this.fallbackCounter.increment();

        // 3. Retornar Falha
        // Para a API, isso deve ser traduzido em um erro HTTP apropriado,
        // como 503 Service Unavailable, com uma mensagem clara para o lojista.
        return CompletableFuture.completedFuture(false);
    }
}
