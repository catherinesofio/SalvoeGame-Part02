package com.codeoftheweb.salvo.entities;

import com.codeoftheweb.salvo.utils.Ships;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Ships type;
    private boolean isDown;

    @ElementCollection
    @Column(name = "hits")
    private Set<String> hits = new HashSet<>();

    @ElementCollection
    @Column(name = "location")
    private Set<String> locations = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    public Ship() {}

    public Ship(Ships type, Set<String> locations, GamePlayer gamePlayer) {
        this.type = type;
        this.locations = locations;
        this.gamePlayer = gamePlayer;
        this.isDown = false;
    }

    public Long getId() { return this.id; }

    public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }

    public void setType(Ships type) { this.type = type; }

    public Ships getType() { return this.type; }

    public void setLocations(Set<String> locations) { this.locations = locations.stream().map(x -> x.toUpperCase()).collect(Collectors.toSet()); }

    public boolean isDown() { return this.isDown; }

    public boolean checkHit(String hit) {
        Boolean hasHitted = locations.contains(hit);

        if (hasHitted) {
            hits.add(hit);
            this.isDown = (this.hits.size() == this.locations.size());

            return true;
        }

        return false;
    }

    @JsonIgnore
    public Map<String, Object> getMappedData() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("type", this.type.toString());
        data.put("locations", this.locations);
        data.put("isDown", this.isDown);

        return data;
    }
}