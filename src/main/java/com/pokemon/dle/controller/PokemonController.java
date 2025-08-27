package com.pokemon.dle.controller;


import com.pokemon.dle.exception.NotFoundException;
import com.pokemon.dle.model.dto.PokemonRequest;
import com.pokemon.dle.model.dto.PokemonResponse;
import com.pokemon.dle.service.GameService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/pokemon")
public class PokemonController {

    private final GameService gameService;
    private final List<String> chutesUsados = new ArrayList<>();
    private final List<PokemonResponse> historico = new ArrayList<>();

    public PokemonController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<PokemonRequest> buscarPokemon() {
        return ResponseEntity.ok(gameService.pokemonDoDiaRequest());
    }

    @PostMapping(path = "/jogar")
    public ResponseEntity<?> buscarPokemon(@RequestParam("pokemon") String id) throws BadRequestException {
        if (chutesUsados.contains(id)) {
            throw new BadRequestException("esse pokemon ja foi usado");
        }
        chutesUsados.add(id);
        historico.add(gameService.startGame(id));
        Collections.reverse(historico);
        return ResponseEntity.ok(historico);
    }
}
