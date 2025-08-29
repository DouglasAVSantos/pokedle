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
        // Garante que a página exiba o histórico se o usuário recarregá-la
        List<PokemonResponse> history = (List<PokemonResponse>) session.getAttribute("history");
        if (history != null) {
            model.addAttribute("history", history);
        }

        addAvailablePokemonToModel(model, session);
        // Simplesmente retorna o nome do arquivo html em /resources/templates
        return "game";
    }

    @PostMapping("/guess")
    public String handleGuess(@RequestParam String pokemonName, Model model, HttpSession session) {
        // 1. Pega o histórico da sessão do usuário
        List<PokemonResponse> history = (List<PokemonResponse>) session.getAttribute("history");
        if (history == null) {
            history = new ArrayList<>();
        }

        try {
            // Chama o serviço com o nome do pokémon vindo do formulário
            PokemonResponse response = gameService.startGame(pokemonName.toLowerCase().trim());
            // 2. Adiciona o novo resultado no início da lista
            history.add(0, response);

        } catch (NotFoundException e) {
            // Se o pokémon não for encontrado, adiciona uma mensagem de erro ao model
            model.addAttribute("error", "Pokémon '" + pokemonName + "' não encontrado. Tente novamente!");
        }

        // 3. Salva o histórico atualizado de volta na sessão
        session.setAttribute("history", history);
        // 4. Adiciona a lista inteira ao model para o Thymeleaf usar
        model.addAttribute("history", history);

        addAvailablePokemonToModel(model, session);
        // Retorna para a mesma página, que agora terá os dados do resultado ou do erro
        return "game";
    }

    @GetMapping("/reset")
    public String resetGame(HttpSession session) {
        // Remove o histórico da sessão, efetivamente reiniciando o jogo
        session.removeAttribute("history");
        return "redirect:/";
    }

    private void addAvailablePokemonToModel(Model model, HttpSession session) {
        // 1. Busca a lista completa de Pokémon
        List<PokemonDropdownItemDTO> allPokemon = gameService.getPokemonListForDropdown();

        // 2. Pega o histórico para saber quais já foram palpitados
        List<PokemonResponse> history = (List<PokemonResponse>) session.getAttribute("history");

        if (history != null && !history.isEmpty()) {
            // 3. Extrai os nomes dos Pokémon já palpitados (em minúsculas)
            Set<String> guessedNames = history.stream()
                    .map(response -> response.getNome().get("valor").toLowerCase())
                    .collect(Collectors.toSet());

            // 4. Filtra a lista completa, removendo os que já foram palpitados
            List<PokemonDropdownItemDTO> availablePokemon = allPokemon.stream()
                    .filter(pokemon -> !guessedNames.contains(pokemon.getName().toLowerCase()))
                    .collect(Collectors.toList());

            model.addAttribute("pokemonList", availablePokemon);
        } else {
            // Se não há histórico, usa a lista completa
            model.addAttribute("pokemonList", allPokemon);
        }
    }
}