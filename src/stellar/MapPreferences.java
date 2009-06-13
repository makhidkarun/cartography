package stellar;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStream;

import java.util.EnumMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import stellar.data.Astrogation;
import stellar.data.GroupRecord;
import stellar.data.GroupType;
import stellar.data.HexID;
import stellar.data.ProviderRecord;

import stellar.data.References;

import stellar.data.StarSystem;
import stellar.data.UWP;

import stellar.dialog.EditOptions;

import stellar.dialog.HexLinePanel;

import stellar.io.Resources;

import stellar.map.MapScale;
import stellar.map.layout.HexLayout;
import stellar.map.layout.HexLineProperties;

public class MapPreferences
{
    private static MapPreferences _instance;

    /* Application parameter values */
    private String workingDir;
    private String currentFile;
    private int appWidth, appHeight, appX, appY;
    private ProviderRecord userData;

    private String userName;
    private String description;
    private String emailAddress;
    private String website;

    private boolean programImportReferences = true;
    private String externalRefsFileName;
    
    private Map <MapScale, HexLayout> scaleOptions = new EnumMap <MapScale, HexLayout> (MapScale.class);
    // This is a small map used for the HexOptionPanels
    private Astrogation mapData;
    
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    public static synchronized MapPreferences getInstance()
    {
        if (_instance == null)
            _instance = new MapPreferences();
        return _instance;
    }

