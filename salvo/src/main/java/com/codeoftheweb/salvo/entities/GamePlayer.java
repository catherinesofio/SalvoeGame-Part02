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

    public GamePlayer() {}

    public GamePlayer(Date createdDate, Player player, Game game) {
        this.createdDate = createdDate;
        this.player = player;
        this.game = game;
        this.salvoes = new HashSet<>();
        this.state = PlayerStates.WAITING_PREPARING;
    }

    public boolean hasLost() { return ships.stream() .filter(x -> x.isDown() == true).count() == ships.size(); }

    public Long getId() { return this.id; }

    public Long getPlayerId() { return this.player.getId(); }

    public Player getPlayer() { return this.player; }

    public Game getGame() { return this.game; }

    public PlayerStates getState() { return this.state; }

    public void setState(PlayerStates state) {
        this.state = state;
        this.game.refreshState();
    }

    public void addShip(Ship ship) { this.ships.add(ship); }

    public void addSalvo(Salvo salvo) { this.salvoes.add(salvo); }

    public Map<String, Object> checkHits(List<Salvo> opponentSalvoes) {
        Set<Ship> activeShips = this.ships.stream().filter(x -> !x.isDown()).collect(Collectors.toSet());

        Map<String, Object> data = new HashMap<>();
        Set<Ship> sunkShips = new HashSet<>();

        for (Salvo salvo: opponentSalvoes) {
            for (Ship ship: activeShips) {
                Boolean hit = ship.checkHit(salvo.getCell());

                if (hit) {
                    salvo.setSuccess(true);

                    if (ship.isDown()) {
                        sunkShips.add(ship);
                    }

                    break;
                }
            }
        }

        data.put("ships", sunkShips);
        data.put("salvoes", opponentSalvoes.stream().collect(Collectors.toSet()));

        return data;
    }

    @JsonIgnore
    public Set<Ship> getDownedShips() { return ships.stream().filter(x -> x.isDown()).collect(Collectors.toSet()); }

    @JsonIgnore
    public Object getPlayerData() { return this.player.getMappedData(); }

    @JsonIgnore
    public Set<Map<String, Object>> getSalvoesData() {
        Set<Map<String, Object>> data = new HashSet<>();

        for (Salvo salvo : this.salvoes) {
            data.add(salvo.getMappedData());
        }

        return data;
    }

    @JsonIgnore
    public Set<Map<String, Object>> getShipsData() {
        Set<Map<String, Object>> data = new HashSet<>();

        for (Ship ship : this.ships) {
            data.add(ship.getMappedData());
        }

        return data;
    }

    @JsonIgnore
    public Map<String, Object> getMappedData() { return this.game.getMappedData(this.id); }
}