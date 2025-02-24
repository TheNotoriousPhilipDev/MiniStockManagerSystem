package org.agomez.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SampleGlobalFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(SampleGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Pre-Request: Executing global filter before the request");

        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header("token", "123456")
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

        return chain.filter(mutatedExchange).then(Mono.fromRunnable(() -> {
            logger.info("Post-Request: Executing global filter after the request or post response");

            String token = exchange.getRequest().getHeaders().getFirst("token");
            if (token != null) {
                logger.info("Token: {}", token);
                mutatedExchange.getResponse().getHeaders().add("token", token);
            }

            mutatedExchange.getResponse().getCookies()
                    .add("color", ResponseCookie.from("color", "red").build());
            mutatedExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        }));
    }

    @Override
    public int getOrder() {
        return 100;
    }
}