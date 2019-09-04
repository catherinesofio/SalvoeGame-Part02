package com.codeoftheweb.salvo;

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

    public Player() { }

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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void addScore(Score score) {
        this.scores.add(score);
    }

    @JsonIgnore
    public Map<String, Object> getMappedData() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", this.id);
        data.put("email", this.email);
        data.put("scores", this.scores.stream().map(x -> x.getScore()));

        return data;
    }
}