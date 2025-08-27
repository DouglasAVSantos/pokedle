package com.pokemon.dle.controller;


import com.pokemon.dle.model.dto.PokemonResponse;
import com.pokemon.dle.model.dto.SpeciesDTO;
import com.pokemon.dle.model.dto.PokemonEvolutionPhaseDTO;
import com.pokemon.dle.model.dto.PokemonDTO;
import com.pokemon.dle.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;
    private final List<String>chutesUsados = new ArrayList<>();
    private final Map<Object,Object>historico = new HashMap<>();

    @GetMapping
    public ResponseEntity<PokemonResponse> buscarPokemon(){
        return ResponseEntity.ok(pokemonService.respostaPokemonDoDia());
    }

    @PostMapping(path = "/jogar")
    public ResponseEntity<List<PokemonResponse>> buscarPokemon(@RequestParam("pokemon") String id) throws BadRequestException {
        if(chutesUsados.contains(id)){
            throw new BadRequestException("esse pokemon ja foi usado");
        }
        return ResponseEntity.ok(pokemonService.pokemonRequest(id));
    }
}
