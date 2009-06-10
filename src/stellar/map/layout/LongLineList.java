package stellar.map.layout;

public enum LongLineList 
{
    NONE ("None"),
    NAME ("Name"),
    HEX_ID ("Hex ID"),
    UWP ("World Profile"), 
    TRADE_CODE ("Trade codes & Comments");

    LongLineList (String text)
    {
        name = text; 
    }
    private final String name;
    
    @Override public final String toString() {return name; } 
    
}
