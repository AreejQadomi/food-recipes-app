package com.wiley.myfoodapp.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@AllArgsConstructor
public class SpoonacularConfig {

    private static final String API_KEY_HEADER = "x-api-key";
    private SpoonacularProperties properties;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl(properties.getApi().getBaseUrl())
            .defaultHeader(API_KEY_HEADER, properties.getApi().getKey())
            .build();
    }
}
