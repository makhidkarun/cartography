/*
 * $Id: Resources.java,v 1.8 2008/11/03 22:06:06 tjoneslo Exp $
 * Copyright 2005 Softstart Services, Inc. All rights reserved. 
 */
package stellar.io;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Resources uses the <code>ClassLoader</code> to retrieve resources like
 * icons, external files or .property files in a file system independent manner. 
 * When deployed these resources are included in the JAR or WAR file, and this 
 * class makes sure the application has access to them. 
 * 
 * @version $Revision: 1.8 $
 * @author Thomas Jones-Low
 * @see java.lang.ClassLoader
 */
public class Resources 
{
    private static ImageIcon blankIcon; // see getBlankIcon, a default icon image.
    private static ResourceBundle rb;  // see getString, a collection of strings.

    // Default properties file, a collection of strings. 
    // This string is also used by the menu and toolbar to load names and icons
    // for the menu/toolbar items, and create the action handlers all in one go.
    // see swing.Command, MapCommands and io.AppDefaults
    public static final String rbWindows = "resources.WinGui";

    private static final String iconPath = "/resources/icons/";
    private static final String resourcesPath = "/resources/";
    
    public static final InputStream getPrefrences()
    {
        return getURLStream(resourcesPath + "prefsOrig.xml");
    }
    
    /**
     * Gets the base default references file used by the application. 
     * This file contains a large set of default data which can later be added
     * to or altered by the user. 
     * @return URL reference to the references file. 
     */
    public static final URL getReferences()
    {
        return getURL(resourcesPath + "AstroML.xml");
    }
    
    /**
     * Gets an icon by the base file name. This code adds the path and type to
     * the base name and loads the picture into a new <code>IconImage</code>. 
     * @return The loaded icon ready for use
     * @param name base name of the Icon.
     */
    public static final Icon getIcon (String name)
    {
        URL path = getURL(iconPath + name + ".gif");
        if (path == null) path = getURL (iconPath + name + ".png");
        if (path == null) throw new NullPointerException ("Icon name " + name + " not found");
        return new ImageIcon (path);
    }

    /**
     * Gets a special blank icon used for filling space on menu items which don't
     * have icons or other places where a small blank space needs to be drawn. 
     * @return Blank icon 
     */
    public static final Icon getBlankIcon ()
    {
        if (blankIcon == null)
        {
            blankIcon = new ImageIcon (getURL(iconPath + "blank.gif"));
        }
        return blankIcon;
    }
    
    /**
     * Gets a URL from string file name using the system class loader to find 
     * the resource in the JAR or WAR file.
     * @return URL to the file or resource. 
     * @param name name of resource.
     */
    private static final URL getURL(String name)
    {
        URL returnValue;
        returnValue = ClassLoader.getSystemClassLoader().getSystemResource(name);
        
        if (returnValue == null)
            returnValue = Resources.class.getResource(name);
            
        return returnValue;
    }
    
    /**
     * Gets an <code>InputStream</code> from a string file name using the 
     * system class loader. Some API require a URL and some require an 
     * <code>InputStream</code>, hence the requirement for both. 
     * @return <code>InputStream</code> of the open file or resource.
     * @param name Name of the resource.
     */
    private static final InputStream getURLStream (String name)
    {
        InputStream returnValue;
        returnValue = ClassLoader.getSystemClassLoader().getSystemResourceAsStream(name);
        
        if (returnValue == null)
            returnValue = Resources.class.getResourceAsStream(name);
            
        return returnValue;
        
    }
    
    /**
     * Gets the ResourceBundleName for the current GUI. Currently returns only
     * WinGui, but when the application supports more than just one Gui, this
     * is where the current GUI for the application will be determined. 
     * @return Resource name for the current GUI.
     */
    public static final String getResourceBundleName ()
    {
        return rbWindows;
    }
    /**
     * Gets a string from the default resource bundle based upon the key.
     * @return string from the resource bundle
     * @param name key name of the resource. 
     */
    public static final String getString (String name)
    {
        if (rb == null) rb = ResourceBundle.getBundle(rbWindows);
        return rb.getString(name);
    }
}
