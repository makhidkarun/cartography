package stellar.map;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Iterator;
import stellar.data.HexID;
import stellar.data.GroupRecord;
import stellar.data.GroupType;

import javax.swing.text.StyleConstants;

public class SquareIcons extends MapIcon
{
    static final int XSIZE[] = {10, 20, 40, 60, 80}; 
    private Point center = new Point();

    public SquareIcons (boolean set) 
    { 
        super (set); calcMapSize(4,4);
        
    }  
    
    
    protected void drawAll()
    {
        int x, y;
        Graphics2D g2 = offscreenImage.createGraphics();
        g2.setColor (getLayout().getOptions().getBackgroundColor());
        g2.fillRect(0,0,mapSize.width, mapSize.height);

        if (!g2.getFont().equals(getLayout().getOptions().getDisplayFont()))
            g2.setFont(getLayout().getOptions().getDisplayFont());
        
        g2.setColor (Color.gray);
        for (y = 0; y < mapRows; y++)
        {
            for (x = 0; x < mapCols; x++)
            {
                center (x,y);
                drawSquare(center, g2);                
            }
        }
        
        if (getMapData() != null)
        {
            if (level == GroupType.SYSTEM)
            {
                drawSystemLayout(g2);
            }
            else
            {
                drawGroupLayout (g2);
            }
        }
        changed = false;
    }
    
    protected Point2D center (int row, int column)
    {
        int scaleIndex = scale.ordinal();
        center.x = (row ) * (XSIZE[scaleIndex] + XSIZE[scaleIndex]/10);
        center.y = (column ) * (XSIZE[scaleIndex] + XSIZE[scaleIndex]/10);
        center.x += XSIZE[scaleIndex]/2;
        center.y += XSIZE[scaleIndex]/2; 
        return center;
    }

    private void drawSquare (Point center, Graphics2D g2)
    {
        int sx, sy, ex, ey; 
        int scaleIndex = scale.ordinal();
        center.x -= XSIZE[scaleIndex]/2;
        center.y -= XSIZE[scaleIndex]/2; 

        sx = center.x;
        sy = center.y;
        ex = sx + XSIZE[scaleIndex];
        ey = sy; 
        g2.drawLine(sx, sy, ex, ey);
        sx = ex;
        sy = ey;
        ex = sx; 
        ey = center.y + XSIZE[scaleIndex];
        g2.drawLine(sx, sy, ex, ey);
        sx = ex;
        sy = ey;
        ex = center.x;
        ey = center.y + XSIZE[scaleIndex];
        g2.drawLine(sx, sy, ex, ey);
        sx = ex; 
        sy = ey;
        ex = center.x;
        ey = center.y;
        g2.drawLine(sx, sy, ex, ey);
    }
    
    protected void calcMapSize (int rows, int cols)
    {
        int scaleIndex = scale.ordinal();
        if (rows < 1) rows = 1;
        if (cols < 1) cols = 1;

        changed = true;
        
        mapRows = rows;
        mapCols = cols;
        
        mapSize.width = (XSIZE[scaleIndex] + XSIZE[scaleIndex]/10) * cols;
        mapSize.height = (XSIZE[scaleIndex] + XSIZE[scaleIndex]/10) * rows;
    }

    public HexID getLocation (int x, int y)
    {
        int scaleIndex = scale.ordinal();
        HexID h = new HexID();
        h.x = 1 + (x / (XSIZE[scaleIndex] + XSIZE[scaleIndex]/10));
        h.y = 1 + (y / (XSIZE[scaleIndex] + XSIZE[scaleIndex]/10));
        return h;
    }
    
    public boolean isDrawn (HexID location) { return true; } 
    /**
     * Create a default image, a sector size (4x4 subsectors)
     */
    public SquareIcons() { this(4,4); }

    /**
     * Create a map of a specified number of rows and columns, to hold all the 
     * stars. 
     */
    public SquareIcons(int rows, int cols)
    {
        this (false);
        calcMapSize (rows, cols);
    }

    private void drawGroupLayout (Graphics2D g2)
    {
        GroupRecord gr;
        Iterator i;
        String type;
        Point p;
        for (i = getMapData().getGroups(); i.hasNext();)
        {
            gr = (GroupRecord)i.next();
            if (gr.getType() == level) 
            {
                p = (Point)center (gr.getLocation());
                drawLayout.drawString(gr.getName(), StyleConstants.ALIGN_CENTER, center, g2);
            }
        }

    }
}