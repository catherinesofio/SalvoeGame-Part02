package com.codeoftheweb.salvo.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Long turn;

    @ElementCollection
    @Column(name = "cells")
    private Set<String> cells = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    public Salvo() { }

    public Salvo(Long turn, Set<String> cells, GamePlayer gamePlayer) {
        this.turn = turn;
        this.cells = cells;
        this.gamePlayer = gamePlayer;
    }

    public Long getId() { return this.id; }

    public void setCells(Set<String> cells) { this.cells = cells; }

    public Map<String, Object> getMappedData() {
        Map<String, Object> data = new HashMap<>();

        data.put("turn", this.turn);
        data.put("locations", this.cells);

        return data;
    }
}