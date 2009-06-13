package stellar.map;
import com.softstart.VectorImage.HexVectorImage;
import com.softstart.VectorImage.LineFillVectorImage;
import com.softstart.VectorImage.OctogonVectorImage;

import stellar.dialog.EditOptions;

import stellar.map.layout.HexOptions;

import stellar.map.layout.ShortLineList;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.font.FontRenderContext;
import stellar.data.StarSystem;
import stellar.data.SystemMarkerType;

import stellar.map.layout.HexLayout;

import stellar.map.layout.HexLine;

import java.awt.*;

import java.util.*;

import javax.swing.text.StyleConstants;

import stellar.MapPreferences;

public class DrawHexLayout //implements DisplayOptions 
{
    private HexLayout layout;
    //private ArrayList <HexLinePanel> drawItems; 
    //private HexOptions options;
    //private HexOptionPanel options;
    //private MapScale scale;
    private int xsize; 
    private int ysize;
    private double fontHeight;
    
    //private ArrayList <MarkerVectorImage> systemMarkers; 
    private MarkerVectorImage system = new MarkerVectorImage ();
    private ZoneVectorImage travelZone = new ZoneVectorImage();
    private OctogonVectorImage marker = new OctogonVectorImage();
    private LineFillVectorImage hexFill; 
    private HexVectorImage hexOutline = new HexVectorImage();

    public DrawHexLayout (int lineItems, MapScale scale)
    {
        layout = new HexLayout (lineItems, scale);
    }
    
    public DrawHexLayout (DrawHexLayout oldLayout)
    {
        layout =new HexLayout (oldLayout.layout);
        setScale (oldLayout.layout.getScale());
    }
    
    public DrawHexLayout (HexLayout layout)
    {
        this.layout = layout;
        setScale (layout.getScale());
    }

    public int getItemCount () { return layout.getLineCount(); } 
    public HexLine getItem (int index) { return layout.getLine(index); }

    public void setScale (MapScale hexScale)
    {
        layout.setScale(hexScale);
        setScale (HexIcons.XSTART[layout.getScaleOrdinal()], HexIcons.YSTART[layout.getScaleOrdinal()]);
    }
    
    public void setScale (double xsize, double ysize)
    {
        this.xsize = (int)xsize;
        this.ysize = (int)ysize;
        AffineTransform at = AffineTransform.getScaleInstance(xsize/(float)HexIcons.XSTART[MapScale.SCALE_3.ordinal()], 
                    ysize/(float)HexIcons.YSTART[MapScale.SCALE_3.ordinal()]);

        travelZone.transform(at);
        system.transform(at);
        hexOutline.transform(at);
        marker.transform(at);
        setFontHeight (-1.0);
    }
    
    public void setFontHeight (double height) { this.fontHeight = height; } 
    public double getFontHeight (Graphics2D g2)
    {
        if (fontHeight == -1.0)
        {
            FontRenderContext frc = g2.getFontRenderContext();
            fontHeight = g2.getFontMetrics().getHeight();
        }           
        return fontHeight;        
    }
    
    /**
     * draw the layout of the hex line items in the hex. The center should really
     * be the center of the hex, rather than HexIcon's "hexCenterPoint" which is 
     * really the upper left corner of the area where the hex is drawn. 
     * @param s the star system to provide the data for input
     * @param center the center of the hex to draw
     * @param g2 the graphics context for drawing into
     */
    public void drawLayout (StarSystem s, Point2D center, Graphics2D g2)
    {
        HexLine currentItem;
        double lineOffset, put;
        Point2D.Double centerLine = new Point2D.Double();
        centerLine.x = center.getX();
        centerLine.y = center.getY();
        
        /* StarSystem may be null for drawing the legend for some maps */
        /* @see HexLegend.drawLegend() */
        //if (s == null) s = options.getMapData().getSystem(0);
        if (s == null) s = MapPreferences.getInstance().getMapData().getSystem(0);

        if (getTravelZone()) drawZone (s, centerLine, g2);
        
        lineOffset = (layout.getLineCount()- 1) / 2.0;
        for (int i = layout.getLineCount(); i > 0; i--)
        {
            put = i - lineOffset - 1.5;
            currentItem = layout.getLine(i - 1);
            centerLine.x = center.getX();
            centerLine.y = center.getY() + (put * getFontHeight(g2));

            if (currentItem.isLongSelected())
            {
                drawLongItem (s, currentItem, centerLine, g2);
            }
            else if (currentItem.isThreeShortItems())
            {
                drawThreeShortItems (s, currentItem, centerLine, g2);
            }
            else
            {
                drawTwoShortItems (s, currentItem, centerLine, g2);
            }
        }
    }

