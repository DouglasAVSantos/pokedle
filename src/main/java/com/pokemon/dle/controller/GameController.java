package com.pokemon.dle.controller;

import com.pokemon.dle.exception.NotFoundException;
import com.pokemon.dle.model.dto.PokemonDropdownItemDTO;
import com.pokemon.dle.model.dto.PokemonResponse;
import com.pokemon.dle.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/")
    public String showGamePage(HttpSession session, Model model) {
        List<PokemonResponse> history = (List<PokemonResponse>) session.getAttribute("history");
        if (history != null) {
            model.addAttribute("history", history);
        }

        addAvailablePokemonToModel(model, session);
        return "game";
    }

    @PostMapping("/guess")
    public String handleGuess(@RequestParam String pokemonName, Model model, HttpSession session) {
        List<PokemonResponse> history = (List<PokemonResponse>) session.getAttribute("history");
        if (history == null) {
            history = new ArrayList<>();
        }

        try {
            PokemonResponse response = gameService.startGame(pokemonName.toLowerCase().trim());
            history.add(0, response);

        } catch (NotFoundException e) {
            model.addAttribute("error", "Pokémon '" + pokemonName + "' não encontrado. Tente novamente!");
        }

        session.setAttribute("history", history);
        model.addAttribute("history", history);

        addAvailablePokemonToModel(model, session);
        return "game";
    }

    @GetMapping("/reset")
    public String resetGame(HttpSession session) {
        session.removeAttribute("history");
        return "redirect:/";
    }

    private void addAvailablePokemonToModel(Model model, HttpSession session) {
        List<PokemonDropdownItemDTO> allPokemon = gameService.getPokemonListForDropdown();

        List<PokemonResponse> history = (List<PokemonResponse>) session.getAttribute("history");

        if (history != null && !history.isEmpty()) {
            Set<String> guessedNames = history.stream()
                    .map(response -> response.getNome().get("valor").toLowerCase())
                    .collect(Collectors.toSet());

            List<PokemonDropdownItemDTO> availablePokemon = allPokemon.stream()
                    .filter(pokemon -> !guessedNames.contains(pokemon.getName().toLowerCase()))
                    .collect(Collectors.toList());

            model.addAttribute("pokemonList", availablePokemon);
        } else {
            model.addAttribute("pokemonList", allPokemon);
        }
    }
}