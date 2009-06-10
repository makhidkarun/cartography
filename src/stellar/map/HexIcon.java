package stellar.map;
import com.softstart.VectorImage.HexVectorImage;
import stellar.data.HexID;
import stellar.data.LinkLine;
import stellar.data.Links;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.text.StyleConstants;

public abstract class HexIcon extends MapIcon
{
    // size of each hex
    private double xsize;
    private double ysize;
    private double xstart = 0.0;
    private double ystart = 0.0; 
    private BasicStroke borderStroke = null;
    
    protected Point2D.Double hexCenterPoint = new Point2D.Double();
    private HexID hexInUse = new HexID();
    private ArrayList linkLineList;

    public HexIcon (boolean set) { super(set); } 


    public void setXSize (double size) { xsize = size; }
    public void setYSize (double size) { ysize = size; } 
    public void setXStart (double start) { xstart = start; } 
    public void setYStart (double start) { ystart = start; }
    public double getXSize () { return xsize; }
    public double getYSize () { return ysize; }
    /**
     * calculate the center poinr of a hex (in pixels) given the hex
     * (row/colums) location
     * @return the center pixel point. 
     */
    @Override protected Point2D center (int row, int column)
    {
        hexCenterPoint.x = /*(2 * xsize) + */(row * 3 * xsize);
        hexCenterPoint.y = (((column * 2) + (row%2 == 1 ? 2 : 1)) * ysize) - ysize;
        hexCenterPoint.x += (2 * xsize);
        hexCenterPoint.y += ysize;
        hexCenterPoint.x += xstart;
        hexCenterPoint.y += ystart;
        return hexCenterPoint;
    }

    /**
     * Draw a single hex with the six borders. Size of the hex is determined 
     * by the xsize/ysize properties, location is determined by the 
     * HexCenterPoint (set in center() )
     * @param hex the hexagon image to draw
     * @param g2  the graphics 
     */
    public void drawHex (Graphics2D g2, HexVectorImage hex)
    {
        double x,y;
        x = (hexCenterPoint.getX() - (2 * xsize));
        y = (hexCenterPoint.getY() - ysize);
    
        g2.translate (x, y);
        hex.drawImage(g2);
        //g2.draw(hexOutline);
        g2.translate (-x, -y);
    }

    /**
     * write a hex id (string) into a given hex. Hex id is a string
     * "0101" to identify each hex for the end user. 
     */
    public void fillHexID (HexID xy, Graphics2D g2)
    {
        Point2D.Double draw = new Point2D.Double();
        draw.x = hexCenterPoint.getX();
        draw.y = (hexCenterPoint.getY() - ysize);
        drawLayout.drawString(xy.toString(), StyleConstants.ALIGN_CENTER, draw, g2);
    }

    /**
     * Draw the HexID's (hex number) into the drawn hexes. 
     * @param g2
     */
    public void fillHexIDs (Graphics2D g2)
    {
        int x, y;
        if (getLayout().getOptions().getLocationID() == LocationIDType.NONE) return;

        for (y = 0; y < mapRows; y++)
        {
            for (x = 0; x < mapCols; x++)
            {
                center(x,y);
                if (getLayout().getOptions().getLocationID() == LocationIDType.UNOCCUPIED ||
                    getLayout().getOptions().getLocationID() == LocationIDType.ALL)
                {
                    hexInUse.x = x + offsetCols;
                    hexInUse.y = y + offsetRows;
                    
                    if (!isDrawn(hexInUse)) continue;
                    
                    hexInUse.convertIDtoHex();
                    if (getLayout().getOptions().getLocationID() == LocationIDType.UNOCCUPIED &&
                        getMapData() != null &&
                        getMapData().getSystem(hexInUse) != null
                        )
                    {
                        ; /* do nothing */
                    }
                    else
                    {
                        fillHexID(hexInUse, g2);
                    }
                }
            }
        }
    }

