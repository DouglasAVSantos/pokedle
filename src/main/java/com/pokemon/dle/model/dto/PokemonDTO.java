package com.pokemon.dle.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonDTO {
    private Integer id;
    private String name;
    @JsonProperty("weight")
    private Double peso;
    @JsonProperty("height")
    private Double altura;
    private List<TypeSlot> types;

    public Double getPeso() {
        return peso / 10.0 ;
    }

    public Double getAltura() {
        return altura / 10.0 ;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TypeSlot {
        private Type type;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Type {
        private String name;
    }


}