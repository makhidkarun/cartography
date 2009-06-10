package stellar.dialog;
import stellar.data.Astrogation;
import stellar.swing.AstrogationChangeListener;
import stellar.swing.HTMLEditPanel;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.SpinnerNumberModel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JTabbedPane;
import java.util.ListIterator;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import stellar.data.StarSystem;
import stellar.data.UWP;
import stellar.data.Star;
import stellar.data.References;
import stellar.data.TableRecord;
import stellar.data.TableRecordKey;
import stellar.data.TableRowRecord;
import stellar.swing.JComboTable;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;

import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.event.ChangeEvent;

/**
 * 
 */
public class EditSystem extends JDialog implements AstrogationChangeListener
{
    private static EditSystem _instance;
    
    private StarSystem data;
    private UWP system; 
    private boolean duplicate = false;

    /* This panel componenets */
    private JTabbedPane jTabbedPane = new JTabbedPane();

    /* Hex location panel */
    private JPanel location = new JPanel();
    /* Location panel components */
    private JComboTable groupName = new JComboTable();
    private JTextField hexLocation = new JTextField();
    private JLabel jLabel13 = new JLabel(); /* Sector/Subsector */
    private JLabel jLabel14 = new JLabel(); /* Hex location */
    private JTextField worldName = new JTextField();
    private JLabel jLabel9 = new JLabel(); /* Name text box header */

    /* Button panel */
    private ButtonPanel buttonPanel = new ButtonPanel();
    //private JButton bOK = new JButton();
    //private JButton bCancel = new JButton();
    private JButton bDuplicate = new JButton();

    /* Universal World Profile panel */
    private JPanel jUWP = new JPanel();
    private UWPActionListener uwpListener = new UWPActionListener();

    /* JPanel jUWP components */    
    private JComboTable starport = new JComboTable();
    private JComboTable worldSize = new JComboTable();
    private JComboTable atmosphere = new JComboTable();
    private JComboTable hydrographic = new JComboTable();
    private JComboTable population = new JComboTable();
    private JComboTable government = new JComboTable();
    private JComboTable lawLevel = new JComboTable();    
    private JComboTable techLevel = new JComboTable();    

    private JLabel jLabel1 = new JLabel(); /* starport */
    private JLabel jLabel2 = new JLabel(); /* Size */
    private JLabel jLabel3 = new JLabel(); /* Atmosphere */
    private JLabel jLabel4 = new JLabel(); /* Hydrographic */
    private JLabel jLabel5 = new JLabel(); /* Population */
    private JLabel jLabel6 = new JLabel(); /* Government */
    private JLabel jLabel7 = new JLabel(); /* Law Level */
    private JLabel jLabel8 = new JLabel(); /* Tech Level */

    /* Contents panel */
    private JPanel contents = new JPanel();
    private JComboTable bases = new JComboTable();
    private JComboTable travelZone = new JComboTable();
    private JComboTable allegiance = new JComboTable();
    private JSpinner popMul = new JSpinner(new SpinnerNumberModel(0,0,9,1));
    private JSpinner belts = new  JSpinner(new SpinnerNumberModel(0,0,6,1));
    private JSpinner giants = new JSpinner(new SpinnerNumberModel(0,0,6,1));

    private JLabel jLabel16 = new JLabel(); /* Bases */
    private JLabel jLabel17 = new JLabel(); /* Travel Zone */
    private JLabel jLabel18 = new JLabel(); /* Allegiance */
    private JLabel jLabel19 = new JLabel(); /* Pop Mul */
    private JLabel jLabel20 = new JLabel(); /* Belts */
    private JLabel jLabel21 = new JLabel(); /* Giants */

    private JLabel jLabel36 = new JLabel();
    private JTextArea tradeCodes = new JTextArea();
    private JLabel jLabel24 = new JLabel(); /* Remark 1*/
    private JComboTable remark1 = new JComboTable();
    private JLabel jLabel25 = new JLabel(); /* Remark 2 */
    private JComboTable remark2 = new JComboTable();
    private JLabel jLabel37 = new JLabel(); /* Remark 3 */
    private JComboTable remark3 = new JComboTable();
    private JLabel jLabel38 = new JLabel(); /* Remark 4 */
    private JComboTable remark4 = new JComboTable();
    private JLabel jLabel10 = new JLabel(); /* survey Date */
    private JTextField surveyDate = new JTextField(8);

