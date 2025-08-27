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
    private Habitat habitat;

    public String getGeneration() {
        return generation.name;
    }

    public String getEvolutionChain() {
        return evolutionChain.url;
    }

    public String getColor() {
        return color.name;
    }

    public String getHabitat() {
        return habitat.name;
    }

    @Getter
    static class Color {
       private String name;
    }

    @Getter
    static class Habitat {
        private String name;
    }

    @Getter
    public static class EvolutionChain{
        public String url;
    }

    @Getter
    static class Generation{
        public String name;
    }
}
