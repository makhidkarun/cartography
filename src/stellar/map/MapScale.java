/*
 * $Id: MapScale.java,v 1.1 2008/11/03 22:09:55 tjoneslo Exp $
 */
package stellar.map;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * MapScale is an enumeration of Scales (sizes) of the map. Currently there are
 * 5 scales (numbered 1 through 5). 
 * @version $Revision: 1.1 $
 * @author $Author: tjoneslo $
 */
public enum MapScale
{
    SCALE_1 ,
    SCALE_2 ,
    SCALE_3 ,
    SCALE_4 , 
    SCALE_5;
    
    private static final Map<Integer,MapScale> lookup 
          = new HashMap<Integer,MapScale>();

     static {
          for(MapScale s : EnumSet.allOf(MapScale.class))
               lookup.put(s.ordinal(), s);
     }
     /**
     * convert an integer to a MapScale
     * @param code integer code for scale (1 to 5)
     * @return Corresponding map scale. 
     */
    public static MapScale get(int code) { 
          return lookup.get(code); 
     }
     /**
     * Get the next larger map scale. 
     * @param current current map scale
     * @return the next larger map scale, or the current scale if at the top.
     */
    public static MapScale next (MapScale current)
    {
        MapScale[] list = MapScale.values();
        MapScale next;
        if (current.ordinal() + 1 < list.length)
            next = list[current.ordinal() + 1]; 
        else
            next = current;
        return next;
    }
    public static MapScale previous (MapScale current)
    {
        MapScale[] list = MapScale.values();
        MapScale prev;
        if (current.ordinal() > 0)
            prev = list[current.ordinal() - 1];
        else
            prev = current;
        return prev;
    }
}
