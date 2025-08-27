package com.pokemon.dle.utility;

import com.pokemon.dle.client.PokemonApiClient;
import com.pokemon.dle.model.dto.PokemonDTO;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PokemonDaily {

    private final Integer id;
    private final PokemonApiClient pokemonApiClient;
    private final PokemonDTO pokemonDto;

    public PokemonDaily(PokemonApiClient pokemonApiClient) {
        this.pokemonApiClient = pokemonApiClient;
        id = new Random().nextInt(150) + 1;
        this.pokemonDto = pokemonApiClient.get("/pokemon/" + id, PokemonDTO.class);
    }

    public Integer getId() {
        return id;
    }

    public PokemonDTO buscarPokemonDoDia() {
        return pokemonDto;
    }
}