    public Rectangle2D getStringBounds (String s, Graphics2D g2)
    {
        FontRenderContext frc = g2.getFontRenderContext();
        return g2.getFont().getStringBounds(s, frc);
    }

    public void drawString (String s, int alignment, Point2D center, Graphics2D g2)
    {
        Rectangle2D bound;
        Point2D.Double start = new Point2D.Double();
        
        bound = getStringBounds (s, g2);
        start.x = center.getX();
        start.y = center.getY() + bound.getHeight();

        switch (alignment)
        {
            case StyleConstants.ALIGN_LEFT: 
                start.x -= bound.getWidth();
                break;
            case StyleConstants.ALIGN_CENTER: 
                start.x -= bound.getWidth()/2.0;
                break;
            case StyleConstants.ALIGN_RIGHT: 
                break;
        }
        //g2.setColor(getBackgroundColor());
        g2.translate(start.x, start.y);
        //g2.fill((Shape)bound);
        g2.setColor(getForegroundColor());
        g2.drawString(s, 0, 0);
        g2.translate(-start.x, -start.y);
    }
    
    private void drawLongItem (StarSystem s, HexLine p, Point2D center, Graphics2D g2)
    {
        String longString = null;
        switch (p.getLongItem())
        {
            case NONE: return;
            case NAME: longString = s.getName(); break;
            case HEX_ID: longString = s.getLocation().toString(); break;
            case UWP: longString = s.getPlanet().toString(); break;
            case TRADE_CODE: longString = (s.getTradeCodes() != null ? s.getTradeCodes() : "") + 
                                " " + 
                                (s.getRemarks() != null ? s.getRemarks() : ""); 
                break;
        }
        if (longString != null)
        {
            drawString (longString, StyleConstants.ALIGN_CENTER, center, g2);
        }
    }
    
    private void drawThreeShortItems (StarSystem s, HexLine p, Point2D center, Graphics2D g2)
    {
        String text;
        ShortLineList markerIndex;
        Point2D.Double draw = new Point2D.Double ();

        draw.x = center.getX() - xsize;
        draw.y = center.getY();
        text = getShortItemText (s, p, 1);
        if (text != null)
        {
            drawString (text, StyleConstants.ALIGN_LEFT, draw, g2);
        }
        else if (p.getShortItem1() != ShortLineList.NONE)
        {
            markerIndex = p.getShortItem1();
            drawMarkers (markerIndex, s, draw, g2);
        }
        draw.setLocation(center);
        text = getShortItemText (s, p, 2);
        if (text != null)
        {
            drawString (text, StyleConstants.ALIGN_CENTER, draw, g2);
        }
        else if (p.getShortItem2() != ShortLineList.NONE)
        {
            markerIndex = p.getShortItem2();
            drawMarkers (markerIndex, s, draw, g2);
        }
        draw.setLocation(center);
        draw.x += xsize;
        text = getShortItemText(s, p, 3);
        if (text != null)
        {
            drawString (text, StyleConstants.ALIGN_RIGHT, draw, g2);
        }
        else if (p.getShortItem3() != ShortLineList.NONE)
        {
            markerIndex = p.getShortItem3();
            drawMarkers (markerIndex, s, draw, g2);
        }
    }
    
    private void drawTwoShortItems (StarSystem s, HexLine p, Point2D center, Graphics2D g2)
    {
        String text;
        ShortLineList markerIndex;
        Point2D.Double draw = new Point2D.Double ();
        
        /* draw item 1 right justified against center - xsize */
        draw.x = center.getX() - xsize;
        draw.y = center.getY();
        text = getShortItemText(s, p, 1);
        if (text != null)
        {
            drawString (text, StyleConstants.ALIGN_RIGHT, draw, g2);
        }
        else if (p.getShortItem1() != ShortLineList.NONE)
        {
            markerIndex = p.getShortItem1();
            drawMarkers (markerIndex, s, draw, g2);
        }
        /* draw item 2 left justified against  center + xsize */
        text = getShortItemText (s, p, 2); 
        draw.x = center.getX() + xsize;
        draw.y = center.getY();
        if (text != null)
        {
            drawString (text, StyleConstants.ALIGN_LEFT, draw, g2);
        }
        else if (p.getShortItem2() != ShortLineList.NONE)
        {
            markerIndex = p.getShortItem2();
            drawMarkers (markerIndex, s, draw, g2);
        }
    }
    
