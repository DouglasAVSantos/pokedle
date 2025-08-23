package com.pokemon.dle.controller;


import com.pokemon.dle.model.dto.SpeciesDTO;
import com.pokemon.dle.model.dto.PokemonEvolutionPhaseDTO;
import com.pokemon.dle.model.dto.PokemonResponse;
import com.pokemon.dle.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping
    public ResponseEntity<PokemonResponse> buscarPokemon(){
        return ResponseEntity.ok(pokemonService.buscarPokemonDoDia());
    }

    @GetMapping(path = "/species")
    public ResponseEntity<SpeciesDTO> buscarPokemonSpecies(){
        return ResponseEntity.ok(pokemonService.buscarPokemonDoDiaSpecies());
    }

    @GetMapping(path = "/chain")
    public ResponseEntity<List<PokemonEvolutionPhaseDTO>> chain(){
        return ResponseEntity.ok(pokemonService.montarFases());
    }
}
