package stellar.map;

public interface DisplayOptions 
{

    public static final int NONE_INDEX = 0;

/*
    public static final String [] longItemsList = 
    {"None", "Name", "Hex ID", "World Profile", "TradeCodes & Comments" };

    public static final int NAME_INDEX = 1;
    public static final int HEX_ID_INDEX = 2;
    public static final int UWP_INDEX = 3;
    public static final int TRADE_INDEX = 4;
*/
/*    
    public static final String [] shortItemsList =
    {"None", "System Marker", "System - Starport", "System - Size",
     "System - Atmosphere", "System - Water", "System - Population", 
     "System - Government", "System - Law", "System - Technology",
     "System - Primary",
     "Primary Trade Code", "Base Code", "Allegiance", "Starport Code", "Population Code",
     "Gas Giant Code", "Belt Code", "PBG Code", "Travel Zone Code",
      "Gas Giant Marker", "Bases Marker", "Belt Marker"};

    public static final int SYSTEM_MARK_INDEX = 1;
    public static final int SYSTEM_STARPORT_INDEX = 2;
    public static final int SYSTEM_SIZE_INDEX = 3;
    public static final int SYSTEM_ATMO_INDEX = 4;
    public static final int SYSTEM_WATER_INDEX = 5;
    public static final int SYSTEM_POPULATION_INDEX = 6;
    public static final int SYSTEM_GOV_INDEX=7; 
    public static final int SYSTEM_LAW_INDEX=8; 
    public static final int SYSTEM_TECH_INDEX=9;
    public static final int SYSTEM_PRIMARY_INDEX = 10;
    public static final int TRADE_CODE_INDEX = 11;
    public static final int BASE_CODE_INDEX = 12;
    public static final int POLITY_INDEX = 13;
    public static final int STARPORT_INDEX = 14;
    public static final int POP_CODE_INDEX =15;
    public static final int GG_CODE_INDEX = 16;
    public static final int BELTS_CODE_INDEX = 17;
    public static final int PBG_CODE_INDEX = 18;
    public static final int ZONE_CODE_INDEX = 19;
    public static final int GG_MARKER_INDEX = 20;
    public static final int BASE_MARKER_INDEX = 21;
    public static final int BELT_MARKER_INDEX = 22;
*/
    //public static final String [] hexIDList = 
    //{"None", "Unoccupied", "All" } ;

    public static final int ALL_INDEX = 2;
    public static final int UNOCCUPIED_INDEX = 1;

    public static final String [] hexFillList = 
    {"None", "System", "Starport", "Size", "Atmosphere", "Hydrographics", "Population", 
     "Government", "Law Level", "Technology", "Primary Star",
     "Primary Trade Code", "Base Code", "Allegiance", "Starport Code", "Population Code",
     "Gas Giant Code", "Belt Code", "Travel Zone Code"};
}