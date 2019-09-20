package com.codeoftheweb.salvo.utils;

import java.util.Map;
import java.util.function.Function;

public abstract class StateMachine {

    protected Map<Transitions, State> states;
    protected Transitions currState;
    protected Function changeEvent;

    public StateMachine(Map<Transitions, State> states, Transitions initState) {
        this.setStates(states, initState);
    }

    protected void setStates(Map<Transitions, State> states, Transitions initState) {
        this.states = states;
        this.currState = initState;
        this.changeEvent = states.get(initState).getChangeEvent();
    }
}