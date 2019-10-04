package com.codeoftheweb.salvo.entities;

import com.codeoftheweb.salvo.utils.PlayerStates;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Date createdDate;
    private PlayerStates state;

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
        this.state = PlayerStates.WAITING_PREPARING;
    }

    public boolean hasLost() {
        return ships.stream()
                .filter(x -> x.isDown() == true)
                .count() < ships.size();
    }

    public Long getId() { return this.id; }

    public Long getPlayerId() { return this.player.getId(); }

    public Game getGame() { return this.game; }

    public List<Ship> getDownedShips() {
        return ships.stream().filter(x -> x.isDown()).collect(Collectors.toList());
    }

    public Object getPlayerData() { return this.player.getMappedData(); }

    public PlayerStates getState() { return this.state; }

    public void setState(PlayerStates state) {
        this.state = state;
        this.game.refreshState();
    }

    public void addShip(Ship ship) { this.ships.add(ship); }

    public void addSalvo(Salvo salvo) { this.salvoes.add(salvo); }

    public void checkSalvoes(List<Salvo> salvoes) {
        Set<Ship> activeShips = this.ships.stream().filter(x -> !x.isDown()).collect(Collectors.toSet());

        for (Salvo salvo: salvoes) {
            for (Ship ship: activeShips) {
                salvo.setSuccessful(ship.checkHit(salvo.getCell()));
            }
        }
    }

    @JsonIgnore
    public List<Map<String, Object>> getSalvoesData() {
        List<Map<String, Object>> salvoes = new ArrayList<Map<String, Object>>();

        for (Salvo salvo : this.salvoes) {
            salvoes.add(salvo.getMappedData());
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
        return this.game.getMappedData(this.id);
    }
}