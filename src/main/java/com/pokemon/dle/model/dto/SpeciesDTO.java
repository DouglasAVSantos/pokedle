package com.pokemon.dle.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties
@Getter
public class SpeciesDTO {
    private String name;
    private Color color;
    @JsonProperty(value = "evolution_chain")
    private EvolutionChain evolutionChain;
    private Generation generation;

    public String getGeneration() {
        return generation.name;
    }

    public String getEvolutionChain() {
        return evolutionChain.url;
    }

    @Getter
    static class Color {
       private String name;
    }

    @Getter
    static class EvolutionChain{
        public String url;
    }

    @Getter
    static class Generation{
        public String name;
    }
}
