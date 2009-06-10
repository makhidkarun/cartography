package stellar.map.layout;

/**
 * The collection of HexOptions properties. This enum is used as a collection of
 * strings, which in turn are used by the save/load process from the Properties
 * and for the PropertyChange as the strings for property changed. 
 * 
 * @see HexOptions
 * @author Thomas Jones-Low
 * @version $Revision: 1.1 $
 */
public enum HexOptionsProperties
{
    BACKGROUND_COLOR ("backgroundColor"), 
    BORDER_COLOR ("borderColor"),
    SHOW_BORDERS ("showBorders"),
    SHOW_JUMP_ROUTES ("showJumpRoutes"),
    SHOW_TRAVEL_ZONES ("showTravelZones"), 
    LOCATION_ID ("locationID"),
    FONT_NAME ("fontName"),
    FONT_SIZE ("fontSize"),
    FONT_STYLE ("fontStyle");
    
    HexOptionsProperties(String name)
    {
        this.name = name;
    }
    private final String name;
    
    @Override public final String toString() {return name; }            
}