    /**
     * Draw the links (if any) for this map. 
     * @param g2
     */
    public void fillLinks (Graphics2D g2)
    {
        Iterator i;
        int spx, spy, epx, epy; 

        if (getGroupData() != null) 
        {
            if (getGroupData().getLinkCount() < 1) return;
            i = getGroupData().getLinks();
            linkLineList = new ArrayList (getGroupData().getLinkCount());
        }
        else 
        {
            if (getMapData().getLinkCount() < 1) return;
            i = getMapData().getLinks();
            linkLineList = new ArrayList (getMapData().getLinkCount());
        }
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        while (i.hasNext())
        {
            Links link = (Links)i.next();
            Iterator j = link.getHexes();
            HexID start = (HexID)j.next();
            while (j.hasNext())
            {
                HexID end = (HexID)j.next();
                if (!isDrawn(start) && !isDrawn(end)) continue;
                if (start.getHexGroup().equals(end.getHexGroup()))
                {
                    center (start.x - offsetCols, start.y - offsetRows); 
                    spx = (int)hexCenterPoint.getX(); spy = (int)hexCenterPoint.getY();
                    center (end.x - offsetCols, end.y - offsetRows);
                    epx = (int)hexCenterPoint.getX(); epy = (int)hexCenterPoint.getY();
                    
                    g2.setColor (link.getLinkColor() != null ? link.getLinkColor() : Color.pink);
                    //g2.drawLine(spx, spy, epx, epy);
                    Line2D.Float line = new Line2D.Float ((float)spx, (float)spy, (float)epx, (float)epy);
                    g2.draw(line);
                    LinkLine linkLine = new LinkLine (link, line);
                    linkLineList.add (linkLine);
                }
                start = end;
            }
        }
    }

    @Override protected void calcMapSize (int rows, int cols)
    {
        mapSize.width = (int)(((cols * 3) + 1) * xsize);
        mapSize.height = (int)(((rows * 2)+ 1) * ysize + 2);
        
        if (sizeChanged)
        {
            if (rows < 1) rows = 1;
            if (cols < 1) cols = 1;
            mapRows = rows;
            mapCols = cols;
        }

        sizeChanged = false;
        dataChanged = false;
        scaleChanged = false;
        changed = true;
    }

     /**
     * Return the HexID location of the hex at a given pixel location on the map.
     * This function relates drawn map locations with the map HexIDs. Usually
     * this pixel location is from the mouse pointer. 
     * @return HexID location
     * @param y Y Coordinate
     * @param x X Corrdiante
     */
    @Override public HexID getLocation (int x, int y)
    {
        HexID h = new HexID();
        h.x = (int)((x + (2 * xsize)) / ( 3 * xsize));
        h.y = (int)((y + (ysize * (h.x%2 == 1 ? 2 : 1))) / (2 * ysize));
        h.x += offsetCols-1;
        h.y += offsetRows-1;
        h.convertIDtoHex();
        return h; 
    }
    
    /**
     * Get the first link within 5 pixels of the link. 
     * @return The first link close to the x/y position, null if there are none
     * @param y
     * @param x
     */
    public Links getLink (int x, int y)
    {
        LinkLine link;

        if (linkLineList == null) return null;
        for (int i = 0; i < linkLineList.size(); i++)
        {
            link = (LinkLine)linkLineList.get(i);
            if (link.getBounds().contains(x, y))
            {
                if (link.ptLineDistSq(x, y) < 25.0)
                {
                    return link.getLink();
                }
            }
        }
        return null;
    }

    /**
     * Set the Stroke used for drawing the polity borders on the map. A Stroke
     * is a collection of parameter defining how a line is to be drawn on the 
     * graphic of the map.
     * @param stroke
     */
    public void setBorderStroke (BasicStroke stroke) { borderStroke = stroke; }
    
    /**
     * Draw the polity borders on the graphic for the map.
     * @param g2 Graphic to draw borders. 
     */
    protected void drawBorders(Graphics2D g2)
    {
        HexBorders borders = new HexBorders();
        borders.setMapOffset(offsetRows, offsetCols);
        // If there is no group data, use the base group for the border data
        if (getGroupData() == null) setGroup (getMapData().getGroup(0));
        // If there is no base group, skip drawing the borders. 
        if (getGroupData() == null) return; 
        //line up the borders with the map hexes
        borders.setMapScale(xsize, ysize);
        borders.setXStart(xstart);
        borders.setYStart(ystart);
        borders.setParent(this);
        
        if (borderStroke != null) borders.setBorderStroke(borderStroke);
        
        // Need some way of overriding the border color
        borders.setBorderColor(getLayout().getOptions().getBorderColor());
        
        // check if the group data has been set properly. 
        if (getGroupData() == null) return;
        // figure out where the borders go
        borders.calculateBorders(getGroupData());
        
        // draw the borders;
        borders.drawBorders(g2);
    }


    public void setHexInUse(HexID hexInUse) { this.hexInUse = hexInUse; }


    public HexID getHexInUse() { return hexInUse; }


    public BasicStroke getBorderStroke() { return borderStroke; }

}