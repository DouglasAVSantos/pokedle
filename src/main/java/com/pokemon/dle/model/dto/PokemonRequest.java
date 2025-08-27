package com.pokemon.dle.model.dto;

import lombok.*;

import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PokemonRequest{
    private String nome;
    private String tipo2;
    private String tipo1;
    private String peso;
    private String altura;
    private String cor;
    private String habitat;
    private Integer fase;

}
