package stellar.dialog;
import stellar.data.Astrogation;
import stellar.data.GroupType;
import stellar.map.MapScale;
import stellar.map.MapScope;
import stellar.swing.AstrogationChangeListener;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;

public class EditViewOptions extends JDialog implements AstrogationChangeListener
{
    private boolean updated = false; 
    private static EditViewOptions _instance;
    
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JButton bOK = new JButton();
    private JButton bCancel = new JButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel jLabel5 = new JLabel();
    private JTextField surveyStartDate = new JTextField();
    private JLabel jLabel6 = new JLabel();
    private JTextField surveyEndDate = new JTextField();

    private JRadioButton groupSystem = new JRadioButton();
    private JRadioButton groupSubsector = new JRadioButton();
    private JRadioButton groupQuadrant = new JRadioButton();
    private JRadioButton groupSector = new JRadioButton();
    private JRadioButton groupDomain = new JRadioButton();
    private JRadioButton scopeSubsector = new JRadioButton();
    private JRadioButton scopeQuadrant = new JRadioButton();
    private JRadioButton scopeSector = new JRadioButton();
    private JRadioButton scopeDomain = new JRadioButton();
    private JRadioButton scopeAll = new JRadioButton();
    private JRadioButton scale5 = new JRadioButton();
    private JRadioButton scale4 = new JRadioButton();
    private JRadioButton scale3 = new JRadioButton();
    private JRadioButton scale2 = new JRadioButton();
    private JRadioButton scale1 = new JRadioButton();
    private ButtonGroup groupButtons = new ButtonGroup();
    private ButtonGroup scaleButtons = new ButtonGroup();
    private ButtonGroup scopeButtons = new ButtonGroup();

    public EditViewOptions()
    {
        this(null, "", false);
    }

