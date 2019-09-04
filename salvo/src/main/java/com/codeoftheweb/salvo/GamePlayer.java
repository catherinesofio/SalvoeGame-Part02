package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Date createdDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvoes;

    public GamePlayer() { }

    public GamePlayer(Date createdDate, Player player, Game game) {
        this.createdDate = createdDate;
        this.player = player;
        this.game = game;
        this.salvoes = new HashSet<>();
    }

    public Long getId() { return this.id; }

    public Long getPlayerId() { return this.player.getId(); }

    public Object getPlayerData() { return this.player.getMappedData(); }

    public void addSalvo(Salvo salvo) {
        this.salvoes.add(salvo);
    }

    @JsonIgnore
    public List<Map<String, Object>> getSalvoesData() {
        List<Map<String, Object>> salvoes = new ArrayList<>();

        Map<String, Object> salvo;
        for (Salvo s : this.salvoes) {
            salvo = new HashMap<>();
            salvo.put("turn", s.getTurn());
            salvo.put("locations", s.getCells());

            salvoes.add(salvo);
        }

        return salvoes;
    }

    @JsonIgnore
    public List<Map<String, Object>> getShipsData() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

        for (Ship ship : this.ships) {
            data.add(ship.getMappedData());
        }

        return data;
    }

    @JsonIgnore
    public Map<String, Object> getMappedData() {
        Map<String, Object> data = this.game.getMappedData();

        return data;
    }
}