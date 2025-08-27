package com.pokemon.dle.service;

import com.pokemon.dle.configuration.exception.NotFoundException;
import com.pokemon.dle.model.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;


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


    public List<PokemonEvolutionPhaseDTO> montarFases() {
        EvolutionDTO.Chain chain = buscarevolucao().getChain();
        List<PokemonEvolutionPhaseDTO> lista = new ArrayList<>();
        percorrer(chain.getSpecies(), chain.getEvolvesTo(), 0, lista);
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

    public PokemonResponse respostaPokemonDoDia() {
        SpeciesDTO specieDTO = buscarPokemonDoDiaSpecies();
        PokemonTranslator ptBr = new PokemonTranslator();
        return new PokemonResponse(

                pokemonDoDia.getName(),
                PokemonTranslator.traduzirTipo(pokemonDoDia.getTypes().get(0).getType().getName()),
                PokemonTranslator.traduzirTipo(pokemonDoDia.getTypes().size() > 1 ? pokemonDoDia.getTypes().get(1).getType().getName() : "sem tipo 2"),
                pokemonDoDia.getPeso().toString(),
                pokemonDoDia.getAltura().toString(),
                PokemonTranslator.traduzirCor(specieDTO.getColor()),
                PokemonTranslator.traduzirHabitat(specieDTO.getHabitat()),
                montarFases().stream()
                        .filter(p -> p.getNome().equals(buscarPokemonDoDia().getName()))
                        .findFirst().map(PokemonEvolutionPhaseDTO::getFase)
                        .orElse(0)
        );
    }
    public List<PokemonResponse> pokemonRequest(String pokemon) {
        PokemonTranslator ptBr = new PokemonTranslator();
        PokemonResponse pokeDoDia = respostaPokemonDoDia();
        PokemonDTO pokeDTORequest ;
        SpeciesDTO specieDTORequest;

        try{
           pokeDTORequest = buscarPokemonDoDia(pokemon);
           specieDTORequest = buscarPokemonDoDiaSpeciesPeloNome(pokemon);
        } catch (RuntimeException e) {
            throw new NotFoundException("Pokemon não encontrado");
        }

        PokemonResponse pokeDoDiaRequest = new PokemonResponse(

                pokeDTORequest.getName().substring(0,1).toUpperCase()+pokeDTORequest.getName().substring(1),
                PokemonTranslator.traduzirTipo(pokeDTORequest.getTypes().get(0).getType().getName()),
                PokemonTranslator.traduzirTipo(pokeDTORequest.getTypes().size() > 1 ? pokeDTORequest.getTypes().get(1).getType().getName() : "sem tipo 2"),
                pokeDTORequest.getPeso().toString(),
                pokeDTORequest.getAltura().toString(),
                PokemonTranslator.traduzirCor(specieDTORequest.getColor()),
                PokemonTranslator.traduzirHabitat(specieDTORequest.getHabitat()),
                montarFases().stream()
                        .filter(p -> p.getNome().equals(buscarPokemonDoDia().getName()))
                        .findFirst().map(PokemonEvolutionPhaseDTO::getFase)
                        .orElse(0)
        );

        if(!pokeDoDiaRequest.equals(pokeDoDia)){
        List<PokemonResponse> lista = new ArrayList<>();
        lista.add(verificaDTO(pokeDoDia,pokeDoDiaRequest));
        lista.add(pokeDoDiaRequest);
        return lista;
        }
        List<PokemonResponse> lista = new ArrayList<>();
        lista.add(pokeDoDia);
        lista.add(pokeDoDiaRequest);
        return lista;
    }

    public PokemonResponse verificaDTO(PokemonResponse dto1, PokemonResponse dto2){
        String altura = "";
        String peso = "";
        if(Double.parseDouble(dto1.getAltura()) > Double.parseDouble(dto2.getAltura())){
          altura =  "MAIS ALTO";
        }
        if(Double.parseDouble(dto1.getAltura()) < Double.parseDouble(dto2.getAltura())){
           altura = "MAIS BAIXO";
        }

        if(Double.parseDouble(dto1.getPeso()) > Double.parseDouble(dto2.getPeso())){
            peso =  "MAIS PESADO";
        }
        if(Double.parseDouble(dto1.getPeso()) < Double.parseDouble(dto2.getPeso())){
            peso = "MAIS LEVE";
        }

       return new PokemonResponse(
         dto1.getNome().equals(dto2.getNome()) ? dto1.getNome() : "ERRADO",
         dto1.getTipo1().equals(dto2.getTipo1()) ? dto1.getTipo1() : "ERRADO",
         dto1.getTipo2().equals(dto2.getTipo2()) ? dto1.getTipo2() : "ERRADO",
         dto1.getPeso().equals(dto2.getPeso()) ? dto1.getPeso() : peso,
        dto1.getAltura().equals(dto2.getAltura()) ? dto1.getAltura() : altura,
         dto1.getCor().equals(dto2.getCor()) ? dto1.getCor() : "ERRADO",
         dto1.getHabitat().equals(dto2.getHabitat()) ? dto1.getHabitat() : "ERRADO",
         dto1.getFase() == dto2.getFase() ? dto1.getFase() : 00
        );
    }
}





