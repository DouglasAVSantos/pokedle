package com.pokemon.dle.service;

import com.pokemon.dle.configuration.exception.NotFoundException;
import com.pokemon.dle.model.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Service
public class PokemonService {

    private final WebClient webClient;
    private final Integer id;
    private final PokemonDTO pokemonDoDia;

    public PokemonService(WebClient webClient) {
        this.webClient = webClient;
        this.id = (new Random().nextInt(150)) + 1;
        pokemonDoDia = this.buscarPokemonDoDia();
    }


    public PokemonDTO buscarPokemonDoDia() {
        return webClient
                .get()
                .uri("/pokemon/" + id)
                .retrieve()
                .bodyToMono(PokemonDTO.class)
                .block();
    }

    public PokemonDTO buscarPokemonDoDia(String pokemon) {
        return webClient
                .get()
                .uri("/pokemon/" + pokemon)
                .retrieve()
                .bodyToMono(PokemonDTO.class)
                .block();
    }

    public SpeciesDTO buscarPokemonDoDiaSpecies() {
        return webClient
                .get()
                .uri("/pokemon-species/" + id)
                .retrieve()
                .bodyToMono(SpeciesDTO.class)
                .block();
    }

    public SpeciesDTO buscarPokemonDoDiaSpeciesPeloNome(String pokemon) {
        return webClient
                .get()
                .uri("/pokemon-species/" + pokemon)
                .retrieve()
                .bodyToMono(SpeciesDTO.class)
                .block();
    }

    public SpeciesDTO buscarPokemonDoDiaSpecies(String url) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(SpeciesDTO.class)
                .block();
    }

    public EvolutionDTO buscarevolucao() {
        return webClient
                .get()
                .uri(buscarPokemonDoDiaSpecies().getEvolutionChain())
                .retrieve()
                .bodyToMono(EvolutionDTO.class)
                .block();
    }

    public EvolutionDTO buscarevolucao(String url) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(EvolutionDTO.class)
                .block();
    }


    public List<PokemonEvolutionPhaseDTO> montarFases() {
        EvolutionDTO.Chain chain = buscarevolucao().getChain();
        List<PokemonEvolutionPhaseDTO> lista = new ArrayList<>();
        percorrer(chain.getSpecies(), chain.getEvolvesTo(), 0, lista);
        System.out.println(lista);
        return lista;
    }

    public List<PokemonEvolutionPhaseDTO> montarFases(String url) {
        EvolutionDTO.Chain chain = buscarevolucao(url).getChain();
        List<PokemonEvolutionPhaseDTO> lista = new ArrayList<>();
        percorrer(chain.getSpecies(), chain.getEvolvesTo(), 0, lista);
        System.out.println(lista);
        return lista;
    }

    private void percorrer(EvolutionDTO.Species species, List<EvolutionDTO.EvolvesTo> evolutions, int fase, List<PokemonEvolutionPhaseDTO> lista) {
        String geracao = buscarPokemonDoDiaSpecies(species.getUrl()).getGeneration();
        if (geracao.equals("generation-i")) {
            fase++;
            lista.add(new PokemonEvolutionPhaseDTO(species.getName(), fase));
        }

        if (evolutions != null) {
            for (EvolutionDTO.EvolvesTo evo : evolutions) {
                percorrer(evo.getSpecies(), evo.getEvolvesTo(), fase, lista);
            }
        }
    }

    public PokemonRequest respostaPokemonDoDia() {
        SpeciesDTO specieDTO = buscarPokemonDoDiaSpecies();
        PokemonTranslator ptBr = new PokemonTranslator();
        return new PokemonRequest(

                pokemonDoDia.getName(),
                PokemonTranslator.traduzirTipo(pokemonDoDia.getTypes().get(0).getType().getName()),
                PokemonTranslator.traduzirTipo(pokemonDoDia.getTypes().size() > 1 ? pokemonDoDia.getTypes().get(1).getType().getName() : "sem tipo 2"),
                pokemonDoDia.getPeso().toString(),
                pokemonDoDia.getAltura().toString(),
                PokemonTranslator.traduzirCor(specieDTO.getColor()),
                PokemonTranslator.traduzirHabitat(specieDTO.getHabitat()),
                montarFases().stream()
                        .filter(p -> p.getNome().equals(pokemonDoDia.getName()))
                        .findFirst().map(PokemonEvolutionPhaseDTO::getFase)
                        .orElse(0)
        );

    }

    public PokemonResponse pokemonRequest(String pokemon) {
        PokemonTranslator ptBr = new PokemonTranslator();
        PokemonRequest pokeDoDia = respostaPokemonDoDia();
        PokemonDTO pokeDTORequest;
        SpeciesDTO specieDTORequest;

        try {
            pokeDTORequest = buscarPokemonDoDia(pokemon);
            specieDTORequest = buscarPokemonDoDiaSpeciesPeloNome(pokemon);
        } catch (RuntimeException e) {
            throw new NotFoundException("Pokemon não encontrado");
        }

        PokemonRequest pokeDoDiaRequest = new PokemonRequest(

                pokeDTORequest.getName(),
                PokemonTranslator.traduzirTipo(pokeDTORequest.getTypes().get(0).getType().getName()),
                PokemonTranslator.traduzirTipo(pokeDTORequest.getTypes().size() > 1 ? pokeDTORequest.getTypes().get(1).getType().getName() : "sem tipo 2"),
                pokeDTORequest.getPeso().toString(),
                pokeDTORequest.getAltura().toString(),
                PokemonTranslator.traduzirCor(specieDTORequest.getColor()),
                PokemonTranslator.traduzirHabitat(specieDTORequest.getHabitat()),
                montarFases(specieDTORequest.getEvolutionChain()).stream()
                        .filter(p -> p.getNome().equals(pokeDTORequest.getName()))
                        .findFirst().map(PokemonEvolutionPhaseDTO::getFase)
                        .orElse(0)
        );
        System.out.println(pokeDoDiaRequest.equals(pokeDoDia));
        if (pokeDoDiaRequest.equals(pokeDoDia)) {
            System.out.println("aqui");
            return new PokemonResponse(pokeDoDia, pokeDoDiaRequest);
        }

        return verificaDTO(pokeDoDia, pokeDoDiaRequest);
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





