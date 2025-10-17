package br.com.orionpay.authorizationservice.authorization.adapters.in.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class IdempotencyInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate redisTemplate;
    private static final String IDEMPOTENCY_KEY_HEADER = "Idempotency-Key";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Só aplicamos a lógica para métodos que modificam dados, como POST
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        final String idempotencyKey = request.getHeader(IDEMPOTENCY_KEY_HEADER);
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            log.warn("Idempotency-Key header está ausente em uma requisição POST.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "O header Idempotency-Key é obrigatório.");
            return false;
        }

        String key = "idempotency:" + idempotencyKey;

        // Tenta adquirir um "lock". Se a chave já existir, significa que a requisição está em processamento ou já foi processada.
        Boolean isNew = redisTemplate.opsForValue().setIfAbsent(key, "PROCESSING", 24, TimeUnit.HOURS);

        if (Boolean.FALSE.equals(isNew)) {
            log.warn("Requisição duplicada detectada com a chave de idempotência: {}", idempotencyKey);
            response.sendError(HttpServletResponse.SC_CONFLICT, "Requisição duplicada."); // 409 Conflict
            return false;
        }

        // Armazena a chave no request para uso posterior
        request.setAttribute("idempotencyKey", key);
        return true;
    }
}
