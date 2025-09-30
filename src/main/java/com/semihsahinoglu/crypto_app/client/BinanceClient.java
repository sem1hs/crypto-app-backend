package com.semihsahinoglu.crypto_app.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BinanceClient {

    private final WebClient webClient;

    public Mono<List<List<Object>>> getCrypto(String symbol, String interval, long startTime, long endTime) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v3/klines")
                        .queryParam("symbol", symbol)
                        .queryParam("interval", interval)
                        .queryParam("startTime", startTime)
                        .queryParam("endTime", endTime)
                        .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<List<Object>>>() {});
    }
}
