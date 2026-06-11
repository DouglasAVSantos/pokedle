package com.pokemon.dle.repository;

import com.pokemon.dle.model.ScoreEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScoreEntryRepository extends JpaRepository<ScoreEntry, Long> {
    Optional<ScoreEntry> findByIpAddressAndPlayDate(String ipAddress, LocalDate playDate);
    List<ScoreEntry> findAllByPlayDate(LocalDate playDate);
}
