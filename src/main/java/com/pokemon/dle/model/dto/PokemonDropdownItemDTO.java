package com.pokemon.dle.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PokemonDropdownItemDTO {
    private String name;
    private String imageUrl;
}