package com.pokemon.dle.utility;

import java.util.Map;

public class Translator {

    public static final Map<String, String> tipos = Map.ofEntries(
            Map.entry("grass", "Grama"),
            Map.entry("poison", "Veneno"),
            Map.entry("fire", "Fogo"),
            Map.entry("water", "Água"),
            Map.entry("electric", "Elétrico"),
            Map.entry("ground", "Terra"),
            Map.entry("rock", "Pedra"),
            Map.entry("ice", "Gelo"),
            Map.entry("bug", "Inseto"),
            Map.entry("dragon", "Dragão"),
            Map.entry("psychic", "Psíquico"),
            Map.entry("flying", "Voador"),
            Map.entry("fairy", "Fada"),
            Map.entry("fighting", "Lutador"),
            Map.entry("normal", "Normal"),
            Map.entry("ghost", "Fantasma"),
            Map.entry("steel", "Aço")
            // Adicione os outros conforme necessário
    );

    public static final Map<String, String> cores = Map.ofEntries(
            Map.entry("green", "Verde"),
            Map.entry("red", "Vermelho"),
            Map.entry("blue", "Azul"),
            Map.entry("yellow", "Amarelo"),
            Map.entry("brown", "Marrom"),
            Map.entry("gray", "Cinza"),
            Map.entry("pink", "Rosa"),
            Map.entry("purple", "Roxo"),
            Map.entry("white", "Branco"),
            Map.entry("black", "Preto")
    );

    public static final Map<String, String> habitats = Map.ofEntries(
            Map.entry("forest", "Floresta"),
            Map.entry("grassland", "Campo"),
            Map.entry("mountain", "Montanha"),
            Map.entry("cave", "Caverna"),
            Map.entry("sea", "Mar"),
            Map.entry("urban", "Urbano"),
            Map.entry("rare", "Raro"),
            Map.entry("rough-terrain", "Terreno Acidentado")
    );

    public static String traduzirTipo(String tipo) {
        return tipos.getOrDefault(tipo, tipo);
    }

    public static String traduzirCor(String cor) {
        return cores.getOrDefault(cor, cor);
    }

    public static String traduzirHabitat(String habitat) {
        return habitats.getOrDefault(habitat, habitat);
    }
}