    /**
     * 
     * @param parent
     * @param title
     * @param modal
     */
    public EditViewOptions(Frame parent, String title, boolean modal)
    {
        super(parent, title, modal);
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public static synchronized EditViewOptions getInstance()
    {
        if (_instance == null) _instance = new EditViewOptions();
        return _instance;
    }
    
    private void jbInit() throws Exception
    {
        this.setSize(new Dimension(479, 239));
        this.getContentPane().setLayout(gridBagLayout1);
        this.setTitle("Edit Map View Options");
        this.setModal(true);
        jLabel1.setText("Group Level");
        jLabel2.setText("Scope");
        jLabel3.setText("Scale");
        jLabel4.setText("Survey");
        
        groupSystem.setText("System");
        groupSystem.setMnemonic('Y');
        groupSystem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    groupSystem_actionPerformed(e);
                }
            });
        groupSubsector.setText("Subsectors");
        groupSubsector.setMnemonic('B');
        groupQuadrant.setText("Quadrants");
        groupQuadrant.setMnemonic('Q');
        groupSector.setText("Sectors");
        groupSector.setMnemonic('S');
        groupDomain.setText("Domain");
        groupDomain.setMnemonic('D');
        
        bOK.setText("OK");
        bOK.setMnemonic('O');
        bOK.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bOK_actionPerformed(e);
                }
            });
        bCancel.setText("Cancel");
        bCancel.setMnemonic('C');
        bCancel.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bCancel_actionPerformed(e);
                }
            });

        scopeSubsector.setText("Subsector");
        scopeSubsector.setMnemonic('B');
        scopeQuadrant.setText("Quadrant");
        scopeQuadrant.setMnemonic('Q');
        scopeSector.setText("Sector");
        scopeSector.setMnemonic('S');
        scopeDomain.setText("Domain");
        scopeDomain.setMnemonic('D');
        scopeAll.setText("All");
        scopeAll.setMnemonic('A');
        
        scale5.setText("Scale 5");
        scale5.setMnemonic('5');
        scale4.setText("Scale 4");
        scale4.setMnemonic('4');
        scale3.setText("Scale 3");
        scale3.setMnemonic('3');
        scale2.setText("Scale 2");
        scale2.setMnemonic('2');
        scale1.setText("Scale 1");
        scale1.setMnemonic('1');

        jLabel5.setText("Start Date");
        surveyStartDate.setText("001-1200");
        surveyStartDate.setColumns(7);
        surveyStartDate.setMinimumSize(surveyStartDate.getPreferredSize());
        jLabel6.setText("End Date");
        surveyEndDate.setText("001-1200");
        surveyEndDate.setColumns(7);
        surveyEndDate.setMinimumSize(surveyEndDate.getPreferredSize());

        this.getContentPane().add(groupDomain, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
        this.getContentPane().add(groupSector, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
        this.getContentPane().add(groupQuadrant, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
        this.getContentPane().add(groupSubsector, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 0, 0), 3, 0));
        this.getContentPane().add(groupSystem, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
        this.getContentPane().add(jLabel4, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
        this.getContentPane().add(jLabel3, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
        this.getContentPane().add(jLabel2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));
        this.getContentPane().add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
        this.getContentPane().add(bOK, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(bCancel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(scopeSubsector, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(scopeQuadrant, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(scopeSector, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(scopeDomain, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(scopeAll, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(scale5, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(scale4, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(scale3, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(scale2, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(scale1, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        this.getContentPane().add(jLabel5, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
        this.getContentPane().add(surveyStartDate, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
        this.getContentPane().add(jLabel6, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 10, 0, 0), 0, 0));
        this.getContentPane().add(surveyEndDate, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 0), 0, 0));
        
        groupButtons.add (groupSystem);
        groupButtons.add (groupSubsector);
        groupButtons.add (groupQuadrant);
        groupButtons.add (groupSector);
        groupButtons.add (groupDomain);
        
        scaleButtons.add (scale5);
        scaleButtons.add (scale4);
        scaleButtons.add (scale3);
        scaleButtons.add (scale2);
        scaleButtons.add (scale1);
        scopeButtons.add (scopeSubsector);
        scopeButtons.add (scopeQuadrant);
        scopeButtons.add (scopeSector);
        scopeButtons.add (scopeDomain);
        scopeButtons.add (scopeAll);
        
        groupSystem.setSelected(true);
        scale3.setSelected(true);
        scopeAll.setSelected(true);
        this.pack();
    }
    
    public void setScale (MapScale scale)
    {
        switch (scale)
        {
            case SCALE_1: scale1.setSelected(true);break;
            case SCALE_2: scale2.setSelected(true); break;
            case SCALE_3: scale3.setSelected(true); break;
            case SCALE_4: scale4.setSelected(true); break;
            case SCALE_5: scale5.setSelected(true); break;
        }
    }

    public MapScope getScope()
    {
        MapScope scope = null;
        switch (scopeButtons.getSelection().getMnemonic())
        {
            case 'A': scope = MapScope.ALL; break;
            case 'D': scope = MapScope.DOMAIN; break;
            case 'S': scope = MapScope.SECTOR; break;
            case 'Q': scope = MapScope.QUADRANT; break;
            case 'B': scope = MapScope.SUBSECTOR; break;
        }
        return scope;
    }
    
    public void setScope (MapScope scope)
    {
        if (scope == null) return;
        switch (scope)
        {
            case ALL: scopeAll.setSelected(true); break;
            case DOMAIN: scopeDomain.setSelected(true); break;
            case SECTOR: scopeSector.setSelected(true); break;
            case QUADRANT: scopeQuadrant.setSelected(true); break;
            case SUBSECTOR: scopeSubsector.setSelected(true); break;
        }
    }

    public void setScopeEnabled (MapScope scope, boolean enabled)
    {
        switch (scope)
        {
            case ALL: scopeAll.setEnabled(enabled); break;
            case DOMAIN: scopeDomain.setEnabled(enabled); break;
            case SECTOR: scopeSector.setEnabled(enabled); break;
            case QUADRANT: scopeQuadrant.setEnabled(enabled); break;
            case SUBSECTOR: scopeSubsector.setEnabled(enabled); break;
        }
    }
    
    public void setLevel (GroupType level)
    {
        switch (level)
        {
            case DOMAIN: groupDomain.setSelected(true); break;
            case SECTOR: groupSector.setSelected(true); break;
            case QUADRANT: groupQuadrant.setSelected(true); break;
            case SUBSECTOR: groupSubsector.setSelected(true); break;
            case SYSTEM: groupSystem.setSelected(true); break;
        }
    }
    
    public GroupType getLevel()
    {
        GroupType level = null;

        switch (groupButtons.getSelection().getMnemonic())
        {
            case 'D': level = GroupType.DOMAIN; break;
            case 'S': level = GroupType.SECTOR; break;
            case 'Q': level = GroupType.QUADRANT; break;
            case 'B': level = GroupType.SUBSECTOR; break;
            case 'Y': level = GroupType.SYSTEM; break;
        }
        return level;
    }
    public void setLevelEnabled (GroupType level, boolean enabled)
    {
        switch (level)
        {
            case DOMAIN: groupDomain.setEnabled(enabled); break;
            case SECTOR: groupSector.setEnabled(enabled); break;
            case QUADRANT: groupQuadrant.setEnabled(enabled); break;
            case SUBSECTOR: groupSubsector.setEnabled(enabled); break;
            case SYSTEM: groupSystem.setEnabled(enabled); break;
        }
    }
    
    public MapScale getScale()
    {
        MapScale scale = null;
        switch (scaleButtons.getSelection().getMnemonic())
        {
            case '1': scale = MapScale.SCALE_1; break;
            case '2': scale = MapScale.SCALE_2; break;
            case '3': scale = MapScale.SCALE_3; break;
            case '4': scale = MapScale.SCALE_4; break;
            case '5': scale = MapScale.SCALE_5; break;
        }
        return scale; 
    }
/*    
    private void menuViewLevelDomain_actionPerformed(ActionEvent e)
    {
        menuViewScopeAll.setEnabled(true);
        menuViewScopeDomain.setEnabled(false);
        menuViewScopeSector.setEnabled(false);
        menuViewScopeQuadrant.setEnabled(false);
        menuViewScopeSubsector.setEnabled(false);
        menuViewScopeAll.setSelected(true);
    }

    private void menuViewLevelSector_actionPerformed(ActionEvent e)
    {
        menuViewScopeAll.setEnabled(true);
        menuViewScopeDomain.setEnabled(true);
        menuViewScopeSector.setEnabled(false);
        menuViewScopeQuadrant.setEnabled(false);
        menuViewScopeSubsector.setEnabled(false);
        menuViewScopeAll.setSelected(true);
    }

    private void menuViewLevelQuadrant_actionPerformed(ActionEvent e)
    {
        menuViewScopeAll.setEnabled(true);
        menuViewScopeDomain.setEnabled(true);
        menuViewScopeSector.setEnabled(true);
        menuViewScopeQuadrant.setEnabled(false);
        menuViewScopeSubsector.setEnabled(false);
        menuViewScopeAll.setSelected(true);
    }

    private void menuViewLevelSubsector_actionPerformed(ActionEvent e)
    {
        menuViewScopeAll.setEnabled(true);
        menuViewScopeDomain.setEnabled(true);
        menuViewScopeSector.setEnabled(true);
        menuViewScopeQuadrant.setEnabled(true);
        menuViewScopeSubsector.setEnabled(false);
        menuViewScopeAll.setSelected(true);

    }

    private void menuViewLevelSystems_actionPerformed(ActionEvent e)
    {
        menuViewScopeAll.setEnabled(true);
        menuViewScopeDomain.setEnabled(true);
        menuViewScopeSector.setEnabled(true);
        menuViewScopeQuadrant.setEnabled(true);
        menuViewScopeSubsector.setEnabled(true);
        menuViewScopeAll.setSelected(true);
    }
*/
    private void groupSystem_actionPerformed(ActionEvent e)
    {
    }

    private void bOK_actionPerformed(ActionEvent e)
    {
        updated = true;
        this.setVisible(false);
    }

    private void bCancel_actionPerformed(ActionEvent e)
    {
        updated = false;
        this.setVisible(false);
    }

    public boolean isUpdated()
    {
        return updated;
    }

    public void stateChanged (ChangeEvent event)
    {
        Astrogation data = (Astrogation)event.getSource();
        setScopeEnabled(MapScope.ALL, data.isGroupPresent(GroupType.GROUP));
        setScopeEnabled(MapScope.DOMAIN, data.isGroupPresent(GroupType.DOMAIN));
        setScopeEnabled(MapScope.SECTOR, data.isGroupPresent(GroupType.SECTOR));
        setScopeEnabled(MapScope.QUADRANT, data.isGroupPresent(GroupType.QUADRANT));
        setScopeEnabled(MapScope.SUBSECTOR, data.isGroupPresent(GroupType.SUBSECTOR));
        
        setLevelEnabled(GroupType.DOMAIN, data.isGroupPresent(GroupType.DOMAIN));
        setLevelEnabled(GroupType.SECTOR, data.isGroupPresent(GroupType.SECTOR));
        setLevelEnabled(GroupType.QUADRANT, data.isGroupPresent(GroupType.QUADRANT));
        setLevelEnabled(GroupType.SUBSECTOR, data.isGroupPresent(GroupType.SUBSECTOR));
        setLevelEnabled(GroupType.SYSTEM, true);

        MapScope scope = null;
        if (data.isGroupPresent(GroupType.SUBSECTOR)) scope = MapScope.SUBSECTOR; 
        if (data.isGroupPresent(GroupType.QUADRANT)) scope = MapScope.QUADRANT; 
        if (data.isGroupPresent(GroupType.SECTOR)) scope = MapScope.SECTOR; 
        if (data.isGroupPresent(GroupType.DOMAIN)) scope = MapScope.DOMAIN; 
        if (data.isGroupPresent(GroupType.GROUP)) scope = MapScope.ALL; 
        setScope(scope);
        
        
    }
    
    public static void main (String[] args)
    {
        EditViewOptions.getInstance().setVisible(true);
    }
}