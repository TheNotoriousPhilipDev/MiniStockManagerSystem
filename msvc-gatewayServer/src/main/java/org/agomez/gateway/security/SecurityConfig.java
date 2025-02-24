package org.agomez.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                .pathMatchers(HttpMethod.GET, "/api/v1/items", "/api/v1/products", "/api/v1/users").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/v1/items/{id}", "/api/v1/products/{id}", "/api/v1/users/{id}")
                .hasAnyRole("ADMIN", "USER")
                .pathMatchers("/api/v1/items/**", "/api/v1/products/**", "/api/v1/users/**")
                .hasAnyRole("ADMIN", "USER")
                .anyExchange().authenticated())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(source -> {
                            Collection<String> roles = source.getClaimAsStringList("roles");
                            Collection<GrantedAuthority> grantedAuthorities = roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
                            return Mono.just(new JwtAuthenticationToken(source, grantedAuthorities));
                        })))
                .build();
    }
}
