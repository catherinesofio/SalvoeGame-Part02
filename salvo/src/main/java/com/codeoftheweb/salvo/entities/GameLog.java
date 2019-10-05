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
    private Date date;
    private String message;
    private Long gamePlayerId;

    @ElementCollection
    @Column(name = "params")
    private Set<String> params;

    public GameLog() {}

    public GameLog(Date date, String message, Long gamePlayerId) {
        this.date = date;
        this.message = message;
        this.gamePlayerId = gamePlayerId;
        this.params = null;
    }

    public GameLog(Date date, String message, Long gamePlayerId, Set<String> params) {
        this.date = date;
        this.message = message;
        this.gamePlayerId = gamePlayerId;
        this.params = params;
    }

    @JsonIgnore
    public Map<String, Object> getMappedData() {
        Map<String, Object> data = new HashMap<>();

        data.put("gamePlayerId", gamePlayerId);
        data.put("date", date.toInstant());
        data.put("message", message);
        data.put("params", params);

        return data;
    }
}