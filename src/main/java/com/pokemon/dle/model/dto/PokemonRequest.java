package com.pokemon.dle.model.dto;

public record PokemonRequest(
        String nome,
        String tipo,
        Double peso,
        Integer fase
) {
}
