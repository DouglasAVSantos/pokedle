package com.pokemon.dle.utility;

import com.pokemon.dle.client.PokemonApiClient;
import com.pokemon.dle.model.dto.PokemonDTO;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PokemonDailyTest {

    @Test
    void shouldKeepTheSamePokemonForTheSameDayAndChangeOnTheNextDay() {
        PokemonApiClient client = mock(PokemonApiClient.class);
        AtomicInteger nextId = new AtomicInteger(25);

        when(client.get(anyString(), eq(PokemonDTO.class))).thenAnswer(invocation -> {
            PokemonDTO pokemonDto = new PokemonDTO();
            ReflectionTestUtils.setField(pokemonDto, "id", nextId.getAndIncrement());
            return pokemonDto;
        });

        PokemonDaily daily = new PokemonDaily(client, () -> nextId.getAndIncrement(), LocalDate.of(2026, 6, 11));

        int firstId = daily.getId();
        daily.refreshIfNeeded(LocalDate.of(2026, 6, 11));
        int sameDayId = daily.getId();
        daily.refreshIfNeeded(LocalDate.of(2026, 6, 12));
        int nextDayId = daily.getId();

        assertEquals(firstId, sameDayId);
        assertNotEquals(firstId, nextDayId);
    }
}
