package com.softstart.VectorImage;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.Color;
import java.awt.BasicStroke;
/**
 * HexVectorImage draw a hexagon as a vector image. 
 * 
 * @version $Id$
 * @author Thomas Jones-Low
 */
public class HexVectorImage extends VectorImage 
{
    /**
     * Creates a regular hexagon of standard size. Use an affinity transform 
     * to change the size of the shape. 
     */
    public HexVectorImage()
    {
        super();
        Point hexCenterPoint = new Point(); 
        GeneralPath outline; 
        int xsize = 12; //HexIcons.XSTART[1];
        int ysize = 20; //HexIcons.YSTART[1];

        hexCenterPoint.x = xsize * 2;
        hexCenterPoint.y = ysize;
        outline = new GeneralPath (GeneralPath.WIND_EVEN_ODD, 7);
        outline.moveTo(hexCenterPoint.x - xsize, hexCenterPoint.y - ysize);
        outline.lineTo(hexCenterPoint.x + xsize, hexCenterPoint.y - ysize);
        outline.lineTo(hexCenterPoint.x + 2 * xsize, hexCenterPoint.y);
        outline.lineTo(hexCenterPoint.x + xsize, hexCenterPoint.y + ysize);
        outline.lineTo(hexCenterPoint.x - xsize, hexCenterPoint.y + ysize);
        outline.lineTo(hexCenterPoint.x - 2 * xsize, hexCenterPoint.y);
        outline.closePath();
        
        setShape(outline);
        setColor(Color.gray);
        setStroke(new BasicStroke (1));
    }
    
    /**
     * Creates a partial regular hexagon of standard size. Use an affinity
     * tranform to change the size of the hexagon
     * @param north
     * @param northEast
     * @param southEast
     * @param south
     * @param southWest
     * @param northWest
     */
    public HexVectorImage (boolean north, boolean northEast, boolean southEast,
        boolean south, boolean southWest, boolean northWest)
    {
        super();
        Point hexCenterPoint = new Point();
        GeneralPath line;
        int xsize = 12;
        int ysize = 20;
        
        hexCenterPoint.x = xsize * 2;
        hexCenterPoint.y = ysize;
        line = new GeneralPath (GeneralPath.WIND_EVEN_ODD, 2);
        
        if (northEast)
        {
            line.moveTo (hexCenterPoint.x + xsize, hexCenterPoint.y - ysize);
            line.lineTo (hexCenterPoint.x + 2 * xsize, hexCenterPoint.y);
        }
        if (southEast)
        {
            line.moveTo (hexCenterPoint.x + 2 * xsize, hexCenterPoint.y);
            line.lineTo (hexCenterPoint.x + xsize, hexCenterPoint.y + ysize);
        }
        if (south)
        {
            line.moveTo (hexCenterPoint.x + xsize, hexCenterPoint.y + ysize);
            line.lineTo (hexCenterPoint.x - xsize, hexCenterPoint.y + ysize);
        }
        if (southWest)
        {
            line.moveTo (hexCenterPoint.x - xsize, hexCenterPoint.y + ysize);
            line.lineTo (hexCenterPoint.x - 2 * xsize, hexCenterPoint.y);
        }
        if (northWest)
        {
            line.moveTo (hexCenterPoint.x - 2 * xsize, hexCenterPoint.y);
            line.lineTo (hexCenterPoint.x - xsize, hexCenterPoint.y - ysize);
        }
        if (north)
        {
            line.moveTo (hexCenterPoint.x - xsize, hexCenterPoint.y - ysize);
            line.lineTo (hexCenterPoint.x + xsize, hexCenterPoint.y - ysize);
        }
        line.closePath();
        setShape (line);
        setColor (Color.gray);
        setStroke (new BasicStroke (3));
    }
    
    /**
     * Create one side of a regular hexagon of standard size. Use an affinity
     * tranforamation to change the size of the hexagon side. 
     * @param side Which side to draw, follows the clock: 1: North East, 
     *  2: South East, 3: South, 4: South West, 5: North West, 6: North.
     */
    public HexVectorImage (int side)
    {
        super();
        Point hexCenterPoint = new Point();
        GeneralPath line;
        int xsize = 12;
        int ysize = 20;
        
        hexCenterPoint.x = xsize * 2;
        hexCenterPoint.y = ysize;
        line = new GeneralPath (GeneralPath.WIND_EVEN_ODD, 2);

        switch (side)
        {
            case 1: /*NE */
                line.moveTo (hexCenterPoint.x + xsize, hexCenterPoint.y - ysize);
                line.lineTo (hexCenterPoint.x + 2 * xsize, hexCenterPoint.y);
                break;
            case 2: /* SE */
                line.moveTo (hexCenterPoint.x + 2 * xsize, hexCenterPoint.y);
                line.lineTo (hexCenterPoint.x + xsize, hexCenterPoint.y + ysize);
                break;
            case 3: /* S */
                line.moveTo (hexCenterPoint.x + xsize, hexCenterPoint.y + ysize);
                line.lineTo (hexCenterPoint.x - xsize, hexCenterPoint.y + ysize);
                break;
            case 4: /* SW */
                line.moveTo (hexCenterPoint.x - xsize, hexCenterPoint.y + ysize);
                line.lineTo (hexCenterPoint.x - 2 * xsize, hexCenterPoint.y);
                break;
            case 5: /* NW */
                line.moveTo (hexCenterPoint.x - 2 * xsize, hexCenterPoint.y);
                line.lineTo (hexCenterPoint.x - xsize, hexCenterPoint.y - ysize);
                break;
            case 6:
                line.moveTo (hexCenterPoint.x - xsize, hexCenterPoint.y - ysize);
                line.lineTo (hexCenterPoint.x + xsize, hexCenterPoint.y - ysize);
                break;                
            default: break;
        }
        line.closePath();
        setShape (line);
        setColor (Color.gray);
        setStroke (new BasicStroke (3));
    }
}
