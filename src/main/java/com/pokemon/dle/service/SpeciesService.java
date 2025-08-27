package com.pokemon.dle.service;

import com.pokemon.dle.client.PokemonApiClient;
import com.pokemon.dle.model.dto.SpeciesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpeciesService {

    private final PokemonApiClient pokemonApiClient;


    public SpeciesDTO buscarPokemonDoDiaSpeciesPeloNome(String pokemon) {
        return pokemonApiClient.get("/pokemon-species/" + pokemon, SpeciesDTO.class);
    }

    public SpeciesDTO buscarPokemonDoDiaSpecies(String url) {
        return pokemonApiClient.get(url, SpeciesDTO.class);
    }

    public SpeciesDTO buscarPokemonDoDiaSpecies(int id) {
        return pokemonApiClient.get("/pokemon-species/" + id, SpeciesDTO.class);
    }

    public String retornaUriEvolucao(int id) {
        SpeciesDTO especie = buscarPokemonDoDiaSpecies(id);
        return especie.getEvolutionChain();
    }
}
