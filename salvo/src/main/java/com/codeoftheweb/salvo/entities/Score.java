package com.codeoftheweb.salvo.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private float score;
    private Date finishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    public Score() {}

    public Score(float score, Date finishDate, Game game, Player player) {
        this.score = score;
        this.finishDate = finishDate;
        this.game = game;
        this.player = player;
    }

    public Long getId() { return this.id; }

    public float getScore() { return this.score; }

    public Long getPlayerId() { return this.player.getId(); }
}