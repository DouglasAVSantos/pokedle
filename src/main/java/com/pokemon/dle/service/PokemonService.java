package com.pokemon.dle.service;

import com.pokemon.dle.model.dto.EvolutionDTO;
import com.pokemon.dle.model.dto.PokemonEvolutionPhaseDTO;
import com.pokemon.dle.model.dto.PokemonResponse;
import com.pokemon.dle.model.dto.SpeciesDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class PokemonService {

    private final WebClient webClient;
    private final Integer id;

    public PokemonService(WebClient webClient) {
        this.webClient = webClient;
        this.id = (new Random().nextInt(150)) + 1;
    }


    public PokemonResponse buscarPokemonDoDia() {
        return webClient
                .get()
                .uri("/pokemon/" + id)
                .retrieve()
                .bodyToMono(PokemonResponse.class)
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
}