    /* Stellar Details panel */
    private JCheckBox primary = new JCheckBox();
    private JPanel stellarDetails = new JPanel();
    private JComboBox primarySpectrum = new JComboBox();
    private JSpinner primaryModifier = new JSpinner(new SpinnerNumberModel(0,0,9,1));
    private JComboTable primaryLuminosity = new JComboTable();
    private JComboBox companionSpectrum = new JComboBox();
    private JSpinner companionModifier = new JSpinner(new SpinnerNumberModel(0,0,9,1));
    private JComboTable companionLuminosity = new JComboTable();
    private JComboBox tertiarySpectrum = new JComboBox();
    private JSpinner tertiaryModifier = new JSpinner (new SpinnerNumberModel(0,0,9,1));
    private JComboTable tertiaryLuminosity = new JComboTable();
    private JCheckBox companion = new JCheckBox();
    private JCheckBox tertiary = new JCheckBox();

    private JLabel jLabel26 = new JLabel();
    private JLabel jLabel27 = new JLabel();
    private JLabel jLabel28 = new JLabel();
    private JLabel jLabel29 = new JLabel();
    private JLabel jLabel30 = new JLabel();
    private JLabel jLabel31 = new JLabel();
    private JLabel jLabel32 = new JLabel();
    private JLabel jLabel33 = new JLabel();
    private JLabel jLabel34 = new JLabel();

    /* Remarks panel */
    private HTMLEditPanel commentPanel = new HTMLEditPanel();
    //private JPanel commentPanel = new JPanel();
    //private JEditorPane comments = new JEditorPane();
    private BoxLayout locationLayout;
    private BoxLayout uwpLayout;
    private GridBagLayout gridBagLayout3 = new GridBagLayout();
    private GridBagLayout gridBagLayout6 = new GridBagLayout();
    private BorderLayout borderLayout2 = new BorderLayout();
    
    public EditSystem()
    {
        this(null, "", true);
    }

