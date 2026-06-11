package com.pokemon.dle.controller;

import com.pokemon.dle.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ScoreBoardController {

    private final ScoreService scoreService;

    @GetMapping("/scoreboard")
    public String scoreboard(Model model) {
        model.addAttribute("scores", scoreService.findAllScores());
        return "scoreboard";
    }
}
