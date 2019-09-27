package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entities.*;
import com.codeoftheweb.salvo.repositories.*;
import com.codeoftheweb.salvo.utils.PlayerStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
    private ShipRepository shipRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isGuest(Authentication authentication) {
        return (authentication == null) ? true : false;
    }

    private Player getUser(Authentication authentication) {
        return (authentication == null) ? null : playerRepository.findByEmail(authentication.getName());
    }

    @RequestMapping("/user")
    private Map<String, Object> getPlayer(Authentication authentication) {
        Player user = getUser(authentication);

        return (user == null) ? null : user.getMappedData();
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

    @RequestMapping("/players")
    private List<Map<String, Object>> getPlayers() {
        return playerRepository.findAll()
                .stream()
                .map(x -> x.getMappedData())
                .collect(Collectors.toList());
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

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    private ResponseEntity<Long> createGame(Authentication authentication) {
        if (!isGuest(authentication)) {
            Game game = gameRepository.save(new Game(new Date()));

            GamePlayer gp = gamePlayerRepository.save(new GamePlayer(new Date(), getUser(authentication), game));

            return new ResponseEntity<Long>(gp.getId(), HttpStatus.CREATED);
        }

        return new ResponseEntity<Long>(0L, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/game/{gm}/players", method = RequestMethod.POST)
    private ResponseEntity<Long> addPlayer(@PathVariable Long gm, Authentication authentication) {
        Game game = gameRepository.findById(gm).get();
        Player user = getUser(authentication);

        if (!isGuest(authentication) && !game.containsPlayer(user)) {
            GamePlayer gp = gamePlayerRepository.save(new GamePlayer(new Date(), user, game));

            return new ResponseEntity<Long>(gp.getId(), HttpStatus.CREATED);
        }

        return new ResponseEntity<Long>(0L, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/games/players/{gp}/ships", method = RequestMethod.GET)
    private ResponseEntity<List<Map<String, Object>>> getShips(@PathVariable Long gp, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gp).get();
        Player user = getUser(authentication);

        if(user != null && gamePlayer.getPlayerId() == user.getId()) {
            return new ResponseEntity<>(gamePlayer.getShipsData(), HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<List<Map<String, Object>>>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/games/players/{gp}/ships", method = RequestMethod.POST)
    private void addShips(@PathVariable Long gp, @RequestParam List<String> types, @RequestParam List<Set<String>> positions, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gp).get();
        Player user = getUser(authentication);

        if(user != null && gamePlayer.getPlayerId() == user.getId()) {
            for (int i = types.size() - 1; i >= 0; i--) {
                gamePlayer.addShip(shipRepository.save(new Ship(types.get(i), positions.get(i), gamePlayer)));
            }

            gamePlayer.setState(PlayerStates.WAITING_PLAYER);
        }
    }

    @RequestMapping("/templates/ships")
    private Set<Map<String, Object>> getShips() {
        return Consts.SHIPS;
    }

    @RequestMapping(value = "/game_view/{gp}", method = RequestMethod.GET)
    private ResponseEntity<Map<String, Object>> getGameView(@PathVariable Long gp, Authentication authentication) {
        GamePlayer gp = gamePlayerRepository.findById(gp).get();
        Player user = getUser(authentication);

        if (user != null || gp.getPlayerId() != user.getId()) {
            return new ResponseEntity<Map<String, Object>>(new HashMap<>(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<Map<String, Object>> (gp.getMappedData(), HttpStatus.ACCEPTED);
    }
}