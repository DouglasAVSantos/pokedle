package com.pokemon.dle.service;

import com.pokemon.dle.model.dto.EvolutionDTO;
import com.pokemon.dle.client.PokemonApiClient;
import com.pokemon.dle.model.dto.PokemonEvolutionPhaseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class EvolutionService {
    private final SpeciesService speciesService;
    private final PokemonApiClient pokemonApiClient;


    public EvolutionDTO buscarevolucao(String url) {
        return pokemonApiClient.get(url, EvolutionDTO.class);
    }


    public List<PokemonEvolutionPhaseDTO> montarFases(String url) {
        EvolutionDTO.Chain chain = buscarevolucao(url).getChain();
        List<PokemonEvolutionPhaseDTO> lista = new ArrayList<>();
        percorrer(chain.getSpecies(), chain.getEvolvesTo(), 0, lista);
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
