package com.codeoftheweb.salvo.entities;

import com.codeoftheweb.salvo.Consts;
import com.codeoftheweb.salvo.utils.State;
import com.codeoftheweb.salvo.utils.StateMachine;
import com.codeoftheweb.salvo.utils.States;

import com.codeoftheweb.salvo.utils.Transitions;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
public class GameStateMachine extends StateMachine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @OneToOne(mappedBy = "gameState", fetch = FetchType.EAGER)
    private Game game;

    public GameStateMachine() {
        super(Consts.GAME_STATES.get(States.WAITING), Transitions.WAITING_SHIPS);
    }

    public void changeState(State state, Transitions initState) {
        this.setStates(Consts.GAME_STATES.get(state), initState);
    }
}