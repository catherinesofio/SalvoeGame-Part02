package com.codeoftheweb.salvo;

import java.util.*;

public final class Consts {

    public static Set<Map<String, Object>> SHIPS = new HashSet<Map<String, Object>>() {{
        add(new HashMap<String, Object>() {{ put("type", Ships.AIRCRAFT_CARRIER.toString()); put("size", 5); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.BATTLESHIP.toString()); put("size", 4); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.DESTROYER.toString()); put("size", 3); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.SUBMARINE.toString()); put("size", 3); }});
        add(new HashMap<String, Object>() {{ put("type", Ships.PATROL_BOAT.toString()); put("size", 2); }});
    }};
}