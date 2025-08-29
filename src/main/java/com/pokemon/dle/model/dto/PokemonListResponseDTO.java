package com.pokemon.dle.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonListResponseDTO {
    private List<PokemonListItemDTO> results;
}