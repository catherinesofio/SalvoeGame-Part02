package com.codeoftheweb.salvo.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StateMachine<T extends Serializable> {

    protected Map<T, State> states;
    protected State currState;

    public String getCurrentState() { return this.currState.toString(); }

    public StateMachine(Map<T, State> states, T initState) {
        this.configure(states, initState);
    }

    private void configure(Map<T, State> states, T initState) {
        this.states = new HashMap<>(states);
        this.currState = this.states.get(initState);

        this.currState.enter();
    }

    public void changeState(T state) {
        this.currState = this.states.get(currState);
        this.currState.enter();
    }
}