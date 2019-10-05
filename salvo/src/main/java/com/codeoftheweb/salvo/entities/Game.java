package com.codeoftheweb.salvo.entities;

import com.codeoftheweb.salvo.Consts;
import com.codeoftheweb.salvo.utils.GameLogs;
import com.codeoftheweb.salvo.utils.GameStates;
import com.codeoftheweb.salvo.utils.PlayerStates;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Date creationDate;
    private Long turn;
    private GameStates state;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany
    private List<GameLog> gameLogs;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Game() {}

    public Game(Date creationDate) {
        this.creationDate = creationDate;
        this.turn = 0L;
        this.state = GameStates.WAITING;
        this.gamePlayers = new HashSet<>();
        this.gameLogs = new ArrayList<>();
        this.scores = new HashSet<>();
    }

    public Long getId() { return this.id; }

    public GamePlayer getOpponent(GamePlayer gamePlayer) {
        return this.gamePlayers.stream().filter(x -> x != gamePlayer).findFirst().orElse(null);
    }

    public boolean containsPlayer(Player player) {
        return gamePlayers.stream().map(x -> x.getPlayerId()).collect(Collectors.toSet()).contains(player.getId());
    }

    public GameStates getState() { return this.state; }

    public void refreshState() {
        if (this.state == GameStates.WAITING && gamePlayers.stream().filter(x -> x.getState() == PlayerStates.WAITING_PLAYER).count() == 2) {
            this.state = GameStates.PLAYING;

            List<GamePlayer> gps = gamePlayers.stream().collect(Collectors.toList());

            GamePlayer gp1 = gps.get(0).getId() < gps.get(1).getId() ? gps.get(0) : gps.get(1);
            gp1.setState(PlayerStates.PLAYING_TURN);

            GamePlayer gp2 = gps.get(0).getId() > gps.get(1).getId() ? gps.get(0) : gps.get(1);
            gp2.setState(PlayerStates.PLAYING_WAITING);
        }
    }

    public Long refreshTurn(GamePlayer gamePlayer) {
        GamePlayer opponent = getOpponent(gamePlayer);

        if (gamePlayer.getId() < opponent.getId()) {
            this.turn += 1;
        }

        return this.turn;
    }

    public Map<String, Object> checkHits(GamePlayer gamePlayer, List<Salvo> salvoes) {
        GamePlayer opponent = getOpponent(gamePlayer);
        Map<String, Object> data = opponent.checkHits(salvoes);

        if (opponent.hasLost()) {
            this.state = GameStates.FINISHED;

            gamePlayer.setState(PlayerStates.FINISHED_WON);
            opponent.setState(PlayerStates.FINISHED_LOST);
        } else {
            gamePlayer.setState(PlayerStates.PLAYING_WAITING);
            opponent.setState(PlayerStates.PLAYING_TURN);
        }

        return data;
    }

    public void addGameLog(GameLog gameLog) { this.gameLogs.add(gameLog); }

    public void addScore(Score score) { this.scores.add(score); }

    @JsonIgnore
    public float getPlayerScore(Long id) {
        List<Score> score = this.scores.stream().filter(x -> x.getPlayerId() == id).collect(Collectors.toList());

        return (score != null && score.size() > 0) ? score.get(0).getScore() : -1f;
    }

    @JsonIgnore
    public Map<String, Object> getMappedData() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", this.id);
        data.put("created", new Timestamp(this.creationDate.getTime()));

        List<Map<String, Object>> players = new ArrayList<Map<String, Object>>();

        Map<String, Object> player;

        for (GamePlayer gp : this.gamePlayers) {
            player = new HashMap<String, Object>();
            player.put("id", gp.getId());
            player.put("player", gp.getPlayerData());
            player.put("score", getPlayerScore(gp.getPlayerId()));
            player.put("state", gp.getState().toString());

            players.add(player);
        }

        data.put("gamePlayers", players);
        data.put("turn", this.turn);
        data.put("state", this.state.toString());
        data.put("log", this.gameLogs.stream().map(x -> x.getMappedData()).collect(Collectors.toList()));

        return data;
    }

    @JsonIgnore
    public Map<String, Object> getMappedData(Long id) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", this.id);
        data.put("created", new Timestamp(this.creationDate.getTime()));

        List<Map<String, Object>> players = new ArrayList<Map<String, Object>>();

        Map<String, Object> player;

        for (GamePlayer gp : this.gamePlayers) {
            player = new HashMap<String, Object>();
            player.put("id", gp.getId());
            player.put("player", gp.getPlayerData());
            if (gp.getId() == id) {
                player.put("ships", gp.getShipsData());
            } else {
                player.put("ships", gp.getDownedShips());
            }
            player.put("salvoes", gp.getSalvoesData());
            player.put("score", getPlayerScore(gp.getPlayerId()));
            player.put("state", gp.getState().toString());

            players.add(player);
        }

        data.put("gamePlayers", players);
        data.put("turn", this.turn);
        data.put("state", this.state.toString());
        data.put("log", this.gameLogs.stream().map(x -> x.getMappedData()).collect(Collectors.toList()));

        return data;
    }
}