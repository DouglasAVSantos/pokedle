package com.pokemon.dle.utility;

import com.pokemon.dle.client.PokemonApiClient;
import com.pokemon.dle.model.dto.PokemonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;
import java.util.function.Supplier;

@Component
public class PokemonDaily {

    private final PokemonApiClient pokemonApiClient;
    private final Supplier<Integer> idGenerator;
    private LocalDate currentDate;
    private Integer id;
    private PokemonDTO pokemonDto;

    @Autowired
    public PokemonDaily(PokemonApiClient pokemonApiClient) {
        this(pokemonApiClient, () -> new Random().nextInt(150) + 1, LocalDate.now());
    }

    public PokemonDaily(PokemonApiClient pokemonApiClient, Supplier<Integer> idGenerator, LocalDate currentDate) {
        this.pokemonApiClient = pokemonApiClient;
        this.idGenerator = idGenerator;
        this.currentDate = currentDate;
        refreshIfNeeded(currentDate);
    }

    public Integer getId() {
        refreshIfNeeded(LocalDate.now());
        return id;
    }

    public PokemonDTO buscarPokemonDoDia() {
        refreshIfNeeded(LocalDate.now());
        return pokemonDto;
    }

    void refreshIfNeeded(LocalDate requestedDate) {
        if (pokemonDto == null || currentDate == null || !currentDate.equals(requestedDate)) {
            currentDate = requestedDate;
            id = idGenerator.get();
            pokemonDto = pokemonApiClient.get("/pokemon/" + id, PokemonDTO.class);
        }
    }
}
