package com.codeoftheweb.salvo;

import java.util.*;

public final class Consts {

    public static Set<Map<String, Object>> SHIPS = new HashSet<Map<String, Object>>() {{
        add(new HashMap<String, Object>() {{ put("type", ShipTypes.AIRCRAFT_CARRIER.toString()); put("size", 5); }});
        add(new HashMap<String, Object>() {{ put("type", ShipTypes.BATTLESHIP.toString()); put("size", 4); }});
        add(new HashMap<String, Object>() {{ put("type", ShipTypes.DESTROYER.toString()); put("size", 3); }});
        add(new HashMap<String, Object>() {{ put("type", ShipTypes.SUBMARINE.toString()); put("size", 3); }});
        add(new HashMap<String, Object>() {{ put("type", ShipTypes.PATROL_BOAT.toString()); put("size", 2); }});
    }};

    public static int SALVOES = 6;
}