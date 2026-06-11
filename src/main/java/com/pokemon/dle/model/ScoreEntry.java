package com.pokemon.dle.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ScoreEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ipAddress;
    private String playerName;

    @Column(name = "play_date")
    private LocalDate playDate;

    private Integer attempts;
    private boolean solved;
    private LocalDate createdAt;

    public ScoreEntry(String ipAddress, String playerName, LocalDate playDate, Integer attempts, boolean solved) {
        this.ipAddress = ipAddress;
        this.playerName = playerName;
        this.playDate = playDate;
        this.attempts = attempts;
        this.solved = solved;
        this.createdAt = LocalDate.now();
    }
}
