/*
 *  $Id$
 *  Copyright 2006 by Softstart Services Inc. 
 */
package stellar.map;
import stellar.dialog.EditOptions;
import stellar.swing.AstrogationChangeListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.imageio.ImageIO;
import stellar.data.Astrogation;
import stellar.data.HexID;
import stellar.data.StarSystem;
import stellar.data.GroupRecord;
import stellar.data.GroupType;


import stellar.map.layout.HexLayout;

import java.util.*;

import javax.swing.event.ChangeEvent;

/**
 * MapIcon is a Icon (graphic image) on which the (generic) map is drawn. The 
 * Icon itself is kept in offscreenImage, a BufferedImage, as a offscreen buffer.
 * The abstract method drawAll() will (should) draw the graphics on this offscreen
 * image. The data for this display comes from the Astrogation data. 
 * 
 * To use this, add the MapIcon as the Icon to a JLabel. Keep in mind this image
 * can be quite large, and you may need to put your JLabel in a JScrollPane to
 * keep your map a reasonable display size. 
 * 
 * MapIcon supports 5 scales, which are the size of the elements drawn on the map. 
 * SCALE_1 repesents the smallest elements, up to SCALE_5 which represents the
 * largest elements. The zoomIn() moves the scale to the next larger scale,
 * zoomOut() moves to the next smaller scale. 
 * 
 * MapIcon supports levels (via getLevel() and setLevel()), which selects the 
 * group type to use for the map. In addition to the GroupRecord Domain, Sector, 
 * Quadrant, Subsector, MapIcon adds System level (the default). By selecting a
 * level, MapIcon should display the map at that level. For example, selecting 
 * a level of Subsector should display a map of subsectors from the Astrogation data.
 * 
 * MapIcon supports scope (via setScope()), which selects a limitation to the 
 * number of map elements (systems, etc) displayed. The Scope can be set to any of
 * the GroupRecord indexes, or to ALL, which is eveything in the Astrogation
 * data. By default, setting the scope also set the map size in terms of map 
 * elements and if you want a different size, you will need to explicitly override 
 * it. For example, if you set the scope to subsector, the map will display all
 * of the systems in the subsector, with a map size based upon the size of a 
 * subsector at the current scale. 
 * 
 * @version $Revision: 1.11 $
 * @author  $Author$
 */
public abstract class MapIcon implements Icon, AstrogationChangeListener
{
    protected BufferedImage offscreenImage;
    private GroupRecord mapGroup;
    private Astrogation mapData;
    MapScale scale = MapScale.SCALE_3;
    GroupType level = GroupType.SYSTEM;
    MapScope scope = MapScope.ALL;
    boolean changed = true;
    boolean dataChanged = true;
    boolean scaleChanged = true;
    boolean sizeChanged = true;
    
    Dimension mapSize = new Dimension();
 
    // size of map (in hexes)
    int mapRows;
    int mapCols;
    int offsetRows = 1;
    int offsetCols = 1;
    int maxRows = 0;
    int maxCols = 0;
    Dimension mapExtent = new Dimension (1,1);
    Dimension mapOffset = new Dimension (offsetRows, offsetCols);

    // Layout for the five scales of hexes/squares
    protected Map <MapScale, HexLayout> layouts = new EnumMap<MapScale, HexLayout> (MapScale.class);
    protected DrawHexLayout drawLayout;
    
    protected abstract void drawAll();
    protected abstract void calcMapSize(int rows, int cols);
    protected abstract Point2D center(int x, int y);
    public abstract HexID getLocation (int x, int y);
    protected abstract boolean isDrawn (HexID hex);
    
    public MapIcon (boolean set)
    {
        if (set)
        {
            setLayout (MapScale.SCALE_5, EditOptions.getInstance().getScaleLayout(MapScale.SCALE_5));
            setLayout (MapScale.SCALE_4, EditOptions.getInstance().getScaleLayout(MapScale.SCALE_4));
            setLayout (MapScale.SCALE_3, EditOptions.getInstance().getScaleLayout(MapScale.SCALE_3));
            setLayout (MapScale.SCALE_2, EditOptions.getInstance().getScaleLayout(MapScale.SCALE_2));
            setLayout (MapScale.SCALE_1, EditOptions.getInstance().getScaleLayout(MapScale.SCALE_1));
            scale = MapScale.SCALE_3;
        }
    }
    
