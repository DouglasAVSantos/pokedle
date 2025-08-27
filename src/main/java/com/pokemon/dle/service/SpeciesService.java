package com.pokemon.dle.service;

import com.pokemon.dle.model.dto.SpeciesDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SpeciesService {

    private final WebClient webClient;

    public SpeciesService(WebClient webClient) {
        this.webClient = webClient;

    }

    public SpeciesDTO buscarPokemonDoDiaSpeciesPeloNome(String pokemon) {
        return webClient
                .get()
                .uri("/pokemon-species/" + pokemon)
                .retrieve()
                .bodyToMono(SpeciesDTO.class)
                .block();
    }

    public SpeciesDTO buscarPokemonDoDiaSpecies(String url) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(SpeciesDTO.class)
                .block();
    }

    public SpeciesDTO buscarPokemonDoDiaSpecies(int id) {
        return webClient
                .get()
                .uri("/pokemon-species/" + id)
                .retrieve()
                .bodyToMono(SpeciesDTO.class)
                .block();
    }

    public String retornaUriEvolucao(int id) {
        SpeciesDTO especie = buscarPokemonDoDiaSpecies(id);
        return especie.getEvolutionChain();
    }
}
