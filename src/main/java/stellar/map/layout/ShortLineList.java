package stellar.map.layout;

public enum ShortLineList
{
    NONE ("None"),
    SYSTEM_MARKER ("System marker"),
    SYSTEM_STARPORT ("System - Starport"),
    SYSTEM_SIZE ("System - Size"),
    SYSTEM_ATMO ("System - Atmosphere"),
    SYSTEM_WATER ("System - Water"),
    SYSTEM_POPULATION ("System - Population"),
    SYSTEM_GOVERNMENT ("System - Government"),
    SYSTEM_LAW ("System - Law Level"),
    SYSTEM_TECH ("System - Technology"),
    SYSTEM_PRIMARY ("System - Primary"),
    TRADE_CODE ("Primary trade code"),
    BASE_CODE ("Base code"),
    POLITY ("Allegiance"),
    STARPORT ("Starport code"),
    POP_CODE ("Population code"),
    GG_CODE ("Gas giant code"),
    BELTS_CODE ("Belts code"),
    PBG_CODE ("PBG code"),
    ZONE_CODE ("Travel zone code"),
    GG_MARKER ("Gas giant marker"),
    BASE_MARKER ("Bases marker"),
    BELT_MARKER ("Belt marker");
    
    ShortLineList (String text)
    {
        name = text; 
    }
    private final String name;
    
    @Override public final String toString() {return name; } 

}
