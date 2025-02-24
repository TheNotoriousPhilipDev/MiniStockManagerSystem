package org.agomez.msvc.items.clients;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> customizerCircuitBreaker() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.
                        custom()
                        .slidingWindowSize(10) //de 10 llamadas
                        .failureRateThreshold(50) //50% de fallos
                        .waitDurationInOpenState(Duration.ofSeconds(10L))//entramos al estado abierto del circuito y permanecemos ahí durante 10s
                        .permittedNumberOfCallsInHalfOpenState(5)//luego de esos 10s entramos a semiabierto y permitimos 5 llamadas
                        .slowCallDurationThreshold(Duration.ofSeconds(2L))//si una llamada tarda más de 2s
                        .slowCallRateThreshold(50) // si todas las llamadas de las 10 tardan más de 2s, se abre el circuito,
                        .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(4L)) //si el timeout tiene la misma duración de una llamada lenta se le dará prioridad al timeout
                        .build())
                .build());
    }
}
