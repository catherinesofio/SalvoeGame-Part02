package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.entities.GameLog;
import com.codeoftheweb.salvo.utils.GameLogs;
import com.codeoftheweb.salvo.utils.PlayerStates;
import com.codeoftheweb.salvo.utils.Ships;

import java.util.*;

public final class Consts {

    public static final int MAX_TURNS = 20;

    public static final int SALVOES = 5;

    public static final Set<Map<String, Object>> SHIPS = new HashSet<Map<String, Object>>() {{
        add(new HashMap<String, Object>() {{ put("type", Ships.AIRCRAFT_CARRIER.toString()); put("size", 5); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.BATTLESHIP.toString()); put("size", 4); }});
        /*add(new HashMap<String, Object>() {{ put("type", Ships.DESTROYER.toString()); put("size", 3); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.SUBMARINE.toString()); put("size", 3); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.PATROL_BOAT.toString()); put("size", 2); }});*/
    }};

    public static final Map<PlayerStates, Float> SCORES = new HashMap<PlayerStates, Float>() {{
        put(PlayerStates.FINISHED_LOST, 0f);
        put(PlayerStates.FINISHED_WON, 1f);
        put(PlayerStates.FINISHED_TIED, 0.5f);
    }};

    public static final Map<PlayerStates, GameLogs> RESULTS = new HashMap<PlayerStates, GameLogs>() {{
        put(PlayerStates.FINISHED_WON, GameLogs.PLAYER_WON_MATCH);
        put(PlayerStates.FINISHED_LOST, GameLogs.PLAYER_LOST_MATCH);
        put(PlayerStates.FINISHED_TIED, GameLogs.PLAYER_TIED_MATCH);
    }};

    public static final Map<GameLogs, String> LOG_TEMPLATES = new HashMap<GameLogs, String>() {{
        put(GameLogs.PLAYER_CREATED_GAME, "{gp} created this match!.");
        put(GameLogs.PLAYER_FIRED_SALVOES, "{gp} fired salvoes to these locations: {ss}.");
        put(GameLogs.PLAYER_JOINED_GAME, "{gp} has joined the match!");
        put(GameLogs.PLAYER_LOST_MATCH, "{gp} lost the match!");
        put(GameLogs.PLAYER_PLACED_SHIPS, "{gp} has placed their ships.");
        put(GameLogs.PLAYER_TIED_MATCH, "{gp} tied the match!");
        put(GameLogs.PLAYER_WON_MATCH, "{gp} won the match!");
        put(GameLogs.SALVO_FAILED, "{gp}'s shots in {ss} were unsuccessful.");
        put(GameLogs.SALVO_SUCCEDED, "{gp}'s shots in {ss} did hit a target!");
        put(GameLogs.SHIP_SANK, "{gp}'s {st} sank!");
    }};
}