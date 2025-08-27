package com.pokemon.dle.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PokemonApiClient {

    private final WebClient webClient;

    public PokemonApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> T get(String uri, Class<T> classe) {
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(classe)
                .block();
    }
}
