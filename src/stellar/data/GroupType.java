package stellar.data;

/**
 * An enum for the types of group records
 * @version $Revision: 1.2 $
 * @author  $Author: tjoneslo $
 */
public enum GroupType
{
    SYSTEM ("system"),
    SUBSECTOR ("subsector"),
    QUADRANT ("quadrant"),
    SECTOR ("sector"), 
    DOMAIN ("domain"),
    GROUP ("group"),
    ALL   ("all");
    
    GroupType (String text)
    {
        name = text; 
    }
    private final String name;
    
    public final String getName() {return name; } 
          
}
