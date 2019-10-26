package com.codeoftheweb.salvo.entities;

import com.codeoftheweb.salvo.Consts;
import com.codeoftheweb.salvo.utils.PlayerStates;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Player() {}

    public Player(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gamePlayers = new HashSet<>();
        this.scores = new HashSet<>();
    }

    public Long getId() { return this.id; }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() { return this.password; }

    public void addScore(Score score) {
        this.scores.add(score);
    }

    @JsonIgnore
    public Map<String, Object> getMappedData() {
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("id", this.id);
        data.put("name", this.name);

        Map<String, Object> scores = new HashMap<>();
        scores.put("total", (this.scores.size() > 0) ? this.scores.stream().map(score -> score.getScore()).reduce((a,
                                                                                                                   b) -> a + b) : 0);
        scores.put("lost",
                this.scores.stream().filter(score -> score.getScore() == Consts.SCORES.get(PlayerStates.FINISHED_LOST)).count());
        scores.put("won",
                this.scores.stream().filter(score -> score.getScore() == Consts.SCORES.get(PlayerStates.FINISHED_WON)).count());
        scores.put("tied",
                this.scores.stream().filter(score -> score.getScore() == Consts.SCORES.get(PlayerStates.FINISHED_TIED)).count());

        data.put("scores", scores);

        return data;
    }

    @JsonIgnore
    public Map<String, Object> getReducedMappedData() {
        Map<String, Object> data = new HashMap<>();

        data.put("id", this.id);
        data.put("name", this.name);

        return data;
    }
}