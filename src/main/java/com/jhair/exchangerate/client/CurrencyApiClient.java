package com.jhair.exchangerate.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.jhair.exchangerate.config.ExternalApiProperties;
import com.jhair.exchangerate.model.dto.ApiExchangeResponseDTO;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CurrencyApiClient {
    
    private final WebClient.Builder webClientBuilder;
    private final ExternalApiProperties externalApiProperties;

    public Mono<ApiExchangeResponseDTO> fetchExternalRate(String originCurrency, String finalCurrency){
        String url = externalApiProperties.getUrl() + "?base=" + originCurrency + "&symbols=" + finalCurrency;

        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(ApiExchangeResponseDTO.class);
    }
}
