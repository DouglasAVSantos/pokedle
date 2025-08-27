package com.pokemon.dle.controller;


import com.pokemon.dle.configuration.exception.NotFoundException;
import com.pokemon.dle.model.dto.PokemonRequest;
import com.pokemon.dle.model.dto.PokemonResponse;
import com.pokemon.dle.service.PokemonService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;
    private final List<String> chutesUsados = new ArrayList<>();
    private final List<PokemonResponse> historico = new ArrayList<>();

    @GetMapping
    public ResponseEntity<PokemonRequest> buscarPokemon() {
        return ResponseEntity.ok(pokemonService.respostaPokemonDoDia());
    }

    @PostMapping(path = "/jogar")
    public ResponseEntity<?> buscarPokemon(@RequestParam("pokemon") String id) throws BadRequestException {
        if (chutesUsados.contains(id)) {
            throw new BadRequestException("esse pokemon ja foi usado");
        }
        try {
            chutesUsados.add(id);
            historico.add(pokemonService.pokemonRequest(id));
            Collections.reverse(historico);
            return ResponseEntity.ok(historico);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", e.getMessage(), "dica", "só é aceito pokemon da primeira geração"));//.body();
        }
    }
}
