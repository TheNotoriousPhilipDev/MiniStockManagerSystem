package org.agomez.msvc.items.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient webClient(WebClient.Builder webClientBuilder,
                        @Value("${config.base-url.endpoint.msvcProducts}") String url,
                        ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        return webClientBuilder
                .baseUrl(url)
                .filter(lbFunction)
                .build();
    }

    /*
    @Bean
    @LoadBalanced
    WebClient.Builder webClientBuilder() {

        return WebClient.builder().baseUrl(url); //option to set up the config in the properties file

        return WebClient.builder().baseUrl("http://msvc-products/api/v1/products");
        return WebClient.builder().baseUrl("");
          if we opt for the second option, we need to add the url in the method call wherever it is used
          in our case we are using the url in the ItemServiceWebClient class, so we need to add the whole url in the get method call
        */
}
