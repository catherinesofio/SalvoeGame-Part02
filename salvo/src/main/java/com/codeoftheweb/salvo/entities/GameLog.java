package com.codeoftheweb.salvo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class GameLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Long turn;
    private Date date;
    private String message;
    private Long gamePlayerId;

    @ElementCollection
    @Column(name = "params")
    private Set<String> params;

    public GameLog() {}

    public GameLog(Long turn, Date date, String message, Long gamePlayerId) {
        this.turn = turn;
        this.date = date;
        this.message = message;
        this.gamePlayerId = gamePlayerId;
        this.params = null;
    }

    public GameLog(Long turn, Date date, String message, Long gamePlayerId, Set<String> params) {
        this.turn = turn;
        this.date = date;
        this.message = message;
        this.gamePlayerId = gamePlayerId;
        this.params = params;
    }

    public Long getTurn() { return this.turn; }

    @JsonIgnore
    public Map<String, Object> getMappedData() {
        Map<String, Object> data = new HashMap<>();

        data.put("gamePlayerId", this.gamePlayerId);
        data.put("turn", this.turn);
        data.put("date", this.date.toInstant());
        data.put("message", this.message);
        data.put("params", this.params);

        return data;
    }
}