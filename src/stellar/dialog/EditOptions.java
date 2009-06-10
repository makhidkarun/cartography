package stellar.dialog;

import stellar.data.ProviderRecord;
import stellar.io.Resources;
import stellar.data.GroupRecord;
import stellar.data.Astrogation;
import stellar.data.GroupType;
import stellar.data.UWP;
import stellar.data.StarSystem;
import stellar.data.HexID;


import stellar.map.MapScale;
import stellar.map.MapScope;
import stellar.map.layout.HexLayout;
import stellar.map.layout.HexOptions;

import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Color;

import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.prefs.InvalidPreferencesFormatException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.BevelBorder;
import javax.swing.UnsupportedLookAndFeelException; 
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;

import java.util.*;

import javax.swing.*;

/**
 * TODO: This needs to load the preferences file resources/prefs.xml if the
 * preferences are not found in the Preferences backing store.
 */
public class EditOptions extends JDialog
{
    private static final String IMPORT_EXTERNAL_REFS = "importExternalRefs";
    private static EditOptions _instance;

    /* Application parameter values */
    private String workingDir;
    private String currentFile;
    private Astrogation mapData;
    private int appWidth, appHeight, appX, appY;
    private ProviderRecord userData;

    private JTabbedPane jTabbedPane1 = new JTabbedPane();

    private JPanel program = new JPanel();

    private JPanel programExternal = new JPanel();
    private JCheckBox programImportReferences = new JCheckBox();
    private JTextField externalRefsFileName = new JTextField();
    private JButton bOpenExternalRefsFile = new JButton();

    private ButtonPanel jButtonPanel = new ButtonPanel();
    private JButton bApply = new JButton();
    private JButton bExportPrefs = new JButton();
    private JButton bRestorePrefs = new JButton();

    private JPanel mapScale1 = new JPanel();
    private JPanel mapScale2 = new JPanel();
    private JPanel mapScale3 = new JPanel();
    private JPanel mapScale4 = new JPanel();
    private JPanel mapScale5 = new JPanel();

    private BorderLayout borderLayout1 = new BorderLayout();
    private BorderLayout scale1CardLayout = new BorderLayout();
    private BorderLayout scale2CardLayout = new BorderLayout();
    private BorderLayout scale3CardLayout = new BorderLayout();
    private BorderLayout scale4CardLayout = new BorderLayout();
    private BorderLayout scale5CardLayout = new BorderLayout();

    private Map <MapScale, HexLayout> scaleOptions = new EnumMap <MapScale, HexLayout> (MapScale.class);
    private Map <MapScale, HexOptionPanel> scalePanel= new EnumMap <MapScale, HexOptionPanel> (MapScale.class);
    //private Map <MapScale, DrawHexLayout> layout = new EnumMap <MapScale, DrawHexLayout> (MapScale.class);

    private JPanel scale1HexLines = new JPanel();
    private JPanel scale2HexLines = new JPanel();
    private JPanel scale3HexLines = new JPanel();
    private JPanel scale4HexLines = new JPanel();
    private JPanel scale5HexLines = new JPanel();

    private GridBagLayout scale5LinesLayout = new GridBagLayout();
    private GridBagLayout scale4LinesLayout = new GridBagLayout();
    private GridBagLayout scale3LinesLayout = new GridBagLayout();
    private GridBagLayout scale2LinesLayout = new GridBagLayout();
    private GridBagLayout scale1LinesLayout = new GridBagLayout();

    private BorderLayout borderLayout2 = new BorderLayout();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    //private HexLinePanel scale1Line1; // = new HexLinePanel();

    //private HexLinePanel scale2Line2; // = new HexLinePanel();
    //private HexLinePanel scale2Line1; // = new HexLinePanel();

    //private HexLinePanel scale3Line1; // = new HexLinePanel();
    //private HexLinePanel scale3Line2; // = new HexLinePanel();
    //private HexLinePanel scale3Line3; // = new HexLinePanel();

    //private HexLinePanel scale4Line1; // = new HexLinePanel();
    //private HexLinePanel scale4Line2; // = new HexLinePanel();
    //private HexLinePanel scale4Line3; // = new HexLinePanel();
    //private HexLinePanel scale4Line4; // = new HexLinePanel();

