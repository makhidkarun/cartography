package stellar.dialog;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.BorderFactory;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;

public class GenerateOptionsPanel extends JPanel 
{
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JPanel pFullGeneration = new JPanel();
    private GridBagLayout gridBagLayout6 = new GridBagLayout();
    private JRadioButton rBook6Full = new JRadioButton();
    private JRadioButton rMTFull = new JRadioButton();
    private JRadioButton rTNEFull = new JRadioButton();
    private JRadioButton rGTFIFull = new JRadioButton();
    private JRadioButton rT20Full = new JRadioButton();
    private JRadioButton rSpaceFull = new JRadioButton();
    private JCheckBox cBasicSystem = new JCheckBox();
    private JPanel pBasicGeneration = new JPanel();
    private GridBagLayout gridBagLayout4 = new GridBagLayout();
    private JRadioButton rBook3Basic = new JRadioButton();
    private JRadioButton rBook6Basic = new JRadioButton();
    private JRadioButton rMTBasic = new JRadioButton();
    private JRadioButton rTNEBasic = new JRadioButton();
    private JRadioButton rT4Basic = new JRadioButton();
    private JRadioButton rT3DBasic = new JRadioButton();
    private JLabel jLabel5 = new JLabel();
    private JComboBox jcModifiersBasic = new JComboBox();
    private JCheckBox cExtendedSystem = new JCheckBox();
    private JCheckBox cFullSystem = new JCheckBox();
    private JPanel pExtendedGeneration = new JPanel();
    private GridBagLayout gridBagLayout5 = new GridBagLayout();
    private JRadioButton rBook6Ext = new JRadioButton();
    private JRadioButton rMTExt = new JRadioButton();
    private JRadioButton rTNEExt = new JRadioButton();
    private JRadioButton rGalExt = new JRadioButton();
    private JRadioButton rAccreteExt = new JRadioButton();
    private JLabel jLabel7 = new JLabel();
    private JCheckBox cRSGTExt = new JCheckBox();
    private JPanel pOptions = new JPanel();
    private GridBagLayout gridBagLayout7 = new GridBagLayout();
    private JCheckBox cTCSOption = new JCheckBox();
    private JCheckBox cSurveyOption = new JCheckBox();
    private JCheckBox cCensusOption = new JCheckBox();
    private JCheckBox cWTHOption = new JCheckBox();
    private JCheckBox cPEOption = new JCheckBox();
    private JCheckBox cGTFTOption = new JCheckBox();
    private JCheckBox cGTGFOption = new JCheckBox();
    private JLabel jLabel6 = new JLabel();
    private ButtonGroup  basicButtons = new ButtonGroup();
    private ButtonGroup  extendedButtons = new ButtonGroup();
    private ButtonGroup  fullButtons = new ButtonGroup();

