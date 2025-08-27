package com.pokemon.dle.service;

import com.pokemon.dle.client.PokemonApiClient;
import com.pokemon.dle.exception.NotFoundException;
import com.pokemon.dle.model.dto.*;
import com.pokemon.dle.utility.Translator;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;


@Service
@Data
public class PokemonService {

    private final SpeciesService speciesService;
    private final EvolutionService evolutionService;
    private final PokemonApiClient pokemonApiClient;
    private final Integer id;
    private final PokemonDTO pokemonDoDia;


    public PokemonService(PokemonApiClient pokemonApiClient, SpeciesService speciesService, EvolutionService evolutionService) {
        this.pokemonApiClient = pokemonApiClient;
        this.speciesService = speciesService;
        this.evolutionService = evolutionService;
        this.id = (new Random().nextInt(150)) + 1;
        pokemonDoDia = this.buscarPokemonDoDia();
    }


    public PokemonDTO buscarPokemonDoDia() {
        return pokemonApiClient.get("/pokemon/" + id, PokemonDTO.class);
    }

    public PokemonDTO buscarPokemonDoDia(String pokemon) {
        return pokemonApiClient.get("/pokemon/" + pokemon, PokemonDTO.class);
    }

    public PokemonRequest pokemonDoDiaRequest() {
        SpeciesDTO specieDTO = speciesService.buscarPokemonDoDiaSpecies(id);
        Translator ptBr = new Translator();
        return new PokemonRequest(

                pokemonDoDia.getName(),
                Translator.traduzirTipo(pokemonDoDia.getTypes().get(0).getType().getName()),
                Translator.traduzirTipo(pokemonDoDia.getTypes().size() > 1 ? pokemonDoDia.getTypes().get(1).getType().getName() : "sem tipo 2"),
                pokemonDoDia.getPeso().toString(),
                pokemonDoDia.getAltura().toString(),
                Translator.traduzirCor(specieDTO.getColor()),
                Translator.traduzirHabitat(specieDTO.getHabitat()),
                evolutionService.montarFases(speciesService.retornaUriEvolucao(id)).stream()
                        .filter(p -> p.getNome().equals(pokemonDoDia.getName()))
                        .findFirst().map(PokemonEvolutionPhaseDTO::getFase)
                        .orElse(0)
        );

    }

    public PokemonResponse startGame(String pokemon) {
        Translator ptBr = new Translator();
        PokemonRequest pokemonDoDiaRequest = pokemonDoDiaRequest();
        PokemonDTO pokemonDoJogadorDTO;
        SpeciesDTO speciesDoPokemonDoJogadorDTO;

        try {
            pokemonDoJogadorDTO = buscarPokemonDoDia(pokemon);
            speciesDoPokemonDoJogadorDTO = speciesService.buscarPokemonDoDiaSpeciesPeloNome(pokemon);
        } catch (RuntimeException e) {
            throw new NotFoundException("Pokemon não encontrado");
        }

        PokemonRequest pokemonDoJogadorRequest = new PokemonRequest(

                pokemonDoJogadorDTO.getName(),
                Translator.traduzirTipo(pokemonDoJogadorDTO.getTypes().get(0).getType().getName()),
                Translator.traduzirTipo(pokemonDoJogadorDTO.getTypes().size() > 1 ? pokemonDoJogadorDTO.getTypes().get(1).getType().getName() : "sem tipo 2"),
                pokemonDoJogadorDTO.getPeso().toString(),
                pokemonDoJogadorDTO.getAltura().toString(),
                Translator.traduzirCor(speciesDoPokemonDoJogadorDTO.getColor()),
                Translator.traduzirHabitat(speciesDoPokemonDoJogadorDTO.getHabitat()),
                evolutionService.montarFases(speciesDoPokemonDoJogadorDTO.getEvolutionChain()).stream()
                        .filter(p -> p.getNome().equals(pokemonDoJogadorDTO.getName()))
                        .findFirst().map(PokemonEvolutionPhaseDTO::getFase)
                        .orElse(0)
        );
        System.out.println(pokemonDoJogadorRequest.equals(pokemonDoDiaRequest));
        if (pokemonDoJogadorRequest.equals(pokemonDoDiaRequest)) {
            System.out.println("aqui");
            return new PokemonResponse(pokemonDoDiaRequest, pokemonDoJogadorRequest);
        }

        return verificaDTO(pokemonDoDiaRequest, pokemonDoJogadorRequest);
    }

    public PokemonResponse verificaDTO(PokemonRequest pokemonCerto, PokemonRequest pokemonTentativaPlayer) {
        String altura = "";
        String peso = "";
        if (Double.parseDouble(pokemonCerto.getAltura()) > Double.parseDouble(pokemonTentativaPlayer.getAltura())) {
            altura = "MAIS ALTO";
        }
        if (Double.parseDouble(pokemonCerto.getAltura()) < Double.parseDouble(pokemonTentativaPlayer.getAltura())) {
            altura = "MAIS BAIXO";
        }

        if (Double.parseDouble(pokemonCerto.getPeso()) > Double.parseDouble(pokemonTentativaPlayer.getPeso())) {
            peso = "MAIS PESADO";
        }
        if (Double.parseDouble(pokemonCerto.getPeso()) < Double.parseDouble(pokemonTentativaPlayer.getPeso())) {
            peso = "MAIS LEVE";
        }

        return new PokemonResponse(
                Map.of(
                        "mensagem", "Erro"),
                Map.of(
                        "valor", pokemonTentativaPlayer.getNome(),
                        "status", pokemonCerto.getNome().equals(pokemonTentativaPlayer.getNome()) ? "ACERTO!" : "ERRADO"
                ),
                Map.of(
                        "valor", pokemonTentativaPlayer.getTipo1(),
                        "status", pokemonCerto.getTipo1().equals(pokemonTentativaPlayer.getTipo1()) ? "ACERTO!" : "ERRADO"
                ),
                Map.of(
                        "valor", pokemonTentativaPlayer.getTipo2(),
                        "status", pokemonCerto.getTipo2().equals(pokemonTentativaPlayer.getTipo2()) ? "ACERTO!" : "ERRADO"
                ),
                Map.of(
                        "valor", pokemonTentativaPlayer.getPeso(),
                        "status", pokemonCerto.getPeso().equals(pokemonTentativaPlayer.getPeso()) ? "ACERTO!" : peso
                ),
                Map.of(
                        "valor", pokemonTentativaPlayer.getAltura(),
                        "status", pokemonCerto.getAltura().equals(pokemonTentativaPlayer.getAltura()) ? "ACERTO!" : altura
                ),
                Map.of(
                        "valor", pokemonTentativaPlayer.getCor(),
                        "status", pokemonCerto.getCor().equals(pokemonTentativaPlayer.getCor()) ? "ACERTO!" : "ERRADO"
                ),
                Map.of(
                        "valor", pokemonTentativaPlayer.getHabitat(),
                        "status", pokemonCerto.getHabitat().equals(pokemonTentativaPlayer.getHabitat()) ? "ACERTO!" : "ERRADO"
                ),
                Map.of(
                        "valor", pokemonTentativaPlayer.getFase().toString(),
                        "status", pokemonCerto.getFase().equals(pokemonTentativaPlayer.getFase()) ? "ACERTO!" : "ERRADO"
                )
        );
    }
}