    //private HexLinePanel scale5Line1; // = new HexLinePanel();
    //private HexLinePanel scale5Line2; // = new HexLinePanel();
    //private HexLinePanel scale5Line3; // = new HexLinePanel();
    //private HexLinePanel scale5Line4; // = new HexLinePanel();
    //private HexLinePanel scale5Line5; // = new HexLinePanel();
    private JLabel jLabel1 = new JLabel();
    private JTextField userName = new JTextField();
    private JLabel jLabel2 = new JLabel();
    private JTextField description = new JTextField();
    private JLabel jLabel3 = new JLabel();
    private JTextField emailAddress = new JTextField();
    private JLabel jLabel4 = new JLabel();
    private JTextField website = new JTextField();

    private GenerateOptionsPanel2 generateOptionsPanel1 =
        new GenerateOptionsPanel2();


    public EditOptions()
    {
        this(null, "Edit Options", true);
    }

    public EditOptions(Frame parent, String title, boolean modal)
    {
        super(parent, title, modal);
        try
        {
            initMapData();
            jbInit();
            loadPreferences();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static synchronized EditOptions getInstance()
    {
        if (_instance == null)
            _instance = new EditOptions();
        return _instance;
    }

    private class ScaleActionListener implements ActionListener
    {
        MapScale scale;
        public ScaleActionListener (MapScale scale) {this.scale = scale; }
        public void actionPerformed (ActionEvent e)
        {
            scalePanel.get(scale).redrawMap();            
        }
    }

    private class OKActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            bOK_actionPerformed(e);
        }
    }

