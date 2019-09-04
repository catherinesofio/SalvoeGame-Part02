package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/players")
    public List<Map<String, Object>> getPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(x -> x.getMappedData())
                .collect(Collectors.toList());
    }

    @RequestMapping("/games")
    public List<Map<String, Object>> getGames() {
        return gameRepository.findAll()
                .stream()
                .map(x -> x.getMappedData())
                .collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{nn}")
    public Map<String, Object> getGameView(@PathVariable Long nn) {
        return gamePlayerRepository.findById(nn).get().getMappedData();
    }
}