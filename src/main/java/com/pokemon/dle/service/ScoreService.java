package com.pokemon.dle.service;

import com.pokemon.dle.model.ScoreEntry;
import com.pokemon.dle.repository.ScoreEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreEntryRepository repository;

    public Optional<ScoreEntry> findTodayScore(String ipAddress) {
        return repository.findByIpAddressAndPlayDate(ipAddress, LocalDate.now());
    }

    public List<ScoreEntry> findAllTodayScores() {
        return repository.findAllByPlayDate(LocalDate.now());
    }

    public List<ScoreEntry> findAllScores() {
        return repository.findAll();
    }

    public ScoreEntry saveScore(String ipAddress, String playerName, int attempts, boolean solved) {
        String finalName = (playerName == null || playerName.isBlank()) ? null : playerName.trim();

        ScoreEntry entry = repository.findByIpAddressAndPlayDate(ipAddress, LocalDate.now())
                .orElseGet(() -> new ScoreEntry(ipAddress, finalName, LocalDate.now(), attempts, solved));

        entry.setPlayerName(finalName);
        entry.setAttempts(attempts);
        entry.setSolved(solved);
        return repository.save(entry);
    }
}
