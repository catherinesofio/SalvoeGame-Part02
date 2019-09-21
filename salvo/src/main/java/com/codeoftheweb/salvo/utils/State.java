package com.codeoftheweb.salvo.utils;

import java.util.function.Function;

public class State<T, S> {

    private T id;
    private Function<S, Boolean> onEnter;
    private Function<S, Boolean> onExit;

    public State(T id, Function<S, Boolean> onEnter, Function<S, Boolean> onExit) {
        this.id = id;
        this.onEnter = onEnter;
        this.onExit = onExit;
    }

    public void enter(S obj) {
        this.onEnter.apply(obj);
    }

    public void exit(S obj) {
        this.onExit.apply(obj);
    }
}