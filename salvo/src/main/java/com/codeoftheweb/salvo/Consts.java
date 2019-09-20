package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.utils.State;
import com.codeoftheweb.salvo.utils.States;
import com.codeoftheweb.salvo.utils.Transitions;

import java.util.*;

public final class Consts {
    public static Set<Map<String, Object>> SHIPS = new HashSet<Map<String, Object>>() {{
        add(new HashMap<String, Object>() {{ put("type", Ships.AIRCRAFT_CARRIER.toString()); put("size", 5); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.BATTLESHIP.toString()); put("size", 4); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.SUBMARINE.toString()); put("size", 3); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.DESTROYER.toString()); put("size", 3); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.PATROL_BOAT.toString()); put("size", 2); }});
    }};

    public static Map<States, Map<Transitions, State>> GAME_STATES = new HashMap<States, Map<Transitions, State>>() {{
        put(States.WAITING, new HashMap<Transitions, State>() {{
            put(Transitions.WAITING_SHIPS, new State(Transitions.WAITING_SHIPS, null));
            put(Transitions.WAITING_PLAYER, new State(Transitions.WAITING_PLAYER, null));
            put(Transitions.WAITING_START, new State(Transitions.WAITING_START, null));
        }});
        put(States.STARTED, new HashMap<Transitions, State>() {{
            put(Transitions.PLAYING_TURN_1, new State(Transitions.PLAYING_TURN_1, null));
            put(Transitions.PLAYING_TURN_2, new State(Transitions.PLAYING_TURN_2, null));
            put(Transitions.PLAYING_FINISH, new State(Transitions.PLAYING_FINISH, null));
        }});
        put(States.FINISHED, new HashMap<Transitions, State>() {{
            put(Transitions.FINISHED_RESULTS, new State(Transitions.FINISHED_RESULTS, null));
        }});
    }};
}
