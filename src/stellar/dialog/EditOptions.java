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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import javax.swing.JTextField;

import javax.swing.UIManager;

import stellar.MapPreferences;

import stellar.io.filter.XMLFileFilter;

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

    private BorderLayout borderLayout1 = new BorderLayout();

    private BorderLayout borderLayout2 = new BorderLayout();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

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
       
        borderLayout1.setHgap(5);
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
        jTabbedPane1.addTab("Map Scale 5", new HexLayoutPanel (MapScale.SCALE_5));
        jTabbedPane1.addTab("Map Scale 4", new HexLayoutPanel (MapScale.SCALE_4));
        jTabbedPane1.addTab("Map Scale 3", new HexLayoutPanel (MapScale.SCALE_3));
        jTabbedPane1.addTab("Map Scale 2", new HexLayoutPanel (MapScale.SCALE_2));
        jTabbedPane1.addTab("Map Scale 1", new HexLayoutPanel (MapScale.SCALE_1));

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
        JFileChooser chooser = new JFileChooser ();
        chooser.setDialogTitle (Resources.getString ("map.openTitle"));
        chooser.addChoosableFileFilter (new XMLFileFilter ());
        int option = chooser.showOpenDialog (this);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            prefs.exportPreferences(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void bRestorePrefs_actionPerformed(ActionEvent e)
    {
        JFileChooser chooser = new JFileChooser ();
        chooser.setDialogTitle (Resources.getString ("map.openTitle"));
        chooser.addChoosableFileFilter (new XMLFileFilter ());
        int option = chooser.showOpenDialog (this);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                prefs.restorePreferences(chooser.getSelectedFile().getAbsolutePath(), true);
            }
            catch (FileNotFoundException ex)
            {
                JOptionPane.showMessageDialog (this, ex.getMessage (), Resources.getString ("map.NoFileTitle"), 
                                               JOptionPane.ERROR_MESSAGE);
            } 
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog (this, ex.getMessage (), Resources.getString ("map.IOExceptionTitle"), 
                                               JOptionPane.ERROR_MESSAGE);
            }
        }
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
