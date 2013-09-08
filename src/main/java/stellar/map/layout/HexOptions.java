package stellar.map.layout;


import stellar.map.LocationIDType;

import java.awt.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.prefs.*;

/**
 * HexOptions is a Bean to hold the current setting of the Hex Map options. There
 * are five copies of this data, one for each of the MapScales. These are kept 
 * in the EditOptions dialog (and accessed through that class). These values are 
 * kept (read/stored) in the user Preferences.
 * 
 * @author Thomas Jones-Low
 * @version $Id$
 */
public class HexOptions
{
    private Color backgroundColor = Color.black;
    private Color borderColor = Color.YELLOW;
    private Color foregroundColor = Color.WHITE;
    private Font  displayFont;
    
    private boolean showBorders = false;
    private boolean showJumpRoutes = false;
    private boolean showTravelZones = false;
    private LocationIDType locationID = LocationIDType.NONE;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public HexOptions()
    {
    }

    public HexOptions (HexOptions old)
    {
        backgroundColor = old.backgroundColor;
        borderColor = old.borderColor;
        foregroundColor = old.foregroundColor;
        displayFont = old.displayFont;
        showBorders = old.showBorders;
        showJumpRoutes = old.showJumpRoutes;
        showTravelZones = old.showTravelZones;
        locationID = old.locationID;
    }
    
    public void setBackgroundColor(Color backgroundColor)
    {
        Color oldBackgroundColor = this.backgroundColor;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = new Color ((~backgroundColor.getRed()) & 0xff, 
                                          (~backgroundColor.getGreen()) & 0xff, 
                                          (~backgroundColor.getBlue()) & 0xff);
        propertyChangeSupport.firePropertyChange(HexOptionsProperties.BACKGROUND_COLOR.toString(), 
                                                 oldBackgroundColor, backgroundColor);
    }

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setShowBorders(boolean showBorders)
    {
        boolean oldShowBorders = this.showBorders;
        this.showBorders = showBorders;
        propertyChangeSupport.firePropertyChange(HexOptionsProperties.SHOW_BORDERS.toString(), 
                                                 oldShowBorders, showBorders);
    }

    public boolean isShowBorders()
    {
        return showBorders;
    }

    public void setBorderColor(Color borderColor)
    {
        Color oldBorderColor = this.borderColor;
        this.borderColor = borderColor;
        propertyChangeSupport.firePropertyChange(HexOptionsProperties.BORDER_COLOR.toString(), 
                                                 oldBorderColor, borderColor);
    }

    public Color getBorderColor()
    {
        return borderColor;
    }

    public void setShowJumpRoutes(boolean showJumpRoutes)
    {
        boolean oldShowJumpRoutes = this.showJumpRoutes;
        this.showJumpRoutes = showJumpRoutes;
        propertyChangeSupport.firePropertyChange(HexOptionsProperties.SHOW_JUMP_ROUTES.toString(), 
                                                 oldShowJumpRoutes, showJumpRoutes);
    }

    public boolean isShowJumpRoutes()
    {
        return showJumpRoutes;
    }

    public void setShowTravelZones(boolean showTravelZones)
    {
        boolean oldShowTravelZones = this.showTravelZones;
        this.showTravelZones = showTravelZones;
        propertyChangeSupport.firePropertyChange(HexOptionsProperties.SHOW_TRAVEL_ZONES.toString(), 
                                                 oldShowTravelZones, showTravelZones);
    }

    public boolean isShowTravelZones()
    {
        return showTravelZones;
    }

    public void setLocationID(LocationIDType locationID)
    {
        LocationIDType oldLocationFill = this.locationID;
        this.locationID = locationID;
        propertyChangeSupport.firePropertyChange(HexOptionsProperties.LOCATION_ID.toString(), 
                                                 oldLocationFill, locationID);
    }

    public void setLocationID (int location)
    {
        setLocationID(LocationIDType.values()[location]);
    }
    
    public LocationIDType getLocationID()
    {
        return locationID;
    }

    public Color getForegroundColor()
    {
        return foregroundColor;
    }

    /**
     * Save the HexOptions to the Preferences backing store. Assumes you have set
     * the preferences node correctly.
     * @see Preferences#node
     * @param layoutPrefs
     */
    public void save(Preferences layoutPrefs)
    {
        layoutPrefs.putInt(HexOptionsProperties.LOCATION_ID.toString(), locationID.ordinal());
        layoutPrefs.putBoolean(HexOptionsProperties.SHOW_JUMP_ROUTES.toString(), showJumpRoutes);
        layoutPrefs.putBoolean(HexOptionsProperties.SHOW_BORDERS.toString(), showBorders);
        layoutPrefs.putBoolean(HexOptionsProperties.SHOW_TRAVEL_ZONES.toString(), showTravelZones);
        layoutPrefs.putInt(HexOptionsProperties.BORDER_COLOR.toString(), borderColor.getRGB());
        layoutPrefs.putInt(HexOptionsProperties.BACKGROUND_COLOR.toString(), backgroundColor.getRGB());
        layoutPrefs.put (HexOptionsProperties.FONT_NAME.toString(), displayFont.getName());
        layoutPrefs.putInt (HexOptionsProperties.FONT_SIZE.toString(), displayFont.getSize());
        layoutPrefs.putInt(HexOptionsProperties.FONT_STYLE.toString(), displayFont.getStyle());
    }
    
    /**
     * Load the complete set of HexOptions from the Preferences. Assumes that you 
     * have set the node correctly for the preferences.
     * @see Preferences#node
     * @param optionPrefs
     */
    public void load(Preferences optionPrefs)
    {
        setShowJumpRoutes(optionPrefs.getBoolean (HexOptionsProperties.SHOW_JUMP_ROUTES.toString(), false));
        setShowBorders(optionPrefs.getBoolean(HexOptionsProperties.SHOW_BORDERS.toString(), false));
        setShowTravelZones(optionPrefs.getBoolean(HexOptionsProperties.SHOW_TRAVEL_ZONES.toString(), false));
        setLocationID(optionPrefs.getInt(HexOptionsProperties.LOCATION_ID.toString(), 0));
        setBorderColor(new Color(optionPrefs.getInt(HexOptionsProperties.BORDER_COLOR.toString(), 0)));
        setBackgroundColor(new Color(optionPrefs.getInt(HexOptionsProperties.BACKGROUND_COLOR.toString(), 0)));
        setDisplayFont (new Font (
                            optionPrefs.get(HexOptionsProperties.FONT_NAME.toString(), "serif"),
                            optionPrefs.getInt(HexOptionsProperties.FONT_STYLE.toString(), Font.PLAIN),
                            optionPrefs.getInt(HexOptionsProperties.FONT_SIZE.toString(), 12)));
    }

    public void setDisplayFont(Font displayFont)
    {
        Font oldDisplayFont = this.displayFont;
        this.displayFont = displayFont;
        propertyChangeSupport.firePropertyChange(HexOptionsProperties.FONT_NAME.toString(), oldDisplayFont, displayFont);
    }

    public Font getDisplayFont()
    {
        return displayFont;
    }
}
