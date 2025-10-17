package br.com.orionpay.authorizationservice.config;

import br.com.orionpay.authorizationservice.authorization.adapters.in.web.interceptor.IdempotencyInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final IdempotencyInterceptor idempotencyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Registra o interceptor para ser aplicado a todas as requisições de pagamento
        registry.addInterceptor(idempotencyInterceptor).addPathPatterns("/v1/payments/**");
    }
}