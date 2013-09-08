package stellar.map;


public enum LocationIDType
{
    NONE ("None"), 
    UNOCCUPIED ("Unoccupied"), 
    ALL ("All");

    LocationIDType (String text)
    {
        name = text; 
    }
    private final String name;
    
    @Override public final String toString() {return name; } 
}
