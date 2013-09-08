package stellar.map;
import java.awt.geom.AffineTransform;
import com.softstart.VectorImage.HexVectorImage;
import stellar.data.HexID;
import java.awt.Graphics2D;

 
public class HexIcons extends HexIcon
{
    /* sizes are multiples of x=3, y=5 for the screen resolution to come out clean */
    //static final int XSTART[] = {3, 6, 12, 18, 24};
    //static final int YSTART[] = {5, 10, 20, 30, 40};
    
    static final int XSTART[] = { 6,  9, 12, 18, 24};
    static final int YSTART[] = {10, 15, 20, 30, 40}; 

    // Graphic objects used in the map drawing
    //private LineFillVectorImage hexFill; 
    protected HexVectorImage hexOutline = new HexVectorImage();

    /**
     * create a default icon image, subsector sized (10 hexes tall by 8 hexes wide)
     */
    public HexIcons() { this(10,8); }
    
    /**
     * create a default icon image (10 x 8), but without setting the default set
     * of hex layout managers. 
     */
    public HexIcons (boolean set) { super (set); setSize (10,8); }

    /**
     * Create a map of a specified number of rows and columns, to hold all the 
     * stars. 
     */
    public HexIcons(int rows, int cols)
    {
        super(true);
        setSize (rows, cols);
    }

    /**
     * Decide if the hex (or items in the hex) should be drawn 
     * on the current map.
     * @param hex
     * @return true if the hex is drawn on the map, false if not
     */
    @Override protected boolean isDrawn (HexID hex) 
    {
       // return (getGroupData().getSystem(hex) != null);
        if (hex.x < offsetCols || hex.y < offsetRows) return false;
        
        if ((hex.x >= offsetCols + mapCols) || (hex.y >= offsetRows + mapRows)) return false;
        
        return true; 
    } 
    /**
     * Fill the map image with the hexes. Number of hexes (rows/columns) is
     * set by constructor or setMapSize()
     */
    @Override protected void drawAll()
    {
        Graphics2D g2 = offscreenImage.createGraphics();

        g2.setColor (getLayout().getOptions().getBackgroundColor());
        g2.fillRect(0,0,mapSize.width, mapSize.height);

        if (!g2.getFont().equals(getLayout().getOptions().getDisplayFont()))
            g2.setFont(getLayout().getOptions().getDisplayFont());

        if (getMapData() != null && getLayout().getOptions().isShowBorders())
        {
            drawBorders (g2);
        }

        fillHexes(g2);
        // Add the hex numbers as needed. 
        fillHexIDs(g2);
        // Draw the link, if required and if any
        if (getMapData() != null && getLayout().getOptions().isShowJumpRoutes())
        {
            fillLinks (g2);
        }
        
        // Draw the systems per the layout design. 
        if (getMapData() != null)
        {
            drawSystemLayout (g2);
        }

        changed = false;   
    }


    /**
     * Calculate the size of the map (in pixels) given the number 
     * of hex rows and colums. This also alters the image maps used in the 
     * map creation to scale them properly. 
     * @param rows
     * @param cols
     */
    @Override protected void calcMapSize (int rows, int cols)
    {
        int scaleIndex = scale.ordinal();
        AffineTransform at;
        if (scaleChanged)
        {
            setXSize(XSTART[scaleIndex]);
            setYSize(YSTART[scaleIndex]);
            at = AffineTransform.getScaleInstance(XSTART[scaleIndex]/(float)XSTART[MapScale.SCALE_3.ordinal()], 
                                YSTART[scaleIndex]/(float)YSTART[MapScale.SCALE_3.ordinal()]);
            hexOutline.transform(at);
        }

        super.calcMapSize(rows, cols);
    }
    
    protected void fillHexes(Graphics2D g2)
    {
        int x, y; 
        HexID drawHex = new HexID();
        // Go through the map rows/map columns
        // Draw the hexes on the map
        for (y = 0; y < mapRows; y++)
        {
            for (x = 0; x < mapCols; x++)
            {
                drawHex.x = x+offsetCols;
                drawHex.y = y+offsetRows;
                if (!isDrawn (drawHex)) continue;
                center(x,y);
                drawHex(g2, hexOutline);
            }
        }
    }

}