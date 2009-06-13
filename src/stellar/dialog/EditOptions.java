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

import java.beans.PropertyChangeListener;

import java.util.*;

import javax.swing.*;

import stellar.MapPreferences;

/**
 * EditOptions dialog sets the options for the Stellar Cartographer program. 
 * Mostly this for setting the Map options. There are 5 scales, and for each 
 * scale, there is a set of 1 to 5 lines of options that go onto each hex. 
 * 
 * The EditOptions has 5 tabs (one for each scale), that shows the map as it is
 * currently, and allows the user to make changes and see the results in a small 
 * map. 
 * 
 * All of the data is kept in the @see stellar.MapPreferences , where is it 
 * stored in the Properties backing store for the application. 
 * 
 * @author Thomas Jones-Low
 * @version $Id$
 */
public class EditOptions extends JDialog //implements PropertyChangeListener
{
    private static EditOptions _instance;

    private MapPreferences prefs = MapPreferences.getInstance();

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
        //prefs.addPropertyChangeListener(this);
        try
        {
            jbInit();
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
       
        scalePanel.put (MapScale.SCALE_1, new HexOptionPanel(prefs.getScaleLayout(MapScale.SCALE_1).getOptions()));

        scalePanel.get(MapScale.SCALE_1).setHexLayout(MapScale.SCALE_1, prefs.getScaleLayout(MapScale.SCALE_1));
        scalePanel.get(MapScale.SCALE_1).setMapData(prefs.getMapData());
        scalePanel.get(MapScale.SCALE_1).setMapScale(MapScale.SCALE_1);
        scalePanel.get(MapScale.SCALE_1).setMapScope(MapScope.SUBSECTOR);
        
        //scale1Line1 = new HexLinePanel (scaleOptions.get(MapScale.SCALE_1).getLine(1));
        
        scale1HexLines.setLayout(scale1LinesLayout);
        scale1HexLines.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        scale1HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_1).getLine(0)),
                           new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(5, 5, 0, 0), 0,
                                                  0));

        mapScale1.setLayout(scale1CardLayout);
        mapScale1.add(scale1HexLines, BorderLayout.CENTER);
        mapScale1.add(scalePanel.get(MapScale.SCALE_1), BorderLayout.EAST);

        /* MapScale 2 Layout */
        scalePanel.put (MapScale.SCALE_2, new HexOptionPanel (prefs.getScaleLayout(MapScale.SCALE_2).getOptions()));
        scalePanel.get(MapScale.SCALE_2).setHexLayout (MapScale.SCALE_2, prefs.getScaleLayout(MapScale.SCALE_2));
        scalePanel.get(MapScale.SCALE_2).setMapData(prefs.getMapData());
        scalePanel.get(MapScale.SCALE_2).setMapScale (MapScale.SCALE_2);
        scalePanel.get(MapScale.SCALE_2).setMapScope (MapScope.QUADRANT);
        
        //scale2Line1 = new HexLinePanel (scaleOptions.get(MapScale.SCALE_2).getLine(1));
        //scale2Line2 = new HexLinePanel (scaleOptions.get(MapScale.SCALE_2).getLine(2));

        //layout.get(MapScale.SCALE_2).setOptions(scaleOptions.get(MapScale.SCALE_2));

        scale2HexLines.setLayout(scale2LinesLayout);
        scale2HexLines.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        scale2HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_2).getLine(0)),
                           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale2HexLines.add(new HexLinePanel(prefs.getScaleLayout(MapScale.SCALE_2).getLine(1)),
                           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        mapScale2.setLayout(scale2CardLayout);
        mapScale2.add(scalePanel.get(MapScale.SCALE_2), BorderLayout.EAST);

        /* Scale 3 Map Layout */
        scalePanel.put (MapScale.SCALE_3, new HexOptionPanel(prefs.getScaleLayout(MapScale.SCALE_3).getOptions()));
        scalePanel.get(MapScale.SCALE_3).setHexLayout (MapScale.SCALE_3, prefs.getScaleLayout(MapScale.SCALE_3));
        scalePanel.get(MapScale.SCALE_3).setMapData(prefs.getMapData());
        scalePanel.get(MapScale.SCALE_3).setMapScale(MapScale.SCALE_3);
        scalePanel.get(MapScale.SCALE_3).setMapScope(MapScope.SECTOR);

        scale3HexLines.setLayout(scale3LinesLayout);
        scale3HexLines.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        scale3HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_3).getLine(0)),
                           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale3HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_3).getLine(1)),
                           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale3HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_3).getLine(2)),
                           new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));

        mapScale3.setLayout(scale3CardLayout);
        mapScale3.add(scale3HexLines, BorderLayout.CENTER);
        mapScale3.add(scalePanel.get(MapScale.SCALE_3), BorderLayout.EAST);

        /* Scale 4 Map Layout */
        scalePanel.put (MapScale.SCALE_4, new HexOptionPanel (prefs.getScaleLayout(MapScale.SCALE_4).getOptions()));
        scalePanel.get(MapScale.SCALE_4).setHexLayout (MapScale.SCALE_4, prefs.getScaleLayout(MapScale.SCALE_4));
        scalePanel.get(MapScale.SCALE_4).setMapData (prefs.getMapData());
        scalePanel.get(MapScale.SCALE_4).setMapScale (MapScale.SCALE_4);
        scalePanel.get(MapScale.SCALE_4).setMapScope (MapScope.DOMAIN);

        scale4HexLines.setLayout(scale4LinesLayout);
        scale4HexLines.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        mapScale4.setLayout(scale4CardLayout);
        scale4HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_4).getLine(0)),
                           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale4HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_4).getLine(1)),
                           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale4HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_4).getLine(2)),
                           new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale4HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_4).getLine(3)),
                           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        mapScale4.add(scale4HexLines, BorderLayout.CENTER);
        mapScale4.add(scalePanel.get(MapScale.SCALE_4), BorderLayout.EAST);

        /* Scale 5 Map Layout */
        scalePanel.put(MapScale.SCALE_5, new HexOptionPanel (prefs.getScaleLayout(MapScale.SCALE_5).getOptions()));
        scalePanel.get(MapScale.SCALE_5).setHexLayout (MapScale.SCALE_5, prefs.getScaleLayout(MapScale.SCALE_5));
        scalePanel.get(MapScale.SCALE_5).setMapData(prefs.getMapData());
        scalePanel.get(MapScale.SCALE_5).setMapScale (MapScale.SCALE_5);
        scalePanel.get(MapScale.SCALE_5).setMapScope (MapScope.DOMAIN);

        mapScale5.setLayout(scale5CardLayout);

        scale5HexLines.setLayout(scale5LinesLayout);
        scale5HexLines.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        scale5HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_5).getLine(0)),
                           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale5HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_5).getLine(1)),
                           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale5HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_5).getLine(2)),
                           new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale5HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_5).getLine(3)),
                           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                                                  GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(0, 0, 5, 0), 0,
                                                  0));
        scale5HexLines.add(new HexLinePanel (prefs.getScaleLayout(MapScale.SCALE_5).getLine(4)),
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
        prefs.savePreferences();
    }

    private void bCancel_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
    }

    public boolean doImportExternalRefs()
    {
        return programImportReferences.isSelected();
    }


    private void bOpenExternalRefsFile_actionPerformed(ActionEvent e)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open reference data file");
        chooser.setCurrentDirectory(new File(prefs.getWorkingDir()));
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
        output = new File(prefs.getWorkingDir() + File.separator + "prefs.xml");

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
        prefs.restorePreferences(true);
    }
   
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
