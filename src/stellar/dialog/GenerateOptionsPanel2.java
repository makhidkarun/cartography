package stellar.dialog;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.ComboBoxModel;
import javax.swing.BorderFactory;

public class GenerateOptionsPanel2 extends JPanel 
{
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JComboBox cbBasicDetails = new JComboBox();
    private JComboBox cbDetailedSystem = new JComboBox();
    private JComboBox cbWorldMap = new JComboBox();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private ComboBoxModel comboBoxModel1 = new javax.swing.DefaultComboBoxModel();
    private JPanel pTrade = new JPanel();
    private JCheckBox cTradeBook7 = new JCheckBox();
    private JCheckBox cTradeFT = new JCheckBox();
    private JCheckBox cTradeT20 = new JCheckBox();
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private JPanel pCultural = new JPanel();
    private GridBagLayout gridBagLayout3 = new GridBagLayout();
    private JCheckBox cCultureCensus = new JCheckBox();
    private JCheckBox cCultureFI = new JCheckBox();
    private JPanel pMilitary = new JPanel();
    private GridBagLayout gridBagLayout4 = new GridBagLayout();
    private JCheckBox cMilitaryStriker = new JCheckBox();
    private JCheckBox cMilitaryPE = new JCheckBox();
    private JCheckBox cMilitaryGF = new JCheckBox();
    private JPanel pAnimal = new JPanel();
    private GridBagLayout gridBagLayout5 = new GridBagLayout();
    private JCheckBox jCheckBox1 = new JCheckBox();
    private JCheckBox jCheckBox2 = new JCheckBox();
    private JCheckBox cPsionicInstitute = new JCheckBox();
    private JComboBox jcModifiersBasic = new JComboBox();
    private JLabel jLabel5 = new JLabel();
    private BoxLayout boxLayout1;
    private BoxLayout boxLayout2;
    private BoxLayout boxLayout3;
    private BoxLayout boxLayout4;

    public GenerateOptionsPanel2()
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

        cbBasicDetails.addItem("None");
        cbBasicDetails.addItem ("Book 3: Worlds and Adventures");
        cbBasicDetails.addItem ("Book 6: Scouts");
        cbBasicDetails.addItem ("MegaTraveller");
        cbBasicDetails.addItem ("Traveller: New Era");
        cbBasicDetails.addItem ("Mark Miller's Traveller");
        cbBasicDetails.addItem ("Traveller 3D");
        
        cbDetailedSystem.addItem("None");
        cbDetailedSystem.addItem("Book 6: Scouts");
        cbDetailedSystem.addItem("MegaTraveller");
        cbDetailedSystem.addItem("Traveller: New Era");
        cbDetailedSystem.addItem("GURPS Traveller: First In");
        cbDetailedSystem.addItem("GURPS Space");
        cbDetailedSystem.addItem("Traveller D20");
        cbDetailedSystem.addItem("Galactic StarGen");
        cbDetailedSystem.addItem("Accrete");

        cbWorldMap.addItem ("None");
        cbWorldMap.addItem ("Grand Survey");
        cbWorldMap.addItem ("World Tamer's Handbook");
        cbWorldMap.addItem ("Fractal Generate");
        
        String economicProfiles [] = {"Book 7: Merchant Prince", "GURPS Traveller: Far Trader", "Traveller T20"};
        
        String culturalProfiles[] = {"Grand Census", "GURPS Traveller: First In"};
        
        String militaryProfiles [] = {"Striker", "Adventure 5: Trillion Credit Squadron", 
            "Pocket Empires", "GURPS Traveller: Ground Forces"};

        String encounterTables [] = {"Book 3: Worlds and Adventures", "GURPS Traveller: First In"};
        
        jLabel1.setText("Basic System");
        jLabel2.setText("System Details");
        jLabel3.setText("World Details");

        boxLayout4 = new BoxLayout (pTrade, BoxLayout.PAGE_AXIS);
        pTrade.setBorder(BorderFactory.createTitledBorder(" Economic and Trade Profiles "));
        pTrade.setLayout(boxLayout4);
        cTradeBook7.setText("Book 7: Merchant Prince");
        cTradeFT.setText("GURPS Traveller: Far Trader");
        cTradeT20.setText("Traveller T20");

        boxLayout3 = new BoxLayout (pCultural, BoxLayout.PAGE_AXIS);
        pCultural.setLayout(boxLayout3);
        pCultural.setBorder(BorderFactory.createTitledBorder(" Cultural Profiles "));
        cCultureCensus.setText("Grand Census");
        cCultureFI.setText("GURPS Traveller: First In");
        cPsionicInstitute.setText("Psionic Institutes");

        boxLayout2 = new BoxLayout (pMilitary, BoxLayout.PAGE_AXIS);
        pMilitary.setLayout(boxLayout2);
        pMilitary.setBorder(BorderFactory.createTitledBorder(" Military Profiles "));
        cMilitaryStriker.setText("Striker");
        cMilitaryPE.setText("Pocket Empires");
        cMilitaryGF.setText("GURPS Traveller: Ground Forces");
        cMilitaryGF.setToolTipText("null");

        boxLayout1 = new BoxLayout (pAnimal, BoxLayout.PAGE_AXIS);
        pAnimal.setBorder(BorderFactory.createTitledBorder(" Animal Encounter Tables "));
        pAnimal.setLayout(boxLayout1);
        
        jCheckBox1.setText("Book 3: Worlds and Adventures");
        jCheckBox2.setText("GURPS Traveller: First In");
        
        jLabel5.setText("Basic System Modifiers");
        
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
        
        this.add(cbBasicDetails, new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(cbDetailedSystem, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
        this.add(cbWorldMap, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
        this.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    
        pTrade.add(cTradeBook7, null );
        pTrade.add(cTradeFT, null);
        pTrade.add(cTradeT20, null);
        pTrade.add(Box.createVerticalGlue(), null);
        this.add(pTrade, new GridBagConstraints(2, 2, 2, 4, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    
        pCultural.add(cCultureCensus, null);
        pCultural.add(cCultureFI, null);
        pCultural.add(cPsionicInstitute, null);
        pCultural.add(Box.createVerticalGlue(), null);
        this.add(pCultural, new GridBagConstraints(2, 5, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    
        pMilitary.add(cMilitaryStriker, null);
        pMilitary.add(cMilitaryPE, null);
        pMilitary.add(cMilitaryGF, null);
        pMilitary.add (Box.createVerticalGlue(), null);
        this.add(pMilitary, new GridBagConstraints(1, 5, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));


        pAnimal.add (jCheckBox1, null);
        pAnimal.add (jCheckBox2, null);
        pAnimal.add (Box.createVerticalGlue(),null);
        this.add(pAnimal, new GridBagConstraints(1, 3, 1, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));

        this.add(jcModifiersBasic, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
        this.add(jLabel5, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
        this.setSize(this.getMinimumSize());

    }
}