    private class CancelActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            bCancel_actionPerformed(e);
        }
    }

    private void jbInit() throws Exception
    {
        ScaleActionListener l;
        
        //layout.put (MapScale.SCALE_1, new DrawHexLayout (1, MapScale.SCALE_1));
        //layout.put (MapScale.SCALE_2, new DrawHexLayout (2, MapScale.SCALE_2));
        //layout.put (MapScale.SCALE_3, new DrawHexLayout (3, MapScale.SCALE_3));
        //layout.put (MapScale.SCALE_4, new DrawHexLayout (4, MapScale.SCALE_4));
        //layout.put (MapScale.SCALE_5, new DrawHexLayout (5, MapScale.SCALE_5));

        borderLayout1.setHgap(5);
        scale1CardLayout.setHgap(5);
        scale1CardLayout.setVgap(5);
        scale2CardLayout.setHgap(5);
        scale3CardLayout.setHgap(5);
        scale4CardLayout.setHgap(5);
        scale5CardLayout.setHgap(5);
        borderLayout2.setHgap(5);

        this.getContentPane().setLayout(borderLayout1);
        this.setModal(true);
        this.setTitle("Edit Options");


        /* Buttons */
        jButtonPanel.addOKActionListener(new OKActionListener());
        jButtonPanel.addCancelActionListener(new CancelActionListener());

        bApply.setText("Apply");
        bApply.setEnabled(false);

        bExportPrefs.setText("Export");
        bExportPrefs.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bExportPrefs_actionPerformed(e);
                }
            });

        bRestorePrefs.setText("Restore");
        bRestorePrefs.setToolTipText("Restore preferences");
        bRestorePrefs.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bRestorePrefs_actionPerformed(e);
                }
            });

        jButtonPanel.add(bRestorePrefs);
        jButtonPanel.add(bExportPrefs);
        jButtonPanel.add(bApply);
        //jButtonPanel.add(bApply, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
        //jButtonPanel.add(bExportPrefs, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
        //jButtonPanel.add(bRestorePrefs, new GridBagConstraints(0, 4, 1, 1, 0.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));

        /* Map Scale 1 Layout */
        scaleOptions.put (MapScale.SCALE_1, new HexLayout(1, MapScale.SCALE_1));
        
        scalePanel.put (MapScale.SCALE_1, new HexOptionPanel(scaleOptions.get(MapScale.SCALE_1).getOptions()));
        scalePanel.get(MapScale.SCALE_1).setHexLayout(MapScale.SCALE_1, scaleOptions.get(MapScale.SCALE_1));
        scalePanel.get(MapScale.SCALE_1).setMapData(mapData);
        scalePanel.get(MapScale.SCALE_1).setMapScale(MapScale.SCALE_1);
        scalePanel.get(MapScale.SCALE_1).setMapScope(MapScope.SUBSECTOR);
        
        //scale1Line1 = new HexLinePanel (scaleOptions.get(MapScale.SCALE_1).getLine(1));
        
        scale1HexLines.setLayout(scale1LinesLayout);
        scale1HexLines.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        scale1HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_1).getLine(0)),
                           new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(5, 5, 0, 0), 0,
                                                  0));

        mapScale1.setLayout(scale1CardLayout);
        mapScale1.add(scale1HexLines, BorderLayout.CENTER);
        mapScale1.add(scalePanel.get(MapScale.SCALE_1), BorderLayout.EAST);

        /* MapScale 2 Layout */
        scaleOptions.put(MapScale.SCALE_2, new HexLayout(2, MapScale.SCALE_2));
        
        scalePanel.put (MapScale.SCALE_2, new HexOptionPanel (scaleOptions.get(MapScale.SCALE_2).getOptions()));
        scalePanel.get(MapScale.SCALE_2).setHexLayout (MapScale.SCALE_2, scaleOptions.get(MapScale.SCALE_2));
        scalePanel.get(MapScale.SCALE_2).setMapData(mapData);
        scalePanel.get(MapScale.SCALE_2).setMapScale (MapScale.SCALE_2);
        scalePanel.get(MapScale.SCALE_2).setMapScope (MapScope.QUADRANT);
        
        //scale2Line1 = new HexLinePanel (scaleOptions.get(MapScale.SCALE_2).getLine(1));
        //scale2Line2 = new HexLinePanel (scaleOptions.get(MapScale.SCALE_2).getLine(2));

        //layout.get(MapScale.SCALE_2).setOptions(scaleOptions.get(MapScale.SCALE_2));

        scale2HexLines.setLayout(scale2LinesLayout);
        scale2HexLines.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        scale2HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_2).getLine(0)),
                           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale2HexLines.add(new HexLinePanel(scaleOptions.get(MapScale.SCALE_2).getLine(1)),
                           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        mapScale2.setLayout(scale2CardLayout);
        mapScale2.add(scalePanel.get(MapScale.SCALE_2), BorderLayout.EAST);

        /* Scale 3 Map Layout */
        scaleOptions.put (MapScale.SCALE_3, new HexLayout(3, MapScale.SCALE_3));
        scalePanel.put (MapScale.SCALE_3, new HexOptionPanel(scaleOptions.get (MapScale.SCALE_3).getOptions()));
        scalePanel.get(MapScale.SCALE_3).setHexLayout (MapScale.SCALE_3, scaleOptions.get(MapScale.SCALE_3));
        scalePanel.get(MapScale.SCALE_3).setMapData(mapData);
        scalePanel.get(MapScale.SCALE_3).setMapScale(MapScale.SCALE_3);
        scalePanel.get(MapScale.SCALE_3).setMapScope(MapScope.SECTOR);

        scale3HexLines.setLayout(scale3LinesLayout);
        scale3HexLines.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        scale3HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_3).getLine(0)),
                           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale3HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_3).getLine(1)),
                           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale3HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_3).getLine(2)),
                           new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));

        mapScale3.setLayout(scale3CardLayout);
        mapScale3.add(scale3HexLines, BorderLayout.CENTER);
        mapScale3.add(scalePanel.get(MapScale.SCALE_3), BorderLayout.EAST);

        /* Scale 4 Map Layout */
        scaleOptions.put (MapScale.SCALE_4, new HexLayout(4, MapScale.SCALE_4));
        scalePanel.put (MapScale.SCALE_4, new HexOptionPanel (scaleOptions.get(MapScale.SCALE_4).getOptions()));
        scalePanel.get(MapScale.SCALE_4).setHexLayout (MapScale.SCALE_4, scaleOptions.get(MapScale.SCALE_4));
        scalePanel.get(MapScale.SCALE_4).setMapData (mapData);
        scalePanel.get(MapScale.SCALE_4).setMapScale (MapScale.SCALE_4);
        scalePanel.get(MapScale.SCALE_4).setMapScope (MapScope.DOMAIN);

        scale4HexLines.setLayout(scale4LinesLayout);
        scale4HexLines.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        mapScale4.setLayout(scale4CardLayout);
        scale4HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_4).getLine(0)),
                           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale4HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_4).getLine(1)),
                           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale4HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_4).getLine(2)),
                           new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale4HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_4).getLine(3)),
                           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        mapScale4.add(scale4HexLines, BorderLayout.CENTER);
        mapScale4.add(scalePanel.get(MapScale.SCALE_4), BorderLayout.EAST);

        /* Scale 5 Map Layout */
        scaleOptions.put (MapScale.SCALE_5, new HexLayout(5, MapScale.SCALE_5));
        scalePanel.put(MapScale.SCALE_5, new HexOptionPanel (scaleOptions.get(MapScale.SCALE_5).getOptions()));
        scalePanel.get(MapScale.SCALE_5).setHexLayout (MapScale.SCALE_5, scaleOptions.get(MapScale.SCALE_5));
        scalePanel.get(MapScale.SCALE_5).setMapData(mapData);
        scalePanel.get(MapScale.SCALE_5).setMapScale (MapScale.SCALE_5);
        scalePanel.get(MapScale.SCALE_5).setMapScope (MapScope.DOMAIN);

        mapScale5.setLayout(scale5CardLayout);

        scale5HexLines.setLayout(scale5LinesLayout);
        scale5HexLines.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        scale5HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_5).getLine(0)),
                           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale5HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_5).getLine(1)),
                           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale5HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_5).getLine(2)),
                           new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale5HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_5).getLine(3)),
                           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale5HexLines.add(new HexLinePanel (scaleOptions.get(MapScale.SCALE_5).getLine(4)),
                           new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));

        mapScale5.add(scalePanel.get(MapScale.SCALE_5), BorderLayout.EAST);
        mapScale5.add(scale5HexLines, BorderLayout.CENTER);

        scale4HexLines.setSize(scale5HexLines.getSize());
        scale3HexLines.setSize(scale5HexLines.getSize());
        scale2HexLines.setSize(scale5HexLines.getSize());
        scale1HexLines.setSize(scale5HexLines.getSize());

        /* Program  Tab*/
        program.setLayout(gridBagLayout1);
        programImportReferences.setText("Import External References");
        programImportReferences.setSelected(true);
        programImportReferences.setToolTipText("Import already defined references on startup");
        externalRefsFileName.setColumns(20);

        jLabel1.setText("User Name");
        userName.setText("userName");
        userName.setColumns(25);
        userName.setToolTipText("Your name, used to identify any new maps and data created");
        jLabel2.setText("Description");
        description.setText("Description");
        description.setColumns(25);
        description.setToolTipText("Your general description");
        jLabel3.setText("Email Address");
        emailAddress.setText("Email Address");
        emailAddress.setColumns(25);
        emailAddress.setToolTipText("Enter your email address");
        jLabel4.setText("Website");
        website.setText("http://www.website.com");
        website.setColumns(25);
        website.setToolTipText("Enter a website where this data will be published");

        bOpenExternalRefsFile.setText("...");
        bOpenExternalRefsFile.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bOpenExternalRefsFile_actionPerformed(e);
                }
            });

        programExternal.setLayout(borderLayout2);

        programExternal.add(programImportReferences, BorderLayout.WEST);
        programExternal.add(externalRefsFileName, BorderLayout.CENTER);
        programExternal.add(bOpenExternalRefsFile, BorderLayout.EAST);

        program.add(programExternal,
                    new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.NONE,
                                           new Insets(5, 0, 5, 0), 0, 0));
        program.add(jLabel1,
                    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 0, 5), 0, 0));
        program.add(userName,
                    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 5, 0), 0, 0));
        program.add(jLabel2,
                    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 0, 5), 0, 0));
        program.add(description,
                    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 5, 0), 0, 0));
        program.add(jLabel3,
                    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 5, 0, 5), 0, 0));
        program.add(emailAddress,
                    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 5, 0), 0, 0));
        program.add(jLabel4,
                    new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 5, 0, 5), 0, 0));
        program.add(website,
                    new GridBagConstraints(1, 4, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST,
                                           GridBagConstraints.NONE,
                                           new Insets(0, 0, 5, 0), 0, 0));

        /* Tabbed Panel */
        jTabbedPane1.addTab("Program", program);
        jTabbedPane1.addTab("Generate", generateOptionsPanel1);
        jTabbedPane1.addTab("Map Scale 5", mapScale5);
        jTabbedPane1.addTab("Map Scale 4", mapScale4);
        jTabbedPane1.addTab("Map Scale 3", mapScale3);
        mapScale2.add(scale2HexLines, BorderLayout.CENTER);
        jTabbedPane1.addTab("mapScale2", mapScale2);
        jTabbedPane1.addTab("Map Scale 1", mapScale1);

        this.getContentPane().add(jButtonPanel, BorderLayout.SOUTH);
        this.getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
        this.pack();
    }

    private void bOK_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
        savePreferences();
    }

    private void bCancel_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
    }

    public boolean doImportExternalRefs()
    {
        return programImportReferences.isSelected();
    }

    public String getWorkingDirName()
    {
        return workingDir;
    }

    public void setWorkingDirName(String dir)
    {
        workingDir = dir;
    }

    public String getCurrentFileName()
    {
        return currentFile;
    }

    public void setCurrentFileName(String file)
    {
        currentFile = file;
    }

    public HexLayout getScaleLayout(MapScale level)
    {
        return scaleOptions.get(level);
    }

    private void loadPreferences()
    {
        Preferences userPrefs, layoutPrefs;

        userPrefs = Preferences.userNodeForPackage(this.getClass());

        appWidth = userPrefs.getInt("width", 200);
        appHeight = userPrefs.getInt("height", 300);
        appX = userPrefs.getInt("xPos", 80);
        appY = userPrefs.getInt("yPos", 80);
        currentFile = userPrefs.get("currentFile", null);

        programImportReferences.setSelected(userPrefs.getBoolean(IMPORT_EXTERNAL_REFS,
                                                                 true));
        externalRefsFileName.setText(userPrefs.get("referenceFile", null));

        userName.setText(userPrefs.get("userName", "user"));
        description.setText(userPrefs.get("description",
                                          "independent user modifications"));
        emailAddress.setText(userPrefs.get("emailAddress", null));
        website.setText(userPrefs.get("website", null));

        userData =
                new ProviderRecord(userName.getText().substring(0, Math.min(10,
                                                                            userName.getText().length())),
                                   description.getText(), userName.getText());
        userData.setEmail(emailAddress.getText());
        userData.setLink(website.getText());

        workingDir =
                userPrefs.get("workingDir", System.getProperty("user.dir"));

        layoutPrefs = userPrefs.node ("layout");
        scaleOptions.get(MapScale.SCALE_1).load(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_2).load(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_3).load(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_4).load(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_5).load(layoutPrefs);
/*        
        layoutPrefs = userPrefs.node("layout/scale1");
        scaleOptions.get(MapScale.SCALE_1).load(layoutPrefs);

        layoutPrefs = userPrefs.node("layout/scale1/line1");
        loadPrefsForHexLine(layoutPrefs, scale1Line1);

        layoutPrefs = userPrefs.node("layout/scale2");
        scaleOptions.get(MapScale.SCALE_2).load(layoutPrefs);
 
        layoutPrefs = userPrefs.node("layout/scale2/line1");
        loadPrefsForHexLine(layoutPrefs, scale2Line1);

        layoutPrefs = userPrefs.node("layout/scale2/line2");
        loadPrefsForHexLine(layoutPrefs, scale2Line2);

        layoutPrefs = userPrefs.node("layout/scale3");
        scaleOptions.get(MapScale.SCALE_3).load(layoutPrefs);
 
        layoutPrefs = userPrefs.node("layout/scale3/line1");
        loadPrefsForHexLine(layoutPrefs, scale3Line1);

        layoutPrefs = userPrefs.node("layout/scale3/line2");
        loadPrefsForHexLine(layoutPrefs, scale3Line2);

        layoutPrefs = userPrefs.node("layout/scale3/line3");
        loadPrefsForHexLine(layoutPrefs, scale3Line3);

        layoutPrefs = userPrefs.node("layout/scale4");
        scaleOptions.get(MapScale.SCALE_4).load(layoutPrefs);

        layoutPrefs = userPrefs.node("layout/scale4/line1");
        loadPrefsForHexLine(layoutPrefs, scale4Line1);

        layoutPrefs = userPrefs.node("layout/scale4/line2");
        loadPrefsForHexLine(layoutPrefs, scale4Line2);

        layoutPrefs = userPrefs.node("layout/scale4/line3");
        loadPrefsForHexLine(layoutPrefs, scale4Line3);

        layoutPrefs = userPrefs.node("layout/scale4/line4");
        loadPrefsForHexLine(layoutPrefs, scale4Line4);

        layoutPrefs = userPrefs.node("layout/scale5");
        scaleOptions.get(MapScale.SCALE_5).load(layoutPrefs);

        layoutPrefs = userPrefs.node("layout/scale5/line1");
        loadPrefsForHexLine(layoutPrefs, scale5Line1);

        layoutPrefs = userPrefs.node("layout/scale5/line2");
        loadPrefsForHexLine(layoutPrefs, scale5Line2);

        layoutPrefs = userPrefs.node("layout/scale5/line3");
        loadPrefsForHexLine(layoutPrefs, scale5Line3);

        layoutPrefs = userPrefs.node("layout/scale5/line4");
        loadPrefsForHexLine(layoutPrefs, scale5Line4);

        layoutPrefs = userPrefs.node("layout/scale5/line5");
        loadPrefsForHexLine(layoutPrefs, scale5Line5);
*/
    }

    private void loadPrefsForHexLine(Preferences layoutPrefs,
                                     HexLinePanel line)
    {
        line.setLongOption(layoutPrefs.getBoolean("isLong", true));
        line.setLongIndex(layoutPrefs.getInt("longOption", 0));
        line.setShort1Index(layoutPrefs.getInt("shortOption1", 0));
        line.setShort2Index(layoutPrefs.getInt("shortOption2", 0));
        line.setShort3Index(layoutPrefs.getInt("shortOption3", 0));
    }

    public void savePreferences()
    {
        Preferences userPrefs, layoutPrefs;
        userPrefs = Preferences.userNodeForPackage(this.getClass());
        userPrefs.putBoolean(IMPORT_EXTERNAL_REFS,
                             programImportReferences.isSelected());
        userPrefs.put("referenceFile", externalRefsFileName.getText());
        if (currentFile == null)
            currentFile = "";
        userPrefs.put("currentFile", currentFile);

        userPrefs.put("userName", userName.getText());
        userPrefs.put("description", description.getText());
        userPrefs.put("emailAddress", emailAddress.getText());
        userPrefs.put("website", website.getText());

        userPrefs.put("workingDir", workingDir);
        userPrefs.putInt("width", appWidth);
        userPrefs.putInt("height", appHeight);
        userPrefs.putInt("xPos", appX);
        userPrefs.putInt("yPos", appY);

        layoutPrefs = userPrefs.node ("layout");
        scaleOptions.get(MapScale.SCALE_1).save(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_2).save(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_3).save(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_4).save(layoutPrefs);
        scaleOptions.get(MapScale.SCALE_5).save(layoutPrefs);
/*        
        layoutPrefs = userPrefs.node("layout/scale1");
        scaleOptions.get(MapScale.SCALE_1).save(layoutPrefs);

        layoutPrefs = userPrefs.node("layout/scale1/line1");
        savePrefsForHexLine(layoutPrefs, scale1Line1);

        layoutPrefs = userPrefs.node("layout/scale2");
        scaleOptions.get(MapScale.SCALE_2).save(layoutPrefs);

        layoutPrefs = userPrefs.node("layout/scale2/line1");
        savePrefsForHexLine(layoutPrefs, scale2Line1);

        layoutPrefs = userPrefs.node("layout/scale2/line2");
        savePrefsForHexLine(layoutPrefs, scale2Line2);

        layoutPrefs = userPrefs.node("layout/scale3");
        scaleOptions.get(MapScale.SCALE_3).save(layoutPrefs);

        layoutPrefs = userPrefs.node("layout/scale3/line1");
        savePrefsForHexLine(layoutPrefs, scale3Line1);

        layoutPrefs = userPrefs.node("layout/scale3/line2");
        savePrefsForHexLine(layoutPrefs, scale3Line2);

        layoutPrefs = userPrefs.node("layout/scale3/line3");
        savePrefsForHexLine(layoutPrefs, scale3Line3);

        layoutPrefs = userPrefs.node("layout/scale4");
        scaleOptions.get(MapScale.SCALE_4).save(layoutPrefs);

        layoutPrefs = userPrefs.node("layout/scale4/line1");
        savePrefsForHexLine(layoutPrefs, scale4Line1);

        layoutPrefs = userPrefs.node("layout/scale4/line2");
        savePrefsForHexLine(layoutPrefs, scale4Line2);

        layoutPrefs = userPrefs.node("layout/scale4/line3");
        savePrefsForHexLine(layoutPrefs, scale4Line3);

        layoutPrefs = userPrefs.node("layout/scale4/line4");
        savePrefsForHexLine(layoutPrefs, scale4Line4);

        layoutPrefs = userPrefs.node("layout/scale5");
        scaleOptions.get(MapScale.SCALE_5).save(layoutPrefs);

        layoutPrefs = userPrefs.node("layout/scale5/line1");
        savePrefsForHexLine(layoutPrefs, scale5Line1);

        layoutPrefs = userPrefs.node("layout/scale5/line2");
        savePrefsForHexLine(layoutPrefs, scale5Line2);

        layoutPrefs = userPrefs.node("layout/scale5/line3");
        savePrefsForHexLine(layoutPrefs, scale5Line3);

        layoutPrefs = userPrefs.node("layout/scale5/line4");
        savePrefsForHexLine(layoutPrefs, scale5Line4);

        layoutPrefs = userPrefs.node("layout/scale5/line5");
        savePrefsForHexLine(layoutPrefs, scale5Line5);
*/
    }

    private void savePrefsForHexLine(Preferences layoutPrefs,
                                     HexLinePanel line)
    {
        layoutPrefs.putBoolean("isLong", line.isLongItem());
        layoutPrefs.putInt("longOption", line.getLongItem().ordinal());
        layoutPrefs.putInt("shortOption1", line.getShortItem1().ordinal());
        layoutPrefs.putInt("shortOption2", line.getShortItem2().ordinal());
        layoutPrefs.putInt("shortOption3", line.getShortItem3().ordinal());
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

    private void bOpenExternalRefsFile_actionPerformed(ActionEvent e)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open reference data file");
        chooser.setCurrentDirectory(new File(workingDir));
        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            externalRefsFileName.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void bExportPrefs_actionPerformed(ActionEvent e)
    {
        Preferences userPrefs;
        File output;
        FileOutputStream os;

        userPrefs = Preferences.userNodeForPackage(this.getClass());
        output = new File(workingDir + File.separator + "prefs.xml");

        try
        {
            os = new FileOutputStream(output);
            userPrefs = Preferences.userRoot();
            userPrefs.exportSubtree(os);
            os.close();
        } catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        } catch (IOException ex)
        {
        } catch (BackingStoreException ex)
        {
            ex.printStackTrace();
        }
    }

    private void bRestorePrefs_actionPerformed(ActionEvent e)
    {
        Preferences userPrefs;
        InputStream is;

        is = Resources.getPrefrences();
        userPrefs = Preferences.userNodeForPackage(this.getClass());

        try
        {
            userPrefs.removeNode();
            Preferences.importPreferences(is);
            is.close();
            userPrefs.flush();
        } catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        } catch (IOException ex)
        {
        } catch (BackingStoreException ex)
        {
            ex.printStackTrace();
        } catch (InvalidPreferencesFormatException ex)
        {
            System.out.println(ex.getMessage());
        }
        loadPreferences();
    }

    public int getAppHeight()
    {
        return appHeight;
    }

    public void setAppHeight(int appHeight)
    {
        this.appHeight = appHeight;
    }

    public int getAppWidth()
    {
        return appWidth;
    }

    public void setAppWidth(int appWidth)
    {
        this.appWidth = appWidth;
    }

    public int getAppX()
    {
        return appX;
    }

    public void setAppX(int appX)
    {
        this.appX = appX;
    }

    public int getAppY()
    {
        return appY;
    }

    public void setAppY(int appY)
    {
        this.appY = appY;
    }

    public ProviderRecord getUserProvider()
    {
        return userData;
    }

    public String getExternalRefsFileName()
    {
        return externalRefsFileName.getText();
    }

    public Astrogation getMapData() { return mapData; }
    
    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex)
        {
            ;
        } 
        catch (InstantiationException ex) { ; } 
        catch (IllegalAccessException ex) {;}
        catch (UnsupportedLookAndFeelException ex) {;}
        EditOptions.getInstance().setVisible(true);
        System.exit (0);
    }

}
