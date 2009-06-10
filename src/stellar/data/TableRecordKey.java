package stellar.data;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
/**
 *  The enum of Table Record Keys, along with their text. The keys are used as 
 *  part of the ID when the table records are stored in (or retrieved from) the 
 *  XML file. They are also used as the key for the References map. 
 * @author Thomas Jones-Low
 * @version $Revision: 1.2 $
 */
public enum TableRecordKey
{
    PORTS ("ports"),
    SIZE  ("size"),
    ATMOSPHERE ("atmosphere"),
    HYDROGRAPHICS ("hydrograph"),
    POPULATION ("population"),
    GOVERNMENT ("government"),
    LAW_LEVEL ("lawlevel"),
    TECHNOLOGY ("technology"),
    TRAVEL_ZONE ("zone"),
    BASES ("bases"),
    TRADE_CODES ("trade"),
    REMARKS_CODES ("remarks"),
    LIFE_CODES ("life"),
    RESOURCE_CODE ("resource"),
    EXPORT_CODE ("export"),
    STAR_SPECTRUM ("spectrum"),
    STAR_PECULIAR ("peculiar"),
    STAR_LUMINOSITY ("luminosity"),
    //ALLEGIANCES ("polities"),
    POLITIES ("polities"),
    COLOR ("color"),
    PLANET_TYPE ("planets"),
    LINKS ("links");

    private final String name;
    private static final Map<String,TableRecordKey> lookup 
          = new HashMap<String,TableRecordKey>();

     static {
          for(TableRecordKey s : EnumSet.allOf(TableRecordKey.class))
               lookup.put(s.toString(), s);
     }

    TableRecordKey (String text) { name = text; }
    
    @Override public final String toString () { return name; }
    
    public static TableRecordKey get(String name) { return lookup.get(name); }
}
