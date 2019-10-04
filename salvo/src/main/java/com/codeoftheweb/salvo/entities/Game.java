package com.codeoftheweb.salvo.entities;

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
    private GameStates state;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Game() { }

    public Game(Date creationDate) {
        this.creationDate = creationDate;
        this.gamePlayers = new HashSet<>();
        this.scores = new HashSet<>();
        this.state = GameStates.WAITING;
    }

    public Long getId() { return this.id; }

    public Date getCreationDate() { return this.creationDate; }

    public void setState(GameStates state) { this.state = state; }

    public int getGamePlayers() { return this.gamePlayers.size(); }

    public void refreshState() {
        List<GamePlayer> gps = gamePlayers.stream().collect(Collectors.toList());

        if (this.state == GameStates.WAITING && gamePlayers.stream().filter(x -> x.getState() == PlayerStates.WAITING_PLAYER).count() == 2) {
            this.state = GameStates.PLAYING;

            GamePlayer gp1 = gps.get(0).getId() < gps.get(1).getId() ? gps.get(0) : gps.get(1);
            gp1.setState(PlayerStates.PLAYING_TURN);

            GamePlayer gp2 = gps.get(0).getId() > gps.get(1).getId() ? gps.get(0) : gps.get(1);
            gp2.setState(PlayerStates.PLAYING_WAITING);
        } else if (this.state == GameStates.PLAYING) {

        }
    }

    public void addScore(Score score) {
        this.scores.add(score);
    }

    public boolean containsPlayer(Player player) {
        return gamePlayers.stream()
                .map(x -> x.getPlayerId())
                .collect(Collectors.toSet())
                .contains(player.getId());
    }

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
        data.put("state", this.state.toString());

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
            }
            player.put("salvoes", gp.getSalvoesData());
            player.put("score", getPlayerScore(gp.getPlayerId()));
            player.put("state", gp.getState().toString());

            players.add(player);
        }

        data.put("gamePlayers", players);
        data.put("state", this.state.toString());

        return data;
    }
}