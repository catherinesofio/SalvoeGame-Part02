package com.codeoftheweb.salvo.entities;

import com.codeoftheweb.salvo.Consts;
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
    private Long turn;
    private Date creationDate;
    private GameStates state;

    @OneToMany
    private List<GameLog> gameLogs;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Game() {}

    public Game(Date creationDate) {
        this.turn = 1L;
        this.creationDate = creationDate;
        this.state = GameStates.WAITING;
        this.gameLogs = new ArrayList<>();
        this.gamePlayers = new HashSet<>();
        this.scores = new HashSet<>();
    }

    public Long getId() { return this.id; }

    public GamePlayer getOpponent(GamePlayer gamePlayer) { return this.gamePlayers.stream().filter(x -> x != gamePlayer).findFirst().orElse(null); }

    public Set<GamePlayer> getGamePlayers() { return this.gamePlayers; }

    public boolean containsPlayer(Player player) { return gamePlayers.stream().map(x -> x.getPlayerId()).collect(Collectors.toSet()).contains(player.getId()); }

    public GameStates getState() { return this.state; }

    public GameStates getAndRefreshState() {
        if (this.state == GameStates.WAITING && gamePlayers.stream().filter(x -> x.getState() == PlayerStates.WAITING_PLAYER).count() == 2) {
            this.state = GameStates.PLAYING;

            for (GamePlayer gp : this.gamePlayers) {
                gp.setState(PlayerStates.PLAYING_TURN);
            }
        } else if (this.gamePlayers.stream().filter(x -> x.getState() == PlayerStates.PLAYING_WAITING).count() == 2) {
            Long totalLosers = this.gamePlayers.stream().filter(x -> x.hasLost()).count();

            if (this.turn + 1 > Consts.MAX_TURNS || totalLosers == 2) {
                this.state = GameStates.FINISHED;

                for (GamePlayer gp : this.gamePlayers) {
                    gp.setState(PlayerStates.FINISHED_TIED);
                }
            } else if (totalLosers == 1) {
                this.state = GameStates.FINISHED;

                for (GamePlayer gp : this.gamePlayers) {
                    gp.setState(gp.hasLost() ? PlayerStates.FINISHED_LOST : PlayerStates.FINISHED_WON);
                }
            } else {
                for (GamePlayer gp : this.gamePlayers) {
                    gp.setState(PlayerStates.PLAYING_TURN);
                }

                this.turn += 1;
            }
        }

        return this.state;
    }

    public Long getTurn() { return this.turn; }

    public Map<String, Object> checkHits(GamePlayer gamePlayer, List<Salvo> salvoes) { return getOpponent(gamePlayer).checkHits(salvoes, this.turn); }

    public void addGameLog(GameLog gameLog) { this.gameLogs.add(gameLog); }

    public void addScore(Score score) { this.scores.add(score); }

    @JsonIgnore
    public float getPlayerScore(Long id) {
        List<Score> score = this.scores.stream().filter(x -> x.getPlayerId() == id).collect(Collectors.toList());

        return (score != null && score.size() > 0) ? score.get(0).getScore() : -1f;
    }

    @JsonIgnore
    public Map<String, Object> getMappedData() {
        Map<String, Object> data = new HashMap<>();

        data.put("id", this.id);
        data.put("date", this.creationDate);
        data.put("state", this.state.toString());

        List<Map<String, Object>> players = new ArrayList<>();
        Map<String, Object> player;
        for (GamePlayer gp : this.gamePlayers) {
            player = new HashMap<String, Object>();

            player.put("id", gp.getId());
            player.put("state", gp.getState());
            player.put("player", gp.getPlayer().getMappedData());

            players.add(player);
        }
        data.put("gamePlayers", players);

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
            player.put("player", gp.getPlayer().getReducedMappedData());
            player.put("ships", gp.getShipsData(gp.getId() == id));
            player.put("salvoes", gp.getSalvoesData());
            player.put("score", getPlayerScore(gp.getPlayerId()));
            player.put("state", gp.getState().toString());

            players.add(player);
        }
        data.put("gamePlayers", players);

        data.put("turn", this.turn);
        data.put("state", this.state.toString());
        data.put("logs", this.gameLogs.stream().map(x -> x.getMappedData()).collect(Collectors.toList()));

        return data;
    }

    @JsonIgnore
    public Map<String, Object> getMappedDataByTurn(Long turn) {
        Map<String, Object> data =  new HashMap<>();

        data.put("id", this.id);
        data.put("created", this.creationDate);

        List<Map<String, Object>> players = new ArrayList<>();
        Map<String, Object> player;
        for (GamePlayer gp : this.gamePlayers) {
            player = new HashMap<>();

            player.put("id", gp.getId());
            player.put("player", gp.getPlayer().getReducedMappedData());
            player.put("ships", gp.getShipsPerTurn(turn));
            player.put("salvoes", gp.getSalvoesPerTurn(turn));
            player.put("score", getPlayerScore(gp.getPlayerId()));
            player.put("state", gp.getState().toString());

            players.add(player);
        }
        data.put("gamePlayers", players);

        data.put("turn", this.turn);
        data.put("state", this.state);
        data.put("logs", this.gameLogs.stream().filter(x -> x.getTurn() == turn).map(x -> x.getMappedData()).collect(Collectors.toList()));

        return data;
    }

    @JsonIgnore
    public Map<String, Object> getMenuMappedData() {
        Map<String, Object> data = new HashMap<>();

        data.put("id", this.id);
        data.put("state", this.state.toString());

        GamePlayer opponent = gamePlayers.stream().findFirst().get();
        Map<String, Object> player = new HashMap<String, Object>();
        player.put("id", opponent.getPlayerId());
        player.put("state", opponent.getState());

        Map<String, Object> gps = new HashMap<>();
        gps.put("opponent", player);
        data.put("gamePlayers", gps);

        return data;
    }

    @JsonIgnore
    public Map<String, Object> getMenuMappedData(Long userId) {
        Map<String, Object> data = new HashMap<>();

        data.put("id", this.id);
        data.put("state", this.state.toString());

        Map<String, Object> gps = new HashMap<>();
        Map<String, Object> player;
        for (GamePlayer gp : this.gamePlayers) {
            player = new HashMap<String, Object>();
            player.put("state", gp.getState());

            if (gp.getPlayerId() != userId) {
                player.put("id", gp.getPlayerId());

                gps.put("opponent", player);
            } else {
                player.put("id", gp.getId());

                gps.put("player", player);
            }
        }

        data.put("gamePlayers", gps);

        return data;
    }
}