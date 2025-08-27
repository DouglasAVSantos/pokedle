package com.pokemon.dle.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.LinkedHashMap;
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

    // Este construtor é para o caso de vitória, onde todos os campos estão corretos.
    public PokemonResponse(PokemonRequest request1, PokemonRequest request2) {
        this.mensagem = Map.of("mensagem", "parabéns, você acertou");
        this.nome = Map.of("valor", request2.getNome(), "status", "correct");
        this.tipo1 = Map.of("valor", request2.getTipo1(), "status", "correct");
        this.tipo2 = Map.of("valor", request2.getTipo2(), "status", "correct");
        this.peso = Map.of("valor", request2.getPeso(), "status", "correct");
        this.altura = Map.of("valor", request2.getAltura(), "status", "correct");
        this.cor = Map.of("valor", request2.getCor(), "status", "correct");
        this.habitat = Map.of("valor", request2.getHabitat(), "status", "correct");
        this.fase = Map.of("valor", request2.getFase().toString(), "status", "correct");
    }
}