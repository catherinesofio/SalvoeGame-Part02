package com.codeoftheweb.salvo.utils;

import javax.swing.*;
import java.util.Map;

public class State {

    private Map<Transitions, State> transitions;

    private Action<Transitions> onEnter;
    private Action<Transitions> onExit;

    public State(Map<Transitions, State> nextState, Action<Transitions> onEnter, Action<Transitions> onExit) {
        this.transitions = nextState;
        this.onEnter = onEnter;
    }

    public void enter(Transitions input) {
        onEnter(input);
    }

    public void exit(inpu) {
        onEnter(input);
    }
}