    public void setMapOffset (int rows, int cols)
    {
/*    
        offsetRows = (rows > 0) ? (rows) : (1); 
        offsetRows = (offsetRows < maxRows) ? offsetRows : maxRows;
        offsetCols = (cols > 0) ? (cols) : (1); 
        offsetCols = (offsetCols < maxCols) ? offsetCols : maxCols;
*/        
        offsetRows = rows;
        offsetCols = cols;
        mapOffset.height = offsetRows;
        mapOffset.width = offsetCols;
    }
    

    public Dimension getMapOffset () { return mapOffset;}
    
    /**
     * calulate the center point of hex (in pixels) given the hex location
     * @return the center pixel. 
     */
    protected Point2D center (HexID xy)
    {
        return center (xy.x - 1, xy.y - 1);
    }
    /**
     * Get the currently displayed map data. 
     * @return The map data currently being displayed. 
     */
    public Astrogation getMapData () { return mapData; }
    public GroupRecord getGroupData () { return mapGroup; } 
    
    public void stateChanged (ChangeEvent e)
    {
        setMapData ((Astrogation)e.getSource());
    }
    /**
     * Set a new set of data to be displayed in the map. 
     * @param data Astrogation map data
     */
    public void setMapData (Astrogation data) 
    {         
        mapData = data; 
        changed = true;
        dataChanged = true;
        setSize();
    }

    protected void drawSystemLayout (Graphics2D g2)
    {
        StarSystem s;
        HexID l;
        Iterator<StarSystem> i;
        Point2D p; 
        DrawHexLayout d = new DrawHexLayout (getLayout());
        //fillLinks(g2);
        if (mapGroup != null) i = mapGroup.getSystems();
        else i = mapData.getSystems();
        
        while (i.hasNext())
        {
            s = i.next();
            l = s.getLocation(); 
            //l.convertHextoID();
            if (!isDrawn (l)) continue;
            p = center(l.x-offsetCols, l.y-offsetRows);

            d.drawLayout(s, p, g2);            
        }
    }

    /**
     * Make the hexes smaller. Zoom
     * is a fixed amount to perserve the shape of the hexes. 
     */
    public void zoomOut ()
    {
        setScale (MapScale.previous (scale));
        //setScale (scale - 1);
    }

    /**
     * Make the hexes larger. Zoom is a fixed amount to preserve the 
     * shape of the hexes. 
     */
    public void zoomIn()
    {
        setScale (MapScale.next(scale));
        //setScale (scale + 1);
    }

    /**
     * Change the size of the hexes to one of the predefined scales. 
     */
    public void setScale (MapScale scale)
    {
        scaleChanged = true;
        this.scale = scale;
        calcMapSize (mapRows, mapCols);
        drawLayout = new DrawHexLayout (getLayout());
    }
    
    /**
     * get the currently set map boundry element size. 
     * @return size of the map boundry elements. 
     */
    public MapScale getScale () { return scale; }
    
    /**
     * Set the map size regardless of the internal data
     * @param rows Number of map rows (vertical)
     * @param cols Number of map columns (horizontal)
     */
    public void setSize(int rows, int cols) 
    { 
        sizeChanged = true;
        calcMapSize (rows, cols);
        mapExtent.height = rows;
        mapExtent.width = cols;
    }

    /**
     * set the size of the map based upon the maximum extent of the 
     * elements in the Astrogation data, based upon the display group level.
     */
    public void setSize()
    {
    
        int maxCols = 1;
        int maxRows = 1;
        //Iterator<GroupRecord> i;
        GroupRecord g;
        StarSystem s;
        HexID l;

        if (mapData != null)
        {
            if (level.ordinal() > GroupType.SYSTEM.ordinal())
            {
                for (Iterator<GroupRecord> i = mapData.getGroups(); i.hasNext(); )
                {
                    g = i.next();
                    if (g.getType() != level) continue;
                    l = g.getLocation();
                    maxCols = (l.x > maxCols) ? l.x : maxCols;
                    maxRows = (l.y > maxRows) ? l.y : maxRows;
                }
            }
            else
            {
                if (scope.compareTo(MapScope.ALL) < 0 && mapGroup != null)
                {
                    maxCols = mapGroup.getExtentX();
                    maxRows = mapGroup.getExtentY();
                }
                else if (mapData.getSystemCount() < 1)
                {
                    maxRows = 10;
                    maxCols = 8;
                }
                else
                {
                    for (Iterator<StarSystem> i = mapData.getSystems(); i.hasNext(); )
                    {
                        s = i.next();
                        l = s.getLocation();
                        maxCols = (l.x > maxCols) ? l.x : maxCols;
                        maxRows = (l.y > maxRows) ? l.y : maxRows;
                    }
                }
            }
        }
        setSize (maxRows, maxCols);
    }
    /**
     * Get the size (in hexes or squares) of the display map.
     * @return Map size.
     */
    public Dimension getMapSize () { return mapExtent; }