    public MapPreferences()
    {
        scaleOptions.put (MapScale.SCALE_1, new HexLayout(1, MapScale.SCALE_1));
        scaleOptions.put (MapScale.SCALE_2, new HexLayout(2, MapScale.SCALE_2));
        scaleOptions.put (MapScale.SCALE_3, new HexLayout(3, MapScale.SCALE_3));
        scaleOptions.put (MapScale.SCALE_4, new HexLayout(4, MapScale.SCALE_4));
        scaleOptions.put (MapScale.SCALE_5, new HexLayout(5, MapScale.SCALE_5));
        loadPreferences();
        initMapData();
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public void restorePreferences (boolean load)
    {
        Preferences userPrefs;
        InputStream is;

        is = Resources.getPreferences();
        userPrefs = Preferences.userNodeForPackage(this.getClass());

        try
        {
            userPrefs.removeNode();
            Preferences.importPreferences(is);
            is.close();
            userPrefs.flush();
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (BackingStoreException ex)
        {
            // Means we can't load the preferences from prefsOrig.xml into
            // the backing store. We don't care. Either the prefsOrig.xml
            // is corrupt or we have bigger problems. 
        }
        catch (IOException ex)
        {
            // Here reading the prefsOrig.xml from the resources file has failed
            // for one reason or another. 
        }
        catch (InvalidPreferencesFormatException ex)
        {
            // The prefsOrig.xml format is bad?? Should never happen?
            // Might happen during the debugging process, log a message to the
            // Console. 
            System.out.println(ex.getMessage());
        }
        if (load) loadPreferences();

    }

    private void loadPreferences()
    {
        boolean loaded = false;
        Preferences userPrefs, layoutPrefs;
    
        userPrefs = Preferences.userNodeForPackage(this.getClass());

        try {
        if (!userPrefs.nodeExists(MapPreferencesProperties.LOADED.name()))
        {
            restorePreferences(false);
            loaded = true;
        }
        }
        catch (BackingStoreException ex)
        {
            // There's something wrong with the Backing store, should never (or
            // rarely ever) happen. 
            // TODO: How to handle this error?
            ex.printStackTrace();
        }
        appWidth = userPrefs.getInt(MapPreferencesProperties.APP_WIDTH.name(), 200);
        appHeight = userPrefs.getInt(MapPreferencesProperties.APP_HEIGHT.name(), 300);
        appX = userPrefs.getInt(MapPreferencesProperties.XPOS.name(), 80);
        appY = userPrefs.getInt(MapPreferencesProperties.YPOS.name(), 80);
        currentFile = userPrefs.get(MapPreferencesProperties.CURRENT_FILE.name(), null);
    
        programImportReferences = userPrefs.getBoolean(MapPreferencesProperties.IMPORT_EXTERNAL_REFS.name(), true);
        externalRefsFileName = userPrefs.get(MapPreferencesProperties.REFERENCE_FILE.name(), null);
    
        userName = userPrefs.get(MapPreferencesProperties.USER_NAME.name(), "user");
        description = userPrefs.get(MapPreferencesProperties.DESCRIPTION.name(),
                                          "independent user modifications");
        emailAddress = userPrefs.get(MapPreferencesProperties.EMAIL_ADDRESS.name(), null);
        website = userPrefs.get(MapPreferencesProperties.WEBSITE.name(), null);
    
        userData =
                new ProviderRecord(userName.substring(0, Math.min(10,userName.length())),
                                   description, userName);
        userData.setEmail(emailAddress);
        userData.setLink(website);
    
        workingDir =
                userPrefs.get(MapPreferencesProperties.WORKING_DIR.name(), System.getProperty("user.dir"));
    
        layoutPrefs = userPrefs.node (MapPreferencesProperties.LAYOUT.name());
        scaleOptions.get(MapScale.SCALE_1).load(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_2).load(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_3).load(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_4).load(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_5).load(layoutPrefs);
    }    
    private void loadPrefsForHexLine(Preferences layoutPrefs,
                                     HexLinePanel line)
    {
        line.setLongOption(layoutPrefs.getBoolean(HexLineProperties.ISLONG.name(), true));
        line.setLongIndex(layoutPrefs.getInt(HexLineProperties.LONG_OPTION.name(), 0));
        line.setShort1Index(layoutPrefs.getInt(HexLineProperties.SHORT_OPTION1.name(), 0));
        line.setShort2Index(layoutPrefs.getInt(HexLineProperties.SHORT_OPTION2.name(), 0));
        line.setShort3Index(layoutPrefs.getInt(HexLineProperties.SHORT_OPTION3.name(), 0));
    }

    public void savePreferences()
    {
        Preferences userPrefs, layoutPrefs;
        userPrefs = Preferences.userNodeForPackage(this.getClass());
        
        userPrefs.putBoolean (MapPreferencesProperties.LOADED.name(),
                              true);
        userPrefs.putBoolean(MapPreferencesProperties.IMPORT_EXTERNAL_REFS.name(),
                             programImportReferences);
        userPrefs.put(MapPreferencesProperties.REFERENCE_FILE.name(), externalRefsFileName);
        if (currentFile == null)
            currentFile = "";
        userPrefs.put(MapPreferencesProperties.CURRENT_FILE.name(), currentFile);

        userPrefs.put(MapPreferencesProperties.USER_NAME.name(), userName);
        userPrefs.put(MapPreferencesProperties.DESCRIPTION.name(), description);
        userPrefs.put(MapPreferencesProperties.EMAIL_ADDRESS.name(), emailAddress);
        userPrefs.put(MapPreferencesProperties.WEBSITE.name(), website);

        userPrefs.put(MapPreferencesProperties.WORKING_DIR.name(), workingDir);
        userPrefs.putInt(MapPreferencesProperties.APP_WIDTH.name(), appWidth);
        userPrefs.putInt(MapPreferencesProperties.APP_HEIGHT.name(), appHeight);
        userPrefs.putInt(MapPreferencesProperties.XPOS.name(), appX);
        userPrefs.putInt(MapPreferencesProperties.YPOS.name(), appY);

        layoutPrefs = userPrefs.node (MapPreferencesProperties.LAYOUT.name());
        scaleOptions.get(MapScale.SCALE_1).save(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_2).save(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_3).save(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_4).save(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_5).save(layoutPrefs);
    }

    public HexLayout getScaleLayout(MapScale level)
    {
        return scaleOptions.get(level);
    }

    public Astrogation getMapData() { return mapData; }

    public void setWorkingDir(String workingDir)
    {
        String oldWorkingDir = workingDir;
        this.workingDir = workingDir;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.WORKING_DIR.name(), 
                                                 oldWorkingDir, workingDir);
    }

    public String getWorkingDir()
    {
        return workingDir;
    }

    public void setCurrentFile(String currentFile)
    {
        String oldCurrentFile = currentFile;
        this.currentFile = currentFile;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.CURRENT_FILE.name(), oldCurrentFile, currentFile);
    }

    public String getCurrentFile()
    {
        return currentFile;
    }

