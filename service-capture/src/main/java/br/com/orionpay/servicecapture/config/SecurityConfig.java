package br.com.orionpay.servicecapture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Protege todos os endpoints de admin, exigindo o papel 'ADMIN'
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll() // Permite acesso a outros endpoints (ex: actuator)
                )
                .httpBasic(Customizer.withDefaults()) // Usa autenticação Basic Auth para a API de admin
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // ATENÇÃO: Em produção, use um provedor de identidade real (LDAP, OAuth2, etc.)
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("supersecretpassword")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}