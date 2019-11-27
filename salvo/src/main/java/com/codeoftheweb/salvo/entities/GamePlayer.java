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

    public void setState(PlayerStates state) { this.state = state; }

    public void addShip(Ship ship) { this.ships.add(ship); }

    public void addSalvo(Salvo salvo) { this.salvoes.add(salvo); }

    public Map<String, Object> checkHits(List<Salvo> opponentSalvoes, Long turn) {
        Set<Ship> activeShips = this.ships.stream().filter(x -> !x.isDown()).collect(Collectors.toSet());

        Map<String, Object> data = new HashMap<>();
        Set<Ship> sunkShips = new HashSet<>();

        for (Salvo salvo: opponentSalvoes) {
            for (Ship ship: activeShips) {
                Boolean hit = ship.checkHit(salvo.getCell(), turn);

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

    public Long getActiveShips() { return this.ships.stream().filter(x -> !x.isDown()).count(); }

    @JsonIgnore
    public Set<Map<String, Object>> getSalvoesData() {
        Set<Map<String, Object>> data = new HashSet<>();

        for (Salvo salvo : this.salvoes) {
            data.add(salvo.getMappedData());
        }

        return data;
    }

    @JsonIgnore
    public Map<String, Object> getShipsData(boolean asPlayer) {
        Map<String, Object> data = new HashMap<>();

        data.put("activeShips", getActiveShips());
        if (asPlayer) {
            data.put("all", this.ships.stream().map(x -> x.getMappedData()).collect(Collectors.toList()));
        } else {
            data.put("sunkShips", this.ships.stream().filter(x -> x.isDown()).map(x -> x.getMappedData()).collect(Collectors.toList()));
        }

        return data;
    }

    @JsonIgnore
    public Map<String, Object> getShipsPerTurn(Long turn) {
        Map<String, Object> data = new HashMap<>();

        data.put("activeShips", getActiveShips());
        data.put("sunkShips", this.ships.stream().filter(x -> x.getSunkInTurn() == turn).map(x -> x.getMappedData()).collect(Collectors.toList()));

        return data;
    }

    @JsonIgnore
    public List<Map<String, Object>> getSalvoesPerTurn(Long turn) { return this.salvoes.stream().filter(x -> x.getTurn() == turn).map(x -> x.getMappedData()).collect(Collectors.toList()); }

}