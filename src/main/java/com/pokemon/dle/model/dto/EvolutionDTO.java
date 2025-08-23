package com.pokemon.dle.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties
@Getter
public class EvolutionDTO {
    private Chain chain;

    @Getter
    public static class Chain {
        private Species species;
        @JsonProperty("evolves_to")
        private List<EvolvesTo> evolvesTo;
    }
    @Getter
    public static class EvolvesTo{
        private Species species;
        @JsonProperty("evolves_to")
        private List<EvolvesTo> evolvesTo;

    }
    @Getter
    public static class Species {
        private String name;
        private String url;
    }

}


