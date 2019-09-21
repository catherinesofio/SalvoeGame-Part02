package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entities.Game;
import com.codeoftheweb.salvo.entities.GamePlayer;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.utils.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public final class Consts {

    public static Set<Map<String, Object>> SHIPS = new HashSet<Map<String, Object>>() {{
        add(new HashMap<String, Object>() {{ put("type", Ships.AIRCRAFT_CARRIER.toString()); put("size", 5); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.BATTLESHIP.toString()); put("size", 4); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.SUBMARINE.toString()); put("size", 3); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.DESTROYER.toString()); put("size", 3); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.PATROL_BOAT.toString()); put("size", 2); }});
    }};

    public static Map<GameStates, State> GAME_STATES = new HashMap<GameStates, State>() {{
        put(GameStates.WAITING, new State<GameStates, Game>(GameStates.WAITING,
                (id) -> {

                    return true;
                },
                (obj) -> {

                    return true;
                }));
        put(GameStates.PLAYING, new State<GameStates, Game>(GameStates.PLAYING,
                (id) -> {
                    return true;
                },
                (id) -> {
                    return true;
                }));
        put(GameStates.FINISHED, new State<GameStates, Game>(GameStates.FINISHED,
                (id) -> {
                    return true;
                },
                (id) -> {
                    return true;
                }));
    }};

    public static Map<PlayerStates, State> PLAYER_STATES = new HashMap<PlayerStates, State>() {{
        put(PlayerStates.PLACING_SHIPS, new State<PlayerStates, GamePlayer>(PlayerStates.PLACING_SHIPS,
                (id) -> {
                    return true;
                },
                (id) -> {

                    return true;
                }));
        put(PlayerStates.WAITING_FOR_PLAYER, new State<PlayerStates, GamePlayer>(PlayerStates.WAITING_FOR_PLAYER,
                (id) -> {
                    return true;
                },
                (id) -> {
                    return true;
                }));
        put(PlayerStates.PLAYING_TURN, new State<PlayerStates, GamePlayer>(PlayerStates.PLAYING_TURN,
                (id) -> {
                    return true;
                },
                (id) -> {
                    return true;
                }));
        put(PlayerStates.WAITING_TURN, new State<PlayerStates, GamePlayer>(PlayerStates.WAITING_TURN,
                (id) -> {
                    return true;
                },
                (id) -> {
                    return true;
                }));
    }};
}