package com.codeoftheweb.salvo.utils;

import java.util.HashMap;
import java.util.Map;

public class StateMachine<T, S> {

    protected Long S;
    protected Map<T, State> states;
    protected State currState;

    public StateMachine(S id, Map<T, State> states, T initState) {
        this.configure(states, initState);
    }

    private void configure(Map<T, State> states, T initState) {
        this.states = new HashMap<>(states);
        this.currState = this.states.get(initState);

        currState.enter(S);
    }
}