    public void setAppWidth(int appWidth)
    {
        int oldAppWidth = appWidth;
        this.appWidth = appWidth;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.APP_WIDTH.name(), 
                                                 oldAppWidth, appWidth);
    }

    public int getAppWidth()
    {
        return appWidth;
    }

    public void setAppHeight(int appHeight)
    {
        int oldAppHeight = appHeight;
        this.appHeight = appHeight;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.APP_HEIGHT.name(), 
                                                 oldAppHeight, appHeight);
    }

    public int getAppHeight()
    {
        return appHeight;
    }

    public void setAppX(int appX)
    {
        int oldAppX = appX;
        this.appX = appX;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.XPOS.name(), 
                                                 oldAppX, appX);
    }

    public int getAppX()
    {
        return appX;
    }

    public void setAppY(int appY)
    {
        int oldAppY = appY;
        this.appY = appY;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.YPOS.name(), 
                                                 oldAppY, appY);
    }

    public int getAppY()
    {
        return appY;
    }

    public ProviderRecord getUserData()
    {
        return userData;
    }

    public void setUserName(String userName)
    {
        String oldUserName = userName;
        this.userName = userName;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.USER_NAME.name(), 
                                                 oldUserName, userName);
    }

    public String getUserName()
    {
        return userName;
    }

    public void setDescription(String description)
    {
        String oldDescription = description;
        this.description = description;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.DESCRIPTION.name(), 
                                                 oldDescription, description);
    }

    public String getDescription()
    {
        return description;
    }

    public void setEmailAddress(String emailAddress)
    {
        String oldEmailAddress = emailAddress;
        this.emailAddress = emailAddress;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.EMAIL_ADDRESS.name(), 
                                                 oldEmailAddress, emailAddress);
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setWebsite(String website)
    {
        String oldWebsite = website;
        this.website = website;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.WEBSITE.name(), 
                                                 oldWebsite, website);
    }

    public String getWebsite()
    {
        return website;
    }

    public void setProgramImportReferences(boolean programImportReferences)
    {
        boolean oldProgramImportReferences = programImportReferences;
        this.programImportReferences = programImportReferences;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.IMPORT_EXTERNAL_REFS.name(), 
                                                 oldProgramImportReferences, programImportReferences);
    }

    public boolean isProgramImportReferences()
    {
        return programImportReferences;
    }

    public void setExternalRefsFileName(String externalRefsFileName)
    {
        String oldExternalRefsFileName = externalRefsFileName;
        this.externalRefsFileName = externalRefsFileName;
        propertyChangeSupport.firePropertyChange(MapPreferencesProperties.REFERENCE_FILE.name(), 
                                                 oldExternalRefsFileName, externalRefsFileName);
    }

    public String getExternalRefsFileName()
    {
        return externalRefsFileName;
    }

    private void initMapData()
    {
        HexID hex = new HexID(1, 1);
        hex.setHexGroup("EditOptions.1");
        UWP planet = new UWP();
        planet.setPort('B');
        planet.setSize('6');
        planet.setAtmosphere('7');
        planet.setHydrograph('8');
        planet.setPopulation('7');
        planet.setGovernment('4');
        planet.setLawlevel('5');
        planet.setTechnology('A');
        StarSystem one = new StarSystem();
        one.setBase('A');
        one.setBelts(0);
        one.setGiants(1);
        one.setPlanet(planet);
        one.setPolity("Im");
        one.setLocation(hex);
        one.setName("Regina");
        one.setTradeCodes("Ri");
        one.setZone('A');
        mapData = new Astrogation();
        mapData.addSystem(one);

        GroupRecord g = new GroupRecord();
        g.setType(GroupType.SUBSECTOR);
        g.setKey("EditOptions.1");
        g.setExtentX(5);
        g.setExtentY(5);
        g.addSystem(one);
        g.setLocation(hex);
        mapData.addGroup(g);

        g = new GroupRecord();
        g.setType(GroupType.QUADRANT);
        g.setKey("EditOptions.2");
        g.setExtentX(3);
        g.setExtentY(3);
        g.addSystem(one);
        g.setLocation(hex);
        mapData.addGroup(g);

        g = new GroupRecord();
        g.setType(GroupType.SECTOR);
        g.setKey("EditOptions.3");
        g.setExtentX(2);
        g.setExtentY(2);
        g.addSystem(one);
        g.setLocation(hex);
        mapData.addGroup(g);

        g = new GroupRecord();
        g.setType(GroupType.DOMAIN);
        g.setKey("EditOptions.4");
        g.setExtentX(1);
        g.setExtentY(1);
        g.addSystem(one);
        g.setLocation(hex);
        mapData.addGroup(g);
    }

}
