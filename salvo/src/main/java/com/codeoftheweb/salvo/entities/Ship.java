package com.codeoftheweb.salvo.entities;

import com.codeoftheweb.salvo.ShipTypes;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private ShipTypes type;
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

    public Ship() { }

    public Ship(ShipTypes type, Set<String> locations, GamePlayer gamePlayer) {
        this.type = type;
        this.locations = locations;
        this.gamePlayer = gamePlayer;
        this.isDown = false;
    }

    public void setType(ShipTypes type) { this.type = type; }

    public void setLocations(Set<String> locations) { this.locations = locations; }

    public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }

    public Long getId() { return this.id; }

    public boolean isDown() { return this.isDown; }

    public void checkHits(Set<String> hits) {
        for (String hit:hits){
            if (this.locations.contains(hit) && !this.hits.contains(hit)) {
                this.hits.add(hit);
            }
        }

        this.isDown = (this.hits.size() == this.locations.size());
    }

    public Map<String, Object> getMappedData() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("type", this.type.toString());
        data.put("locations", this.locations);
        data.put("isDown", this.isDown);

        return data;
    }
}