package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entities.*;
import com.codeoftheweb.salvo.repositories.*;
import com.codeoftheweb.salvo.utils.GameLogs;
import com.codeoftheweb.salvo.utils.GameStates;
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
    private SalvoRepository salvoRepository;

    @Autowired
    private GameLogRepository gameLogRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isGuest(Authentication authentication) { return (authentication == null) ? true : false; }

    private Player getUser(Authentication authentication) { return (authentication == null) ? null : playerRepository.findByEmail(authentication.getName()); }

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

        data.put("user", isGuest(authentication) ? null : getUser(authentication).getMappedData());
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
            Date date = new Date();

            Game game = gameRepository.save(new Game(date));

            GamePlayer gp = gamePlayerRepository.save(new GamePlayer(date, getUser(authentication), game));

            GameLog log = new GameLog(date, Consts.LOG_TEMPLATES.get(GameLogs.PLAYER_CREATED_GAME), gp.getId());
            game.addGameLog(log);
            gameLogRepository.save(log);

            return new ResponseEntity<Long>(gp.getId(), HttpStatus.CREATED);
        }

        return new ResponseEntity<Long>(0L, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/game/{gm}/players", method = RequestMethod.POST)
    private ResponseEntity<Long> addPlayer(@PathVariable Long gm, Authentication authentication) {
        Game game = gameRepository.findById(gm).get();
        Player user = getUser(authentication);

        if (!isGuest(authentication) && !game.containsPlayer(user)) {
            Date date = new Date();

            GamePlayer gp = gamePlayerRepository.save(new GamePlayer(date, user, game));

            GameLog log = new GameLog(date, Consts.LOG_TEMPLATES.get(GameLogs.PLAYER_JOINED_GAME), gp.getId());
            game.addGameLog(log);
            gameLogRepository.save(log);

            return new ResponseEntity<Long>(gp.getId(), HttpStatus.CREATED);
        }

        return new ResponseEntity<Long>(0L, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/games/players/{gp}/ships", method = RequestMethod.POST)
    private ResponseEntity<String> addShips(@PathVariable Long gp, @RequestBody List<Ship> ships, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gp).get();
        Player user = getUser(authentication);

        if (gamePlayer == null) {
            return new ResponseEntity<String>("Gameplayer does not exist.", HttpStatus.UNAUTHORIZED);
        } else if (user != null && gamePlayer.getPlayerId() == user.getId()) {
            if (gamePlayer.getState() != PlayerStates.WAITING_PREPARING) {
                return new ResponseEntity<String>("Ships have already been placed.", HttpStatus.FORBIDDEN);
            }

            for (Ship ship:ships) {
                gamePlayer.addShip(ship);
                ship.setGamePlayer(gamePlayer);
                shipRepository.save(ship);
            }

            Game game = gamePlayer.getGame();

            GameLog log = new GameLog(new Date(), Consts.LOG_TEMPLATES.get(GameLogs.PLAYER_PLACED_SHIPS), gamePlayer.getId());
            game.addGameLog(log);
            gameLogRepository.save(log);

            gamePlayer.setState(PlayerStates.WAITING_PLAYER);
            gamePlayerRepository.save(gamePlayer);
            gameRepository.save(game);

            return new ResponseEntity<String>("Ships successfully placed.", HttpStatus.CREATED);
        }

        return new ResponseEntity<String>("No user found", HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/games/players/{gp}/salvoes", method = RequestMethod.POST)
    private ResponseEntity<String> addSalvoes(@PathVariable Long gp, @RequestBody List<Salvo> salvoes, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gp).get();
        Player user = getUser(authentication);

        if (gamePlayer == null) {
            return new ResponseEntity<String>("Gameplayer does not exist.", HttpStatus.UNAUTHORIZED);
        } else if (user != null && gamePlayer.getPlayerId() == user.getId()) {
            if (gamePlayer.getState() != PlayerStates.PLAYING_TURN) {
                return new ResponseEntity<String>("Cannot place salvoes on the other player's turn.", HttpStatus.FORBIDDEN);
            }

            Game game = gamePlayer.getGame();
            GamePlayer opponent = game.getOpponent(gamePlayer);
            Long turn = game.refreshTurn(gamePlayer);
            Date date = new Date();

            Map<String, Object> hitsData = game.checkHits(gamePlayer, salvoes.subList(0, Consts.SALVOES));

            Set<Salvo> updatedSalvoes = (Set<Salvo>) hitsData.get("salvoes");
            updatedSalvoes.stream().forEach(salvo -> {
                salvo.setTurn(turn);
                salvo.setGamePlayer(gamePlayer);
                gamePlayer.addSalvo(salvo);
                salvoRepository.save(salvo);
            });

            GameLog log = new GameLog(new Date(), Consts.LOG_TEMPLATES.get(GameLogs.PLAYER_FIRED_SALVOES), gamePlayer.getId(), updatedSalvoes.stream().map(x -> x.getCell()).collect(Collectors.toSet()));
            game.addGameLog(log);
            gameLogRepository.save(log);

            Set<String> fails = updatedSalvoes.stream().filter(x -> !x.getSuccess()).map(x -> x.getCell()).collect(Collectors.toSet());
            Set<String> successes = updatedSalvoes.stream().filter(x -> x.getSuccess()).map(x -> x.getCell()).collect(Collectors.toSet());

            if (fails.size() > 0) {
                log = new GameLog(date, Consts.LOG_TEMPLATES.get(GameLogs.SALVO_FAILED), gamePlayer.getId(), fails);
                game.addGameLog(log);
                gameLogRepository.save(log);
            }

            if (successes.size() > 0) {
                log = new GameLog(date, Consts.LOG_TEMPLATES.get(GameLogs.SALVO_SUCCEDED), gamePlayer.getId(), successes);
                game.addGameLog(log);
                gameLogRepository.save(log);
            }

            Set<Ship> sunkShips = (Set<Ship>) hitsData.get("ships");
            sunkShips.forEach((ship) -> {
                GameLog gameLog = new GameLog(date, Consts.LOG_TEMPLATES.get(GameLogs.SHIP_SANK), opponent.getId(), new HashSet<String>() {{ add(ship.getType().toString()); }});
                game.addGameLog(gameLog);
                gameLogRepository.save(gameLog);
            });

            Player player01 = gamePlayer.getPlayer();
            Player player02 = opponent.getPlayer();

            if (game.getState() == GameStates.FINISHED) {
                log = new GameLog(date, Consts.LOG_TEMPLATES.get(GameLogs.PLAYER_WON_MATCH), gamePlayer.getId());
                game.addGameLog(log);
                gameLogRepository.save(log);

                log = new GameLog(date, Consts.LOG_TEMPLATES.get(GameLogs.PLAYER_LOST_MATCH), opponent.getId());
                game.addGameLog(log);
                gameLogRepository.save(log);

                scoreRepository.save(new Score(1f, date, game, player01));
                scoreRepository.save(new Score(0f, date, game, player02));
            }

            playerRepository.save(player01);
            playerRepository.save(player02);
            gamePlayerRepository.save(opponent);
            gamePlayerRepository.save(gamePlayer);
            gameRepository.save(game);

            return new ResponseEntity<String>("Salvoes successfully fired.", HttpStatus.CREATED);
        }

        return new ResponseEntity<String>("No user found", HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping("/templates/ships")
    private Set<Map<String, Object>> getShips() {
        return Consts.SHIPS;
    }

    @RequestMapping("/templates/salvoes")
    private int getSalvoes() { return Consts.SALVOES; }

    @RequestMapping(value = "/game_view/{gp}", method = RequestMethod.GET)
    private ResponseEntity<Map<String, Object>> getGameView(@PathVariable Long gp, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gp).get();
        Player user = getUser(authentication);

        if (user == null || gamePlayer.getPlayerId() != user.getId()) {
            return new ResponseEntity<Map<String, Object>>(new HashMap<>(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<Map<String, Object>> (gamePlayer.getMappedData(), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/game_update/{gp}", method = RequestMethod.GET)
    private ResponseEntity<Map<String, Object>> getGameUpdate(@PathVariable Long gp, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gp).get();
        Player user = getUser(authentication);

        if (user == null || gamePlayer.getId() != user.getId()) {
            return new ResponseEntity<Map<String, Object>>(new HashMap<>(), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<Map<String, Object>>(gamePlayer.getUpdatedData(), HttpStatus.UNAUTHORIZED);
    }
}