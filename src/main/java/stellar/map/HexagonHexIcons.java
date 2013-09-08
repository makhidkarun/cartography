package stellar.map;
import stellar.data.HexID;

public class HexagonHexIcons extends HexIcons 
{
    /**
     * create a default icon image, subsector sized (10 hexes tall by 8 hexes wide)
     */
    public HexagonHexIcons() { super(7,7); }
    
    /**
     * create a default icon image (10 x 8), but without setting the default set
     * of hex layout managers. 
     */
    public HexagonHexIcons (boolean set) { super (set); setSize(7,7);}
    
    /**
     * Create an image of hexagons arranged in a hexagon to display stars.
     * @param radius
     */
    public HexagonHexIcons (int radius)
    {
        super (true);
        setSize (radius*2+1, radius*2+1);
    }

    protected boolean isDrawn (HexID hex)
    {
        if (!super.isDrawn(hex)) return false;
        HexID drawCenter = new HexID ((mapRows+1)/2, (mapCols+1)/2);
        int drawDistance = (mapRows-1)/2;
        
        if (drawCenter.distance(hex) > drawDistance) return false;
        return true;
    }
}