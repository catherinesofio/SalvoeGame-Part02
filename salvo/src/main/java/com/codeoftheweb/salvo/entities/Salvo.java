package com.codeoftheweb.salvo.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Long turn;
    private Boolean successful;

    @Column(name = "cells")
    String cell;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    public Salvo() { }

    public Salvo(Long turn, String cell, GamePlayer gamePlayer) {
        this.turn = turn;
        this.cell = cell;
        this.gamePlayer = gamePlayer;
        this.successful = false;
    }

    public Long getId() { return this.id; }

    public void setTurn(Long turn) { this.turn = turn; }

    public String getCell() { return this.cell; }

    public void setCell(String cell) { this.cell = cell.toUpperCase(); }

    public void setSuccessful(Boolean successful) { this.successful = successful; }

    public Map<String, Object> getMappedData() {
        Map<String, Object> data = new HashMap<>();

        data.put("turn", this.turn);
        data.put("cell", this.cell);
        data.put("success", this.successful);

        return data;
    }
}