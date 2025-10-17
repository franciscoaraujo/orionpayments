package br.com.orionpay.authorizationservice.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Define as regras de autorização para cada endpoint
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público aos endpoints do Actuator (saúde, etc.)
                        .requestMatchers("/actuator/**").permitAll()
                        // Exige que qualquer outra requisição seja autenticada
                        .anyRequest().authenticated()
                )
                // 2. Configura o serviço como um OAuth2 Resource Server, habilitando a validação de JWT
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // 3. Como é uma API REST stateless, desabilitamos o CSRF
                .csrf(csrf -> csrf.disable())
                // 4. Garantimos que nenhuma sessão de usuário será criada no servidor
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}