    public EditSystem(Frame parent, String title, boolean modal)
    {
        super(parent, title, modal);
        try
        {
            jbInit();
            loadReference(null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public static synchronized EditSystem getInstance()
    {
        if (_instance == null) _instance = new EditSystem ();
        return _instance;
    }
    private class OKActionListener implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
            bOK_actionPerformed(e);
        }
    }
    
    private class CancelActionListener implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
            bCancel_actionPerformed(e);
        }
    }
    private class DuplicateActionListener implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
            bDuplicate_actionPerformed(e);
        }
    }
    
    private void jbInit() throws Exception
    {
        /* Setup component on this pane */
        borderLayout2.setHgap(5);
        this.getContentPane().setLayout(borderLayout2);
        this.setTitle("Edit System");

        bDuplicate.setText("Duplicate");
        bDuplicate.addActionListener(new DuplicateActionListener());
        buttonPanel.add(bDuplicate);
        buttonPanel.addOKActionListener(new OKActionListener());
        buttonPanel.addCancelActionListener(new CancelActionListener());
        
        /* Setup the location pane */
        /* Add to location panel */
        locationLayout = new BoxLayout (location, BoxLayout.LINE_AXIS);
        location.setLayout(locationLayout);
        location.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        
        jLabel9.setText("Name");
        jLabel9.setLabelFor (worldName);

        hexLocation.setText("0000");
        hexLocation.setHorizontalAlignment(JTextField.TRAILING);
        
        //hexLocation.setPreferredSize(getTextBoxSize("000000"));
        hexLocation.setColumns(6);
        hexLocation.setMinimumSize(hexLocation.getPreferredSize());
        
        jLabel14.setText("Location");
        jLabel14.setLabelFor(hexLocation);

        jLabel13.setText("Group");
        jLabel13.setLabelFor(groupName);
        location.add (jLabel9, null);
        location.add (Box.createRigidArea(new Dimension (5,0)),null);
        location.add (worldName, null);
        location.add (Box.createRigidArea(new Dimension (10,0)), null);
        location.add (jLabel14, null);
        location.add (Box.createRigidArea (new Dimension (5,0)), null);
        location.add (hexLocation, null);
        location.add (Box.createRigidArea( new Dimension (10,0)), null);
        location.add (jLabel13, null);
        location.add (Box.createRigidArea (new Dimension (5,0)), null);
        location.add (groupName, null);
        location.add (Box.createHorizontalGlue(), null);        
        /*
        location.add(jLabel9, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 5, 0), 0, 0));
        location.add(worldName, new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 0), 0, 0));
        location.add(jLabel14, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 5, 0), 0, 0));
        location.add(hexLocation, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 0), 0, 0));
        location.add(jLabel13, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
        location.add(groupName, new GridBagConstraints(5, 0, 1, 1, 0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 0), 0, 0));
        */
        /* Add to jUWP panel */
        /* Setup the jUWP pane */
        uwpLayout = new BoxLayout (jUWP, BoxLayout.PAGE_AXIS);
        jUWP.setLayout(uwpLayout);
        jUWP.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        /* Setup components on the jWUP pane */
        jLabel1.setText("Starport");
        jLabel1.setLabelFor(starport);
        jLabel2.setText("Size");
        jLabel2.setLabelFor(worldSize);
        jLabel3.setText("Atmosphere");
        jLabel3.setLabelFor(atmosphere);
        jLabel4.setText("Hydrographic");
        jLabel4.setLabelFor(hydrographic);
        jLabel5.setText("Population");
        jLabel5.setLabelFor(population);
        jLabel6.setText("Government");
        jLabel6.setLabelFor(government);
        jLabel7.setText("Law Level");
        jLabel7.setLabelFor(lawLevel);
        jLabel8.setText("Technology Level");
        jLabel8.setLabelFor(techLevel);
        starport.setName("starport");
        worldSize.setName("worldSize");
        atmosphere.setName("atmosphere");
        hydrographic.setName("hydrographic");
        population.setName("population");
        government.setName("government");
        lawLevel.setName("lawLevel");
        techLevel.setName("techLevel");


        jLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel3.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel4.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel5.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel6.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel7.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel8.setAlignmentX(Component.LEFT_ALIGNMENT);
        starport.setAlignmentX(Component.LEFT_ALIGNMENT);
        worldSize.setAlignmentX(Component.LEFT_ALIGNMENT);
        atmosphere.setAlignmentX(Component.LEFT_ALIGNMENT);
        hydrographic.setAlignmentX(Component.LEFT_ALIGNMENT);
        population.setAlignmentX(Component.LEFT_ALIGNMENT);
        government.setAlignmentX(Component.LEFT_ALIGNMENT);
        lawLevel.setAlignmentX(Component.LEFT_ALIGNMENT);
        techLevel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        starport.addActionListener(uwpListener);
        worldSize.addActionListener(uwpListener);
        atmosphere.addActionListener(uwpListener);
        hydrographic.addActionListener(uwpListener);
        population.addActionListener(uwpListener);
        government.addActionListener(uwpListener);
        lawLevel.addActionListener(uwpListener);
        techLevel.addActionListener(uwpListener);

        jUWP.add(jLabel1, null);
        jUWP.add(starport, null);
        jUWP.add(jLabel2, null);
        jUWP.add(worldSize, null);
        jUWP.add(jLabel3, null);
        jUWP.add(atmosphere, null);
        jUWP.add(jLabel4, null);
        jUWP.add(hydrographic, null);
        jUWP.add(jLabel5, null);
        jUWP.add(population, null);
        jUWP.add(jLabel6, null);
        jUWP.add(government, null);
        jUWP.add(jLabel7, null);
        jUWP.add(lawLevel, null);
        jUWP.add(jLabel8, null);
        jUWP.add(techLevel, null);
        /*
        jUWP.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
        jUWP.add(starport, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(jLabel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(worldSize, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(jLabel3, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(atmosphere, new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(jLabel4, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(hydrographic, new GridBagConstraints(0, 7, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(jLabel5, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(population, new GridBagConstraints(0, 9, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(jLabel6, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(government, new GridBagConstraints(0, 11, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(jLabel7, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(lawLevel, new GridBagConstraints(0, 13, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(jLabel8, new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        jUWP.add(techLevel, new GridBagConstraints(0, 15, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 30, 5), 0, 0));
        */
        /* Add to contents panel */
        /* Setup content pane */
        contents.setLayout(gridBagLayout3);
        jLabel16.setText("Bases");
        jLabel17.setText("Travel Zone");
        jLabel18.setText("Allegiance");

        jLabel19.setText("Pop Mul");
        jLabel20.setText("Belts");
        jLabel21.setText("Giants");

        jLabel36.setText("Trade Codes:");
        tradeCodes.setText("Na De Ba");
        tradeCodes.setEditable(false);
        tradeCodes.setBackground(new Color(212, 208, 200));

        jLabel24.setText("Remark 1");
        jLabel25.setText("Remark 2");
        jLabel37.setText("Remark 3");
        jLabel38.setText("Remark 4");
        jLabel10.setText("Survey Date:");
        //surveyDate.setMinimumSize(this.getTextBoxSize("000-0000"));
        surveyDate.setColumns(9);
        surveyDate.setMinimumSize(surveyDate.getPreferredSize());
        
        contents.add(surveyDate, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 8, 0, 0), 0, 0));
        contents.add(giants, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 8, 0, 0), 0, 0));
        contents.add(belts, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 4, 0, 0), 0, 0));
        contents.add(popMul, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 8, 0, 0), 0, 0));
        contents.add(jLabel21, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 8, 0, 0), 0, 0));
        contents.add(jLabel20, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 4, 0, 0), 0, 0));
        contents.add(jLabel19, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 8, 0, 0), 0, 0));
        contents.add(jLabel18, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 8, 0, 0), 0, 0));
        contents.add(jLabel17, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 8, 0, 0), 0, 0));
        contents.add(jLabel16, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 8, 0, 0), 0, 0));
        contents.add(allegiance, new GridBagConstraints(0, 6, 4, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        contents.add(travelZone, new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        contents.add(bases, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        contents.add(jLabel36, new GridBagConstraints(5, 8, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
        contents.add(tradeCodes, new GridBagConstraints(6, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 5), 0, 0));
        contents.add(jLabel24, new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 8, 0, 0), 0, 0));
        contents.add(remark1, new GridBagConstraints(0, 10, 4, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        contents.add(jLabel25, new GridBagConstraints(5, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
        contents.add(remark2, new GridBagConstraints(5, 10, 2, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 8), 0, 0));
        contents.add(jLabel37, new GridBagConstraints(0, 11, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 8, 0, 0), 0, 0));
        contents.add(remark3, new GridBagConstraints(0, 12, 4, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 0), 0, 0));
        contents.add(jLabel38, new GridBagConstraints(5, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
        contents.add(remark4, new GridBagConstraints(5, 12, 2, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 8), 0, 0));
        contents.add(jLabel10, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 8, 0, 0), 0, 0));

        /* Add to stellar details panel */
        /* setup Stellar Details pane */
        stellarDetails.setLayout(gridBagLayout6);
        Dimension choiceDimension = new Dimension (175,25);
        commentPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));


        primarySpectrum.setPreferredSize(choiceDimension);
        primaryLuminosity.setPreferredSize(choiceDimension);
        companionSpectrum.setPreferredSize(choiceDimension);
        companionLuminosity.setPreferredSize(choiceDimension);
        tertiarySpectrum.setPreferredSize(choiceDimension);
        tertiaryLuminosity.setPreferredSize(choiceDimension);

        jLabel26.setText("Spectrum");
        jLabel27.setText("Modifier");
        jLabel28.setText("Luminosity");
        primary.setText("Primary");
        primary.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    primary_actionPerformed(e);
                }
            });

        companion.setText("Companion");
        companion.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    companion_actionPerformed(e);
                }
            });
        jLabel29.setText("Spectrum");
        jLabel30.setText("Modifier");
        jLabel31.setText("Luminosity");
        tertiary.setText("Tertiary");
        tertiary.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    tertiary_actionPerformed(e);
                }
            });
        jLabel32.setText("Spectrum");
        jLabel33.setText("Modifier");
        jLabel34.setText("Luminosity");

        stellarDetails.add(primary, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
        stellarDetails.add(tertiary, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        stellarDetails.add(companion, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 1, 0), 0, 0));
        stellarDetails.add(tertiaryLuminosity, new GridBagConstraints(2, 8, 1, 1, 0.25, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 8), 0, 0));
        stellarDetails.add(tertiaryModifier, new GridBagConstraints(1, 8, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        stellarDetails.add(tertiarySpectrum, new GridBagConstraints(0, 8, 1, 1, 0.25, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 5, 5), 0, 0));
        stellarDetails.add(jLabel34, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        stellarDetails.add(jLabel33, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        stellarDetails.add(jLabel32, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        stellarDetails.add(companionLuminosity, new GridBagConstraints(2, 5, 1, 1, 0.25, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 8), 0, 0));
        stellarDetails.add(companionModifier, new GridBagConstraints(1, 5, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        stellarDetails.add(companionSpectrum, new GridBagConstraints(0, 5, 1, 1, 0.25, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 0, 0));
        stellarDetails.add(jLabel31, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        stellarDetails.add(jLabel30, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        stellarDetails.add(jLabel29, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        stellarDetails.add(primaryLuminosity, new GridBagConstraints(2, 2, 1, 1, 0.25, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 8), 0, 0));
        stellarDetails.add(primaryModifier, new GridBagConstraints(1, 2, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        stellarDetails.add(primarySpectrum, new GridBagConstraints(0, 2, 1, 1, 0.25, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 5), 0, 0));
        stellarDetails.add(jLabel28, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        stellarDetails.add(jLabel27, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        stellarDetails.add(jLabel26, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));

        /* add to remarks panel */
        
        /* Add to this panel */
        jTabbedPane.addTab ("UWP: 000000-0", jUWP);
        jTabbedPane.addTab ("Contents", contents);
        jTabbedPane.addTab ("Stellar Details", stellarDetails);
        jTabbedPane.addTab ("Comments", commentPanel);
        
        this.getContentPane().add(location, BorderLayout.NORTH);
        this.getContentPane().add(jTabbedPane, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.pack();
    }

    protected void uwp_actionPerformed(JComboTable t)
    {

        if (system != null && t.getItemCount() > 0)
        {
            char update = t.getSelectedCode();        
            if (t.getName().matches("starport")) { system.setPort(update); }
            else if (t.getName().matches("worldSize")) { system.setSize(update); }
            else if (t.getName().matches("atmosphere")) { system.setAtmosphere(update); } 
            else if (t.getName().matches("hydrographic")) { system.setHydrograph(update); }
            else if (t.getName().matches("population")) { system.setPopulation(update); }
            else if (t.getName().matches("government")) { system.setGovernment(update); }
            else if (t.getName().matches("lawLevel")) { system.setLawlevel(update); } 
            else if (t.getName().matches("techLevel")) { system.setTechnology(update); }
            
            jTabbedPane.setTitleAt(0, "UWP: " + system.toString());
        }
    }

    public void loadReference (Astrogation data)
    {
        clearReference();
        addReference (data);
    }

    public void clearReference ()
    {
        starport.removeAllItems();
        worldSize.removeAllItems();
        atmosphere.removeAllItems();
        hydrographic.removeAllItems();
        population.removeAllItems();
        government.removeAllItems();
        lawLevel.removeAllItems();
        techLevel.removeAllItems();
        bases.removeAllItems();
        travelZone.removeAllItems();
        allegiance.removeAllItems();
        remark1.removeAllItems();
        remark2.removeAllItems();
        remark3.removeAllItems();
        remark4.removeAllItems();
        primarySpectrum.removeAllItems();
        companionSpectrum.removeAllItems();
        tertiarySpectrum.removeAllItems();
        primaryLuminosity.removeAllItems();
        companionLuminosity.removeAllItems();
        tertiaryLuminosity.removeAllItems();
        
    }
    
    public void addReference (Astrogation data)
    {
        References refData;
        References localData;
        if (data == null)
        {
            refData = ViewTableData.getInstance().getGlobalReferences();
            localData = ViewTableData.getInstance().getLocalReferences();
        }
        else
        {
            refData = data.getGlobalReferences();
            localData = data.getLocalReferences();
        }
        
        if (refData == null) return;
        TableRowRecord o;
        Iterator<TableRowRecord> j;
        for (TableRecord currentData : refData.tablesIterator())
        {
            switch (currentData.getTableKey())
            {
                case PORTS:    
                    starport.addItems(currentData.iterator());
                    break;
                case SIZE:
                    worldSize.addItems(currentData.iterator());
                    break;
                case ATMOSPHERE:
                    atmosphere.addItems(currentData.iterator());
                    break;
                case HYDROGRAPHICS:
                    hydrographic.addItems(currentData.iterator());
                    break;
                case POPULATION:
                    population.addItems(currentData.iterator());
                    break;
                case GOVERNMENT:
                    government.addItems(currentData.iterator());
                    break;
                case LAW_LEVEL:
                    lawLevel.addItems(currentData.iterator());
                    break;
                case TECHNOLOGY:
                    techLevel.addItems(currentData.iterator());
                    break;
                case BASES:
                    bases.addItem(new String (" "));
                    if (localData != null && localData.getTable (TableRecordKey.BASES) != null)
                    {
                        bases.addItems(localData.getTable(TableRecordKey.BASES).iterator());
                    }
                    else
                    {
                        bases.addItems(currentData.iterator());
                    }
                    break;
                case TRAVEL_ZONE:
                    travelZone.addItem(new String (" "));
                    travelZone.addItems(currentData.iterator());
                    break;
                case POLITIES:
                    if (localData != null && localData.getTable(TableRecordKey.POLITIES) != null)
                    {
                        if (allegiance.getItemCount() > 0) break;
                        allegiance.addItems(localData.getTable(TableRecordKey.POLITIES).iterator());
                    }
                    else
                    {
                        allegiance.addItems(currentData.iterator());
                    }
                    break;
                case REMARKS_CODES:
                    String blank = new String (" ");
                    remark1.addItem(blank);
                    remark2.addItem(blank);
                    remark3.addItem(blank);
                    remark4.addItem(blank);
                    remark1.addItems(currentData.iterator());
                    remark2.addItems(currentData.iterator());
                    remark3.addItems(currentData.iterator());
                    remark4.addItems(currentData.iterator());
                    break;
                case STAR_SPECTRUM:
                    j  = currentData.iterator();
                    while (j.hasNext())
                    {            
                        o = j.next();
                        primarySpectrum.addItem(o);
                        companionSpectrum.addItem(o);
                        tertiarySpectrum.addItem(o);
                    }
                    break;
                case STAR_LUMINOSITY:
                    j = currentData.iterator();
                    while (j.hasNext())
                    {
                        o = j.next();
                        primaryLuminosity.addItem(o);
                        companionLuminosity.addItem(o);
                        tertiaryLuminosity.addItem(o);
                    }
                    break;
            }
        }
    }
    public void stateChanged (ChangeEvent e)
    {
        Astrogation data = (Astrogation) e.getSource();
        
        addGroups (data.getGroups());
        loadReference(data);
    }

    public void addGroups (ListIterator groupList)
    {
        groupName.removeAll();
        groupName.addItems(groupList);
    }

    public boolean isDuplicate()
    {
        return duplicate;
    }

    public StarSystem getSystem()
    {
        return data;
    }

    public void setSystem (StarSystem data)
    {
        this.data = data; 
        this.system = new UWP(this.data.getPlanet());
        
        /* set values */
        worldName.setText(data.getName());
        hexLocation.setText(data.getLocation().toString());
        groupName.setSelectedItem(data.getLocation().getHexGroup());        

        /* set UWP  */
        starport.selectWithKeyChar(data.getPlanet().getPort());
        worldSize.selectWithKeyChar(data.getPlanet().getSize());
        atmosphere.selectWithKeyChar(data.getPlanet().getAtmosphere());
        hydrographic.selectWithKeyChar(data.getPlanet().getHydrograph());
        population.selectWithKeyChar(data.getPlanet().getPopulation());
        government.selectWithKeyChar(data.getPlanet().getGovernment());
        lawLevel.selectWithKeyChar(data.getPlanet().getLawlevel());
        techLevel.selectWithKeyChar(data.getPlanet().getTechnology());

        /* Set Other Information */
        bases.selectWithKeyChar(data.getBase());
        travelZone.selectWithKeyChar(data.getZone());
        allegiance.setSelectedItem(data.getPolity());
        surveyDate.setText(data.getLocation().getDate());
        
        popMul.setValue(new Integer(data.getMultiplier()));
        belts.setValue(new Integer(data.getBelts()));
        giants.setValue(new Integer(data.getGiants()));

        /* Remarks?? */
        /* Trade Codes?? */
        /* Stellar data */
        if (data.hasPrimary())
        {
            primary.setSelected(true);
            primarySpectrum.selectWithKeyChar(data.getPrimaryStar().getSpectrum());
            primarySpectrum.setEnabled(true);
            primaryModifier.setValue(new Integer(Character.getNumericValue(data.getPrimaryStar().getModifier())));
            primaryModifier.setEnabled(true);
            primaryLuminosity.setSelectedItem(data.getPrimaryStar().getLuminosity());
            primaryLuminosity.setEnabled(true);
        }
        else
        {
            primary.setSelected(false);
            primarySpectrum.setEnabled(false);
            primaryModifier.setEnabled(false);
            primaryLuminosity.setEnabled(false);
        }
        
        if (data.hasCompanion())
        {
            companion.setSelected(true);
            companionSpectrum.selectWithKeyChar(data.getCompanionStar().getSpectrum());
            companionSpectrum.setEnabled(true);
            companionModifier.setValue(new Integer(Character.getNumericValue(data.getCompanionStar().getModifier())));
            companionModifier.setEnabled(true);
            companionLuminosity.setSelectedItem(data.getCompanionStar().getLuminosity());
            companionLuminosity.setEnabled(true);
        }
        else
        {
            companion.setSelected(false);
            companionSpectrum.setEnabled(false);
            companionModifier.setEnabled(false);
            companionLuminosity.setEnabled(false);
        }

        if (data.hasTertiary())
        {
            tertiary.setSelected(true);
            tertiarySpectrum.selectWithKeyChar(data.getTertiaryStar().getSpectrum());
            tertiarySpectrum.setEnabled(true);
            tertiaryModifier.setValue(new Integer(Character.getNumericValue(data.getTertiaryStar().getModifier())));
            tertiaryModifier.setEnabled(true);
            tertiaryLuminosity.setSelectedItem(data.getTertiaryStar().getLuminosity());
            tertiaryLuminosity.setEnabled(true);
            
        }
        else
        {
            tertiary.setSelected(false);
            tertiarySpectrum.setEnabled(false);
            tertiaryModifier.setEnabled(false);
            tertiaryLuminosity.setEnabled(false);
        }
        if (data.getComment() != null && data.getComment().getLength() > 0)
        {
            commentPanel.setDocument (data.getComment());
        }
        else
        {
            commentPanel.newDocument();
        }
    }
    
    public void newSystem()
    {
        duplicate = true;
        /* set values */
        worldName.setText("");
        hexLocation.setText("");
                
        starport.setSelectedIndex(0);;
        worldSize.setSelectedIndex(0);
        atmosphere.setSelectedIndex(0);
        hydrographic.setSelectedIndex(0);
        population.setSelectedIndex(0);
        government.setSelectedIndex(0);
        lawLevel.setSelectedIndex(0);
        techLevel.setSelectedIndex(0);
        /* Set Other Information */
        bases.setSelectedIndex(0);
        travelZone.setSelectedIndex(0);
        allegiance.setSelectedIndex(0);

        
        popMul.setValue(new Integer(0));
        belts.setValue(new Integer(0));
        giants.setValue(new Integer(0));

        primary.setSelected(false);
        primarySpectrum.setEnabled(false);
        primaryModifier.setEnabled(false);
        primaryLuminosity.setEnabled(false);
        companion.setSelected(false);
        companionSpectrum.setEnabled(false);
        companionModifier.setEnabled(false);
        companionLuminosity.setEnabled(false);
        tertiary.setSelected(false);
        tertiarySpectrum.setEnabled(false);
        tertiaryModifier.setEnabled(false);
        tertiaryLuminosity.setEnabled(false);
        commentPanel.newDocument();
    }

    private void bCancel_actionPerformed(ActionEvent e)
    {
        duplicate = false;
        this.setVisible(false);
    }

    private void bOK_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
        if (duplicate) 
        {
            data = new StarSystem();
            data.setPlanet(new UWP());
            data.setKey(""); // Set to what??
        }
        /* copy data from this to StarSystem.data */
        data.setName(worldName.getText());
        data.getLocation().setHex(hexLocation.getText());
        data.getLocation().setDate(surveyDate.getText());
        data.setComment(commentPanel.getDocument());

        data.getPlanet().setPort(starport.getSelectedCode());
        data.getPlanet().setSize(worldSize.getSelectedCode());
        data.getPlanet().setAtmosphere(atmosphere.getSelectedCode());
        data.getPlanet().setHydrograph(hydrographic.getSelectedCode());
        data.getPlanet().setPopulation(population.getSelectedCode());
        data.getPlanet().setGovernment(government.getSelectedCode());
        data.getPlanet().setLawlevel(lawLevel.getSelectedCode());
        data.getPlanet().setTechnology(techLevel.getSelectedCode());
        data.setBase(bases.getSelectedCode());
        data.setZone(travelZone.getSelectedCode());
        data.setPolity(((TableRowRecord)(allegiance.getSelectedItem())).getCode());
        data.setMultiplier(((Integer)(popMul.getValue())).intValue());
        data.setBelts(((Integer)(belts.getValue())).intValue());
        data.setGiants(((Integer)(giants.getValue())).intValue());

        if(primary.isSelected())
        {
            Star primary = new Star();
            primary.setSpectrum(primarySpectrum.getSelectedItem().toString().charAt(0));
            primary.setModifier(primaryModifier.getValue().toString().charAt(0));
            primary.setLuminosity(((TableRowRecord)primaryLuminosity.getSelectedItem()).getCode());
            data.setPrimary(primary);
        }
        else
        {
            data.clearPrimary();
        }
        
        if (companion.isSelected())
        {
            Star companion = new Star ();
            companion.setSpectrum(companionSpectrum.getSelectedItem().toString().charAt(0));
            companion.setModifier(companionModifier.getValue().toString().charAt(0));
            companion.setLuminosity(((TableRowRecord)companionLuminosity.getSelectedItem()).getCode());
            data.setCompanion(companion);
        }
        else
        {
            data.clearCompanion();
        }

        if (tertiary.isSelected())
        {
            Star tertiary = new Star();
            tertiary.setSpectrum(tertiarySpectrum.getSelectedItem().toString().charAt(0));
            tertiary.setModifier(companionModifier.getValue().toString().charAt(0));
            tertiary.setLuminosity(((TableRowRecord)tertiaryLuminosity.getSelectedItem()).getCode());
            data.setTertiary(tertiary);
        }
        else
        {
            data.clearTertiary(); 
        }
    }

    private void companion_actionPerformed(ActionEvent e)
    {
        if (companion.isSelected())
        {
            companionSpectrum.setEnabled(true);
            companionModifier.setEnabled(true);
            companionLuminosity.setEnabled(true);
        }
        else
        {
            companionSpectrum.setEnabled(false);
            companionModifier.setEnabled(false);
            companionLuminosity.setEnabled(false);
        }
    }

    private void tertiary_actionPerformed(ActionEvent e)
    {
        if (tertiary.isSelected())
        {
            tertiarySpectrum.setEnabled(true);
            tertiaryModifier.setEnabled(true);
            tertiaryLuminosity.setEnabled(true);
        }
        else
        {
            tertiarySpectrum.setEnabled(false);
            tertiaryModifier.setEnabled(false);
            tertiaryLuminosity.setEnabled(false);
        }
    }

    private void primary_actionPerformed(ActionEvent e)
    {
        if (primary.isSelected())
        {
            primarySpectrum.setEnabled(true);
            primaryModifier.setEnabled(true);
            primaryLuminosity.setEnabled(true);
        }
        else
        {
            primarySpectrum.setEnabled(false);
            primaryModifier.setEnabled(false);
            primaryLuminosity.setEnabled(false);
        }
    }

    private class UWPActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            uwp_actionPerformed ((JComboTable)e.getSource());
        }
    }

    private void bDuplicate_actionPerformed(ActionEvent e)
    {
        duplicate = true;
    }
    
    private Dimension getTextBoxSize (String test)
    {
        int width = this.getFontMetrics(this.getFont()).stringWidth(test);
        int height = this.getFontMetrics(this.getFont()).getHeight()+5;
        return new Dimension (width, height);
    }

}