    public GenerateOptionsPanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void jbInit() throws Exception
    {
        this.setLayout(gridBagLayout1);
        pFullGeneration.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        pFullGeneration.setLayout(gridBagLayout6);

        rBook6Full.setText("Book 6: Scouts");
        rMTFull.setText("Megatraveller");
        rTNEFull.setText("Traveller: New Era");
        rGTFIFull.setText("GURPS: First In");
        rT20Full.setText("Traveller D20");
        rSpaceFull.setText("GURPS: Space");
        fullButtons.add (rBook6Full);
        fullButtons.add (rMTFull);
        fullButtons.add (rTNEFull);
        fullButtons.add (rGTFIFull);
        fullButtons.add (rT20Full);
        fullButtons.add (rSpaceFull);

        cBasicSystem.setText("Basic System");
        pBasicGeneration.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        pBasicGeneration.setLayout(gridBagLayout4);

        rBook3Basic.setText("Book 3: Worlds");
        rBook6Basic.setText("Book 6: Scouts");
        rMTBasic.setText("Megatraveller");
        rTNEBasic.setText("Traveller: New Era");
        rT4Basic.setText("Traveller 4th");
        rT3DBasic.setText("Traveller 3D");
        basicButtons.add (rBook3Basic);
        basicButtons.add (rBook6Basic);
        basicButtons.add (rMTBasic);
        basicButtons.add (rTNEBasic);
        basicButtons.add (rT4Basic);
        basicButtons.add (rT3DBasic);
        
        jLabel5.setText("Modifiers");
        cExtendedSystem.setText("Extended System");
        cFullSystem.setText("Full System");
        pExtendedGeneration.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        pExtendedGeneration.setLayout(gridBagLayout5);
        rBook6Ext.setText("Book 6: Scouts");
        rMTExt.setText("Megatraveller");
        rTNEExt.setText("Traveller: New Era");
        rGalExt.setText("Galactic Stargen");
        rAccreteExt.setText("Accrete");
        extendedButtons.add (rBook6Ext);
        extendedButtons.add (rMTExt);
        extendedButtons.add (rTNEExt);
        extendedButtons.add (rGalExt);
        extendedButtons.add (rAccreteExt);
        
        jLabel7.setText("Modifiers");
        cRSGTExt.setText("RSGT");
        pOptions.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        pOptions.setLayout(gridBagLayout7);
        cTCSOption.setText("TCS Budgets");
        cSurveyOption.setText("Grand Survey");
        cCensusOption.setText("Grand Census");
        cWTHOption.setText("World Tamers");
        cPEOption.setText("Pocket Empires");
        cGTFTOption.setText("GURPS: Far Trader");
        cGTGFOption.setText("GURPS: Ground Forces");
        jLabel6.setText("Detail Options");
        pOptions.add(cTCSOption, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pOptions.add(cSurveyOption, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pOptions.add(cCensusOption, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pOptions.add(cWTHOption, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pOptions.add(cPEOption, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pOptions.add(cGTFTOption, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pOptions.add(cGTGFOption, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pExtendedGeneration.add(rBook6Ext, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pExtendedGeneration.add(rMTExt, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pExtendedGeneration.add(rTNEExt, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pExtendedGeneration.add(rGalExt, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pExtendedGeneration.add(rAccreteExt, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pExtendedGeneration.add(jLabel7, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pExtendedGeneration.add(cRSGTExt, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pBasicGeneration.add(rBook3Basic, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pBasicGeneration.add(rBook6Basic, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pBasicGeneration.add(rMTBasic, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pBasicGeneration.add(rTNEBasic, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pBasicGeneration.add(rT4Basic, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pBasicGeneration.add(rT3DBasic, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pBasicGeneration.add(jLabel5, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pBasicGeneration.add(jcModifiersBasic, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jcModifiersBasic.addItem("None");
        jcModifiersBasic.addItem("Alien Module 1");
        jcModifiersBasic.addItem("Alien Module 2");
        jcModifiersBasic.addItem("Alien Module 3");
        jcModifiersBasic.addItem("Alien Module 4");
        jcModifiersBasic.addItem("Alien Module 5");
        jcModifiersBasic.addItem("Alien Module 6");
        jcModifiersBasic.addItem("Alien Module 7");
        jcModifiersBasic.addItem("Hard Times step 1");
        jcModifiersBasic.addItem("Hard Times step 3");
        jcModifiersBasic.addItem("Hard Times step 8");
        jcModifiersBasic.addItem("Hard Times step 9");
        jcModifiersBasic.addItem("TNE Collapse");
        pFullGeneration.add(rBook6Full, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pFullGeneration.add(rMTFull, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pFullGeneration.add(rTNEFull, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pFullGeneration.add(rGTFIFull, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pFullGeneration.add(rT20Full, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pFullGeneration.add(rSpaceFull, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        this.add(cBasicSystem, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(pBasicGeneration, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
        this.add(cExtendedSystem, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(pExtendedGeneration, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        this.add(pFullGeneration, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        this.add(cFullSystem, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(jLabel6, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(pOptions, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        this.setSize(this.getPreferredSize());
    }
}