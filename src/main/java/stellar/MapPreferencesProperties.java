package stellar;

/**
 * An Enum of Properties managed by @see MapPreferences. These values are used
 * to load/store the properties in the MapPreferences from/to the Preferences
 * and as names for the PropertyChangeSupport. 
 * 
 * @author Thomas Jones-Low
 * @version $Id$
 */
public enum MapPreferencesProperties
{
    LOADED ("loaded"),
    REFERENCE_FILE ("referenceFile"),
    CURRENT_FILE ("currentFile"),
    WORKING_DIR ("workingDir"),
    USER_NAME ("userName"),
    DESCRIPTION ("description"),
    EMAIL_ADDRESS ("emailAddress"),
    WEBSITE ("website"),
    APP_WIDTH ("appWidth"), 
    APP_HEIGHT ("appHeight"),
    XPOS ("xPos"),
    YPOS ("yPos"),
    IMPORT_EXTERNAL_REFS ("importExternalRefs"),
    LAYOUT ("layout")
    ;
    
    MapPreferencesProperties (String name)
    {
        this.name = name;
    }
    private String name;
    
    @Override public final String toString() {return name; }
}
