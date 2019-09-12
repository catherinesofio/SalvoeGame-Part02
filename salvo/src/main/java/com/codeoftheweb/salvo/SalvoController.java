package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isGuest(Authentication authentication) {
        return (authentication == null) ? true : false;
    }

    private Player getUser(Authentication authentication) {
        return (authentication == null) ? null : playerRepository.findByEmail(authentication.getName());
    }

    @RequestMapping("/player")
    private Map<String, Object> getPlayer(Authentication authentication) {
        Player user = getUser(authentication);

        return (user == null) ? null : user.getMappedData();
    }

    @RequestMapping("/players")
    private List<Map<String, Object>> getPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(x -> x.getMappedData())
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/game/{nn}/players", method = RequestMethod.POST)
    private ResponseEntity<Long> addPlayer(@PathVariable Long nn, Authentication authentication) {
        Game game = gameRepository.findById(nn).get();
        Player user = getUser(authentication);

        if (!isGuest(authentication) && !game.containsPlayer(user)) {
            GamePlayer gp = gamePlayerRepository.save(new GamePlayer(new Date(), user, game));

            return new ResponseEntity<Long>(gp.getId(), HttpStatus.CREATED);
        }

        return new ResponseEntity<Long>(0L, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    private ResponseEntity<Long> createGame(Authentication authentication) {
        if (!isGuest(authentication)) {
            Game game = gameRepository.save(new Game(new Date()));
            GamePlayer gp = gamePlayerRepository.save(new GamePlayer(new Date(), getUser(authentication), game));

            return new ResponseEntity<Long>(gp.getId(), HttpStatus.CREATED);
        }

        return new ResponseEntity<Long>(0L, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/games", method = RequestMethod.GET)
    private Map<String, Object> getPlayerAndGames(Authentication authentication) {
        Map<String, Object> data = new HashMap<>();

        data.put("player", isGuest(authentication) ? null : getUser(authentication).getMappedData());
        data.put("games", this.getGames());

        return data;
    }

    private List<Map<String, Object>> getGames() {
        return gameRepository.findAll()
                .stream()
                .map(x -> x.getMappedData())
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/game_view/{nn}", method = RequestMethod.GET)
    private ResponseEntity<Map<String, Object>> getGameView(@PathVariable Long nn, Authentication authentication) {
        GamePlayer gp = gamePlayerRepository.findById(nn).get();

        if (isGuest(authentication) || gp.getPlayerId() != getUser(authentication).getId()) {
            return new ResponseEntity<Map<String, Object>>(new HashMap<>(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<Map<String, Object>> (gp.getMappedData(), HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    private ResponseEntity<Object> register(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Must fill all the fields.", HttpStatus.FORBIDDEN);
        }

        name = name.trim();
        email = email.trim();

        if (!email.contains("@") || email.contains(" ")) {
            return new ResponseEntity<>("Invalid e-mail.", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("E-mail already in use.", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(name, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}