package br.com.orionpay.authorizationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // HABILITAR AGENDAMENTO
@EnableCaching // HABILITAR O CACHE
public class OrionpayAuthorizationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrionpayAuthorizationServiceApplication.class, args);
    }

}