    private String getShortItemText (StarSystem s, HexLine p, int shortItem)
    {
        String text = null;
        ShortLineList itemIndex = ShortLineList.NONE; 
        
        if (shortItem == 1) itemIndex = p.getShortItem1();
        if (shortItem == 2) itemIndex = p.getShortItem2();
        if (shortItem == 3) itemIndex = p.getShortItem3();
        
        switch (itemIndex)
        {
            case NONE: text = null; break;
            case BASE_CODE: 
                if (s.getBase() == ' ') text = null;
                else text = Character.toString(s.getBase()); 
                break;
            case POLITY: text = s.getPolity(); break;
            case STARPORT: text = Character.toString(s.getPlanet().getPort()); break;
            case POP_CODE: text = Integer.toString(s.getMultiplier()); break; 
            case GG_CODE: text = Integer.toString(s.getGiants()); break;
            case BELTS_CODE: text = Integer.toString(s.getBelts()); break; 
            case PBG_CODE: text = Integer.toString(s.getMultiplier()) + 
                            Integer.toString(s.getBelts()) + 
                            Integer.toString(s.getGiants()); break;
            case ZONE_CODE: text = Character.toString(s.getZone()); break;
        }
        return text;         
    }
    
    private void drawHexFill (Point2D p, Graphics2D g2)
    {
        Shape oldClip;
        oldClip = g2.getClip();
        g2.translate (p.getX(), p.getY());
        g2.clip(hexOutline);
        hexFill.drawImage(g2);
        g2.translate (-p.getX(), -p.getY());
        g2.setClip(oldClip);
    }
    
    private void drawZone (StarSystem s, Point2D p, Graphics2D g2)
    {
        Point2D.Double draw = new Point2D.Double();
        if (s.getZone() == ' ') return;
        draw.x = p.getX() - travelZone.getBounds2D().getWidth()/2.0;
        draw.y = p.getY() - travelZone.getBounds2D().getHeight()/2.0;

        travelZone.setZone(s.getZone());
        g2.translate(draw.x, draw.y);
        travelZone.drawImage(g2);
        g2.translate(-draw.x, -draw.y);
    }
    
    private void drawMarker (Point2D center, Graphics2D g2)
    {
        Point2D.Double draw = new Point2D.Double();
        draw.x = center.getX() - marker.getBounds2D().getWidth()/2.0;
        draw.y = center.getY();// + marker.getBounds().height;
        g2.translate (draw.x, draw.y);
        marker.setColor(getForegroundColor());
        marker.drawImage(g2);
        g2.translate(-draw.x, -draw.y);
    }
    
