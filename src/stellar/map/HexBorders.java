package stellar.map;
import com.softstart.VectorImage.HexVectorImage;
import stellar.data.GroupRecord;
import stellar.data.HexID;
import stellar.data.StarSystem;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

public class HexBorders extends HexIcon
{
    private int borders[][];
    private int al[][];
    private int mapRows;
    private int mapCols;
    private int offsetRows = 1;
    private int offsetCols = 1;
    private Color borderColor; 
    private HexVectorImage hexBorder[] = new HexVectorImage [6];
    private HexIcon parentIcon;

    public HexBorders()
    {
        super(false);
        for (int i = 0; i < 6; i++) hexBorder[i] = new HexVectorImage(i+1);    
    }

    public void setParent (HexIcon parent) { parentIcon = parent; }
    public void setMapOffset (int rows, int cols) { offsetRows = rows; offsetCols = cols; }
    
    public void calculateBorders(GroupRecord g)
    {
        StarSystem s;
        int x, y, aleg;
        if (g == null) return;
        
        mapRows = g.getExtentY();
        mapCols = g.getExtentX();
        
        borders = new int[mapCols+2][mapRows+2];
        al = new int [mapCols+2][mapRows+2];

        Iterator i = g.getSystems();
        while (i.hasNext())
        {
            s = (StarSystem)i.next();
            x = s.getLocation().x - offsetCols + 1; 
            y = s.getLocation().y - offsetRows + 1;
            if ( !parentIcon.isDrawn(s.getLocation())) continue;
            if (s.getPolity().length() < 2)
            {
                aleg = s.getPolity().charAt(0) * 256 + ' ';
            }
            else
            {
                aleg = s.getPolity().charAt(0) * 256 + s.getPolity().charAt(1);
            }
            al[x][y] = aleg;
        }
        setAlegiances(); // pass 1
        setAlegiances(); // pass 2
        for (y = 1; y <= mapRows; y++)
        {
            for (x = 1; x <= mapCols; x++)
            {
                aleg = al[x][y];
                if (al[x-1][y-(x%2)] != aleg) borders[x][y] += 1; 
                if (al[x][y-1] != aleg) borders[x][y] += 2;
                if (al[x+1][y-(x%2)] != aleg) borders[x][y] += 4;
                if (al[x-1][y-(x%2)+1] != aleg) borders[x][y] += 8;
                if (al[x+1][y-(x%2)+1] != aleg) borders[x][y] += 16;
            }
        }
    }

    private void setAlegiances()
    {
        int x, y, aleg;
        aleg = 0;
        HexID drawHex = new HexID();
        for (y = 1; y <= mapRows; y++)
        {
            for (x = 1; x <= mapCols; x++)
            {
                drawHex.x = x+offsetCols; drawHex.y = y+offsetRows;
                if (!parentIcon.isDrawn(drawHex)) continue;
                aleg = 0;
                if (al[x][y] > 0) continue;
                aleg = ((aleg == 0) ? al[x][y-1] : aleg);
                aleg = ((aleg == 0) ? al[x][y+1] : aleg);
                
                aleg = ((aleg == 0) ? al[x-1][y] : aleg);
                aleg = ((aleg == 0) ? al[x+1][y] : aleg);
                
                aleg = ((aleg == 0) ? al[x-1][y + ((x%2 == 1)?-1:1)] : aleg);
                aleg = ((aleg == 0) ? al[x+1][y + ((x%2 == 1)?-1:1)] : aleg);
                
                if (aleg != 0) borders[x][y] = aleg;
            }
        }
        /*
         * In order to avoid simply moving the alegiances across the map, the 
         * update of the alegiances into the blank areas needs to be done in
         * two steps. First, find the potential updates, but don't mark them
         * in the al array (yet). once we found all the potential update based 
         * upon the unchanged base al array, then update them. 
         */
        for (y = 1; y <= mapRows; y++) 
            for (x = 1; x <= mapCols; x++)
                if (borders[x][y] > 0)
                {
                    al[x][y] = borders[x][y];
                    borders[x][y] = 0;
                }
    }

    public HexVectorImage getBorder (HexID xy)
    {
        boolean north = false; 
        boolean northEast = false; 
        boolean southEast = false;
        boolean south = false; 
        boolean southWest = false; 
        boolean northWest = false;
        
        if ((borders[xy.x][xy.y] & 1) > 0) northWest = true;
        if ((borders[xy.x][xy.y] & 2) > 0) north = true;
        if ((borders[xy.x][xy.y] & 4) > 0) northEast = true;
        if ((borders[xy.x][xy.y] & 8) > 0) southWest = true;
        if ((borders[xy.x][xy.y] & 16) > 0) southEast = true;
        return new HexVectorImage (north, northEast, southEast,
            south, southWest, northWest);
        
    }
    public void drawBorders(Graphics2D g2)
    {
        int x,y;
        if (borders == null) return;
        for (x=0; x < 6; x++) { hexBorder[x].setColor(borderColor); }
        for (y = 1; y <= mapRows; y++)
        {
            for (x = 1; x <= mapCols; x++)
            {
                if (borders[x][y] == 0) continue;
                center (x-1, y-1);
                if ((borders[x][y] & 1) > 0) drawHex(g2, hexBorder[4]);
                if ((borders[x][y] & 2) > 0) drawHex(g2, hexBorder[5]);
                if ((borders[x][y] & 4) > 0) drawHex(g2, hexBorder[0]);
                if ((borders[x][y] & 8) > 0) drawHex(g2, hexBorder[3]);
                if ((borders[x][y] & 16) > 0) drawHex (g2, hexBorder[1]);
            }
        }
    }
    
    public void setBorderColor(Color borderColor) { this.borderColor = borderColor; }
    public void setBorderStroke (BasicStroke borderStroke) 
    { 
        for (int i = 0; i < 6; i++) hexBorder[i].setStroke(borderStroke);
    }
    public void drawAll () {  }
    
    public boolean isDrawn (HexID hex) {return true;}

    public void setMapScale (MapScale hexScale)
    {
        this.scale = hexScale; 
        setMapScale (HexIcons.XSTART[hexScale.ordinal()], HexIcons.YSTART[hexScale.ordinal()]);
    }
    
    public void setMapScale (double xsize, double ysize)
    {
        setXSize(xsize);
        setYSize(ysize);
        AffineTransform at = AffineTransform.getScaleInstance(xsize/(float)HexIcons.XSTART[MapScale.SCALE_3.ordinal()], 
                    ysize/(float)HexIcons.YSTART[MapScale.SCALE_3.ordinal()]);
        for (int i = 0; i < 6; i++) hexBorder[i].transform(at);
    }
    
}