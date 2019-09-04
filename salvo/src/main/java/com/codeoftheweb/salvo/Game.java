package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Array;
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

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Game() { }

    public Game(Date creationDate) {
        this.creationDate = creationDate;
        this.gamePlayers = new HashSet<>();
        this.scores = new HashSet<>();
    }

    public Long getId() { return this.id; }

    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public Date getCreationDate() { return this.creationDate; }

    public void addScore(Score score) {
        this.scores.add(score);
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
            player.put("ships", gp.getShipsData());
            player.put("salvoes", gp.getSalvoesData());
            player.put("score", getPlayerScore(gp.getPlayerId()));

            players.add(player);
        }

        data.put("gamePlayers", players);

        return data;
    }
}