    /**
     * Get the star system at a given pixel location on the map
     * @param x x coordinate
     * @param y y coordinate
     * @return Star system structure
     */    
    public StarSystem getSystemData (int x, int y) { return getSystemData (getLocation(x,y)); }

    /**
     * Get the map data as a specific hex from the map data. 
     * @param xy The location being inquired about
     * @return The star system at the location. 
     */
    public StarSystem getSystemData (HexID xy) { return mapData == null ? null : mapData.getSystem (xy); }
    
    /**
     * Get the information of a given map location
     * @param xy Map location
     * @return String description of the location (usually the name)
     */
    public String getLocationInfo (HexID xy)
    {
        try
        {
            if (level == GroupType.SYSTEM) return mapData.getSystem(xy).toString();
            else return mapData.getGroup (xy, level).toString();
        }
        catch (NullPointerException e) {;}
        return xy.toString();
    }

    /**
     *     Draw the icon at the specified location. Icon implementations may 
     *     use the Component argument to get properties useful for painting, 
     *     e.g. the foreground or background color.
     *     @see javax.swing.Icon
     */
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        if (changed)
        {
            offscreenImage = new BufferedImage (mapSize.width, mapSize.height, BufferedImage.TYPE_INT_RGB);
            drawAll();
        }
        g.drawImage(offscreenImage, x, y, c);
    }

    /**
     * returns the icon's width
     * @return an int specifying the fixed width of the icon.
     * @see javax.swing.Icon
     */
    public int getIconWidth () { return mapSize.width; }
    
    /**
     * returns the icon's height
     * @return an int specifying the fixed height of the icon.
     * @see javax.swing.Icon
     */
    public int getIconHeight() { return mapSize.height; }

    /**
     * returns the icon's size
     * @return a Dimension specifying the width and height of the icon.
     * @see javax.swing.Icon
     */
    public Dimension getIconSize() { return mapSize; } 
    public void redrawMap()
    {
        if (offscreenImage == null) return;
        changed = true;
        //drawAll();
    }

    public void clearMap()
    {
        //mapData = null; mapGroup = null;
        Graphics g = offscreenImage.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, mapSize.width, mapSize.height);
    }

    /**
     * Set the hex/square layout mode for a given scale.
     * @see com.softstart.stellar.dialog.GroupLayoutPanel
     * @see HexLayout
     * @param scale The scale to set for the layout
     * @param layout The HexLayout to use for this map scale. 
     */
    public void setLayout (MapScale scale, HexLayout layout)
    {
        layouts.put (scale, layout);
        layout.setScale(scale);
        //layouts[scale.ordinal()] = layout; layout.setScale(scale);
    }

    public HexLayout getLayout () { return layouts.get(scale); }
    
    public GroupType getLevel()
    {
        return level;
    }

    public void setLevel(GroupType level)
    {
        this.level = level;
        changed = true;
    }

    public MapScope getScope()
    {
        return scope;
    }

    public void setScope(MapScope scope)
    {
        this.scope = scope;
        changed = true;
        mapGroup = null;
        
        if (mapData == null) { setGroup (null); return; }
        
        for (Iterator i = mapData.getGroups(); i.hasNext(); )
        {
            GroupRecord g = (GroupRecord)i.next();
            if (g.getType().ordinal() == scope.ordinal()) 
            {
                setGroup (g);
                return;
            } 
        }
        setGroup (null);
    }
    
    public void setGroup (GroupRecord g)
    {
        mapGroup = g;

        if (g != null)
        {
            setMapOffset (g.getOffset().width, g.getOffset().height);
        }
        else
        {
            setMapOffset (1,1);
        }
        changed = true;
        dataChanged = true;
        setSize();
    }

    public void write (File file) throws IOException
    {
        ImageIO.write(offscreenImage, "jpeg", file);
    }
}
