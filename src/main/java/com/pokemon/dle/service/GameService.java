package com.pokemon.dle.service;

import com.pokemon.dle.client.PokemonApiClient;
import com.pokemon.dle.exception.NotFoundException;
import com.pokemon.dle.model.dto.*;
import com.pokemon.dle.utility.PokemonDaily;
import com.pokemon.dle.utility.Translator;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Data
@RequiredArgsConstructor
public class GameService {

    private final SpeciesService speciesService;
    private final EvolutionService evolutionService;
    private final PokemonApiClient pokemonApiClient;
    private final Translator translator;
    private final PokemonDaily pokemonDaily;


    public PokemonRequest pokemonDoDiaRequest() {
        SpeciesDTO specieDTO = speciesService.buscarPokemonDoDiaSpecies(pokemonDaily.getId());
        return new PokemonRequest(
                pokemonDaily.buscarPokemonDoDia().getId(),
                pokemonDaily.buscarPokemonDoDia().getName(),
                translator.traduzirTipo(pokemonDaily.buscarPokemonDoDia().getTypes().get(0).getType().getName()),
                translator.traduzirTipo(pokemonDaily.buscarPokemonDoDia().getTypes().size() > 1 ? pokemonDaily.buscarPokemonDoDia().getTypes().get(1).getType().getName() : "sem tipo 2"),
                pokemonDaily.buscarPokemonDoDia().getPeso().toString(),
                pokemonDaily.buscarPokemonDoDia().getAltura().toString(),
                translator.traduzirCor(specieDTO.getColor()),
                translator.traduzirHabitat(specieDTO.getHabitat()),
                evolutionService.montarFases(speciesService.retornaUriEvolucao(pokemonDaily.getId())).stream()
                        .filter(p -> p.getNome().equals(pokemonDaily.buscarPokemonDoDia().getName()))
                        .findFirst().map(PokemonEvolutionPhaseDTO::getFase)
                        .orElse(0)
        );

    }

    public PokemonResponse startGame(String pokemon) {
        PokemonRequest pokemonDoDiaRequest = pokemonDoDiaRequest();
        PokemonDTO pokemonDoJogadorDTO;
        SpeciesDTO speciesDoPokemonDoJogadorDTO;

        try {
            pokemonDoJogadorDTO = pokemonApiClient.get("/pokemon/" + pokemon, PokemonDTO.class);
            speciesDoPokemonDoJogadorDTO = speciesService.buscarPokemonDoDiaSpeciesPeloNome(pokemon);
        } catch (RuntimeException e) {
            throw new NotFoundException("Pokemon não encontrado");
        }

        PokemonRequest pokemonDoJogadorRequest = new PokemonRequest(
                pokemonDoJogadorDTO.getId(),
                pokemonDoJogadorDTO.getName(),
                translator.traduzirTipo(pokemonDoJogadorDTO.getTypes().get(0).getType().getName()),
                translator.traduzirTipo(pokemonDoJogadorDTO.getTypes().size() > 1 ? pokemonDoJogadorDTO.getTypes().get(1).getType().getName() : "sem tipo 2"),
                pokemonDoJogadorDTO.getPeso().toString(),
                pokemonDoJogadorDTO.getAltura().toString(),
                translator.traduzirCor(speciesDoPokemonDoJogadorDTO.getColor()),
                translator.traduzirHabitat(speciesDoPokemonDoJogadorDTO.getHabitat()),
                evolutionService.montarFases(speciesDoPokemonDoJogadorDTO.getEvolutionChain()).stream()
                        .filter(p -> p.getNome().equals(pokemonDoJogadorDTO.getName()))
                        .findFirst().map(PokemonEvolutionPhaseDTO::getFase)
                        .orElse(0)
        );

        if (pokemonDoJogadorRequest.equals(pokemonDoDiaRequest)) {
            return new PokemonResponse(pokemonDoDiaRequest, pokemonDoJogadorRequest);
        }

        return verificaDTO(pokemonDoDiaRequest, pokemonDoJogadorRequest);
    }

    public PokemonResponse verificaDTO(PokemonRequest pokemonCerto, PokemonRequest pokemonTentativaPlayer) {
        String alturaStatus;
        // O pokémon correto é mais alto que a tentativa, então a seta deve apontar para cima.
        if (Double.parseDouble(pokemonCerto.getAltura()) > Double.parseDouble(pokemonTentativaPlayer.getAltura())) {
            alturaStatus = "higher";
        // O pokémon correto é mais baixo que a tentativa, então a seta deve apontar para baixo.
        } else if (Double.parseDouble(pokemonCerto.getAltura()) < Double.parseDouble(pokemonTentativaPlayer.getAltura())) {
            alturaStatus = "lower";
        } else {
            alturaStatus = "correct";
        }

        String pesoStatus;
        // O pokémon correto é mais pesado que a tentativa, então a seta deve apontar para cima.
        if (Double.parseDouble(pokemonCerto.getPeso()) > Double.parseDouble(pokemonTentativaPlayer.getPeso())) {
            pesoStatus = "higher";
        // O pokémon correto é mais leve que a tentativa, então a seta deve apontar para baixo.
        } else if (Double.parseDouble(pokemonCerto.getPeso()) < Double.parseDouble(pokemonTentativaPlayer.getPeso())) {
            pesoStatus = "lower";
        } else {
            pesoStatus = "correct";
        }

        return new PokemonResponse(
            Map.of(
                    "mensagem", "Tente novamente!"),
            Map.of(
                    "valor", String.format("https://www.pokemon.com/static-assets/content-assets/cms2/img/pokedex/full/%03d.png", pokemonTentativaPlayer.getId())
            ),
            Map.of(
                    "valor", pokemonTentativaPlayer.getNome(),
                    "status", pokemonCerto.getNome().equals(pokemonTentativaPlayer.getNome()) ? "correct" : "wrong"
            ),
            Map.of(
                    "valor", pokemonTentativaPlayer.getTipo1(),
                    "status", pokemonCerto.getTipo1().equals(pokemonTentativaPlayer.getTipo1()) ? "correct" : "wrong"
            ),
            Map.of(
                    "valor", pokemonTentativaPlayer.getTipo2(),
                    "status", pokemonCerto.getTipo2().equals(pokemonTentativaPlayer.getTipo2()) ? "correct" : "wrong"
            ),
            Map.of(
                    "valor", pokemonTentativaPlayer.getPeso(),
                    "status", pesoStatus
            ),
            Map.of(
                    "valor", pokemonTentativaPlayer.getAltura(),
                    "status", alturaStatus
            ),
            Map.of(
                    "valor", pokemonTentativaPlayer.getCor(),
                    "status", pokemonCerto.getCor().equals(pokemonTentativaPlayer.getCor()) ? "correct" : "wrong"
            ),
            Map.of(
                    "valor", pokemonTentativaPlayer.getHabitat(),
                    "status", pokemonCerto.getHabitat().equals(pokemonTentativaPlayer.getHabitat()) ? "correct" : "wrong"
            ),
            Map.of(
                    "valor", pokemonTentativaPlayer.getFase().toString(),
                    "status", pokemonCerto.getFase().equals(pokemonTentativaPlayer.getFase()) ? "correct" : "wrong"
            )
        );
    }
}
