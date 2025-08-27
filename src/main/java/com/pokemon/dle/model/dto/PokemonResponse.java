package com.pokemon.dle.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class PokemonResponse {
    private String nome;
    private String tipo1;
    private String tipo2;
    private String peso;
    private String altura;
    private String cor;
    private String habitat;
    private int fase;


}