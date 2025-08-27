package com.pokemon.dle.service;

import com.pokemon.dle.model.dto.EvolutionDTO;
import com.pokemon.dle.model.dto.PokemonEvolutionPhaseDTO;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;


@Data
@Service
public class EvolutionService {
    private final SpeciesService speciesService;
    private final WebClient webClient;

    public EvolutionService(WebClient webClient, SpeciesService speciesService) {
        this.webClient = webClient;
        this.speciesService = speciesService;
    }

    public EvolutionDTO buscarevolucao(String url) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(EvolutionDTO.class)
                .block();
    }


    public List<PokemonEvolutionPhaseDTO> montarFases(String url) {
        EvolutionDTO.Chain chain = buscarevolucao(url).getChain();
        List<PokemonEvolutionPhaseDTO> lista = new ArrayList<>();
        percorrer(chain.getSpecies(), chain.getEvolvesTo(), 0, lista);
        System.out.println(lista);
        return lista;
    }

    private void percorrer(EvolutionDTO.Species species, List<EvolutionDTO.EvolvesTo> evolutions, int fase, List<PokemonEvolutionPhaseDTO> lista) {
        String geracao = speciesService.buscarPokemonDoDiaSpecies(species.getUrl()).getGeneration();
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
