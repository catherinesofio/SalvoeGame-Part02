package com.codeoftheweb.salvo.utils;

import java.util.function.Function;

public class State<T> {

    private T id;
    private Function<T, Boolean> onEnter;
    private Function<T, Boolean> onExit;

    public State(T id, Function<T, Boolean> onEnter, Function<T, Boolean> onExit) {
        this.id = id;
        this.onEnter = onEnter;
        this.onExit = onExit;
    }

    public void enter() {
        this.onEnter.apply(this.id);
    }

    public void exit() {
        this.onExit.apply(this.id);
    }
}