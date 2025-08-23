package com.pokemon.dle.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonResponse{
    private String name;
    private List<TypeSlot> types;
    @Getter
    @JsonIgnoreProperties
    public static class TypeSlot{
        private Type type;
    }
    @Getter
    @JsonIgnoreProperties
    public static class Type{
        private String name;
    }


}