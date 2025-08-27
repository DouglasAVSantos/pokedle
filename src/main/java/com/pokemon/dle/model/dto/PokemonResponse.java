package com.pokemon.dle.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class PokemonResponse {
    private Map<String,String> mensagem;
    private Map<String,String> nome;
    private Map<String,String> tipo1;
    private Map<String,String> tipo2;
    private Map<String,String> peso;
    private Map<String,String> altura;
    private Map<String,String> cor;
    private Map<String,String> habitat;
    private Map<String,String> fase;

    public PokemonResponse(PokemonRequest request1, PokemonRequest request2) {
        this.mensagem = Map.of("mensagem","parabéns, você acertou");
        this.nome = Map.of("nome[pokemon do dia]", request1.getNome(), "nome[player]", request2.getNome());
        this.tipo1 = Map.of("tipo1[pokemon do dia]", request1.getTipo1(), "tipo1[player]", request2.getTipo1());
        this.tipo2 = Map.of("tipo2[pokemon do dia]", request1.getTipo2(), "tipo2[player]", request2.getTipo2());
        this.peso = Map.of("peso[pokemon do dia]", request1.getPeso(), "peso[player]", request2.getPeso());
        this.altura = Map.of("altura[pokemon do dia]", request1.getAltura(), "altura[player]", request2.getAltura());
        this.cor = Map.of("cor[pokemon do dia]", request1.getCor(), "cor[player]", request2.getCor());
        this.habitat = Map.of("habitat[pokemon do dia]", request1.getHabitat(), "habitat[player]", request2.getHabitat());
        this.fase = Map.of("fase[pokemon do dia]", request1.getFase().toString(), "fase[player]", request2.getFase().toString());
    }
}