    private void drawMarkers (ShortLineList markerIndex, StarSystem s, Point2D center, Graphics2D g2)
    {
        Point2D.Double draw = new Point2D.Double();
        draw.x = center.getX();
        draw.y = center.getY();
        if (system.getBounds2D().getHeight() < getFontHeight(g2))
        {
            draw.y = draw.y + ((getFontHeight(g2) - system.getBounds2D().getHeight())/2);
        }
        switch (markerIndex)
        {
            case SYSTEM_MARKER:
                draw.x -= system.getBounds2D().getWidth()/2.0;
                system.setMarker(SystemMarkerType.PRESENCE);
                system.drawWorld(g2, draw, s);
                break;
            case SYSTEM_WATER:
                draw.x -= system.getBounds2D().getWidth()/2.0;
                system.setMarker(SystemMarkerType.HYDROGRAPHIC);
                system.drawWorld(g2, draw, s);
                break;
            case SYSTEM_SIZE:
                draw.x -= system.getBounds2D().getWidth()/2.0;
                system.setMarker(SystemMarkerType.SIZE);
                system.drawWorld(g2, draw, s);
                break;
            case SYSTEM_ATMO:
                draw.x -= system.getBounds2D().getWidth()/2.0;
                system.setMarker(SystemMarkerType.ATMOSPHERE);
                system.drawWorld (g2, draw, s);
                break;
            case SYSTEM_POPULATION:
                draw.x -= system.getBounds2D().getWidth()/2.0;
                system.setMarker(SystemMarkerType.POPULATION);
                system.drawWorld (g2, draw, s);
                break;
            case SYSTEM_GOVERNMENT:
                draw.x -= system.getBounds2D().getWidth()/2.0;
                system.setMarker (SystemMarkerType.GOVERNMENT);
                system.drawWorld(g2, draw, s);
                break;
            case SYSTEM_LAW:
                draw.x -= system.getBounds2D().getWidth()/2.0;
                system.setMarker (SystemMarkerType.LAWLEVEL);
                system.drawWorld(g2, draw, s);
                break;
            case SYSTEM_TECH:
                draw.x -= system.getBounds2D().getWidth()/2.0;
                system.setMarker (SystemMarkerType.TECHNOLOGY);
                system.drawWorld (g2, draw, s);
                break;
            case SYSTEM_PRIMARY:
                draw.x -= system.getBounds2D().getWidth()/2.0;
                system.setMarker(SystemMarkerType.PRIMARY);
                system.drawWorld(g2, draw, s);
                break;
            case SYSTEM_STARPORT:
                draw.x -= system.getBounds2D().getWidth()/2.0;
                system.setMarker (SystemMarkerType.PORT);
                system.drawWorld (g2, draw, s);
                break;
            case GG_MARKER:
                if (s.getGiants() > 0)
                {
                    drawMarker (draw, g2);
                }
                break;
            case BELT_MARKER:
                if (s.getBelts() > 0)
                {
                    drawMarker (draw, g2);
                }
                break;
            case BASE_MARKER:
                if (s.getBase() != ' ')
                {
                    drawMarker (draw, g2);
                }
                break;
            default: break;
        }
    }
    
    
    public LocationIDType getHexIDOption () { return layout.getOptions().getLocationID(); }    
    public boolean getJumpRoutes() { return layout.getOptions().isShowJumpRoutes(); } 
    public boolean getDrawBorders() { return layout.getOptions().isShowBorders(); } 
    public boolean getTravelZone() { return layout.getOptions().isShowTravelZones(); } 
    public Color   getBorderColor() { return layout.getOptions().getBorderColor(); }
    public Color   getBackgroundColor() { return layout.getOptions().getBackgroundColor(); }
    public Color   getForegroundColor() { return layout.getOptions().getForegroundColor(); }
    public void    setBackgroundColor(Color newColor) { layout.getOptions().setBackgroundColor(newColor); }
    public Font    getDisplayFont () { return layout.getOptions().getDisplayFont(); } 
    
    public boolean isSystemStarportMarkerUsed () { return layout.isShortIndexUsed (ShortLineList.SYSTEM_STARPORT); }
    public boolean isSystemSizeMarkerUsed() { return layout.isShortIndexUsed (ShortLineList.SYSTEM_SIZE); } 
    public boolean isSystemAtmosphereMarkerUsed () { return layout.isShortIndexUsed (ShortLineList.SYSTEM_ATMO); } 
    public boolean isSystemHydrographicMarkerUsed () { return layout.isShortIndexUsed (ShortLineList.SYSTEM_WATER); } 
    public boolean isSystemPopulationMarkerUsed () { return layout.isShortIndexUsed (ShortLineList.SYSTEM_POPULATION); } 
    public boolean isSystemGovernmentMarkerUsed () { return layout.isShortIndexUsed (ShortLineList.SYSTEM_GOVERNMENT); } 
    public boolean isSystemLawLevelMarkerUsed () { return layout.isShortIndexUsed (ShortLineList.SYSTEM_LAW); } 
    public boolean isSystemTechnologyMarkerUsed() { return layout.isShortIndexUsed (ShortLineList.SYSTEM_TECH); } 
    public boolean isSystemPrimaryMarkerUsed () { return layout.isShortIndexUsed (ShortLineList.SYSTEM_PRIMARY); } 

    public boolean isBasesUsed ()
    {
        return layout.isShortIndexUsed (ShortLineList.BASE_CODE);
    }
    
    public boolean isAllegiancesUsed()
    {
        return layout.isShortIndexUsed (ShortLineList.POLITY);
    }
    
    public boolean isStarportUsed()
    {
        return layout.isShortIndexUsed (ShortLineList.STARPORT);
    }
    
    public boolean isTravelZoneUsed()
    {
        return layout.isShortIndexUsed (ShortLineList.ZONE_CODE) || getTravelZone();
    }

    public void setLayout(HexLayout layout)
    {
        this.layout = layout;
        setScale (layout.getScale());
    }

    public HexLayout getLayout()
    {
        return layout;
    }
}
