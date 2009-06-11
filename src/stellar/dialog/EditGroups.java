/*
 * $Id$
 * Copyright 2006 by Softstart Services Inc. 
 */

package stellar.dialog;
import stellar.data.Astrogation;
import stellar.data.GroupRecord;
import stellar.data.HexID;
import stellar.map.MapLabel;
import stellar.map.SquareIcons;
import stellar.data.GroupType;
import stellar.map.MapScale;
import stellar.swing.HTMLEditPanel;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JTree;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;
import org.jibx.runtime.JiBXException;
import javax.swing.JSpinner;
import java.awt.event.FocusEvent;

/**
 * The EditGroups dialog allows users to create, view and edit the groups of 
 * stars in the map. Traveller defines a set of groups: domain, sector, quadrant,
 * and subsectors, each nested within each other. Cartrogrpher adds a generic 
 * "group" group to allow users to define their own groups as needed. 
 * <BR>
 * TODO: 
 * <UL>
 * <LI>The New/Update button combination does not work. It's confusing and 
 * implemented wrong here. We need to have the valueChanges()/bOKActionPerformed()
 * do the Update and make the update button go away. 
 * <LI>The New button really needs to actually finish the creation of the new group 
 * and put it entirely  in the list. 
 * <LI>Need a delete button to remove groups. 
 * </UL>
 */
public class EditGroups extends JDialog implements TreeSelectionListener
{
    private Astrogation data;
    private MutableTreeNode root;
    private boolean dataChanged = false;
    
    private BorderLayout borderLayout1 = new BorderLayout();

    private JScrollPane treeView = new JScrollPane();
    private JTree groupTree;

    private ButtonPanel buttonPanel = new ButtonPanel();
    private JButton bNewGroup = new JButton();
    private JButton bUpdate = new JButton();

    private HTMLEditPanel commentPanel = new HTMLEditPanel();

    private JTabbedPane jTabbedPane1 = new JTabbedPane();

    private JPanel infoPanel = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel jLabel1 = new JLabel();
    private JTextField groupName = new JTextField();
    private JLabel jLabel2 = new JLabel();
    private JComboBox groupType = new JComboBox();
    private JLabel jLabel3 = new JLabel();
    private JTextField groupLocation = new JTextField();
    private JLabel jLabel4 = new JLabel();

    private JScrollPane mapPane = new JScrollPane();
    private MapLabel map;
    private SquareIcons squareMap = new SquareIcons (false);
    private JLabel jLabel5 = new JLabel();
    private JSpinner extentX = new JSpinner(new SpinnerNumberModel (1, 1, 99, 1));
    private JLabel jLabel6 = new JLabel();
    private JSpinner extentY = new JSpinner(new SpinnerNumberModel (1, 1, 99, 1));
    
    public EditGroups(Astrogation data)
    {
        try
        {
            this.data = data;
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
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
        this.getContentPane().setLayout(borderLayout1);
        infoPanel.setLayout(gridBagLayout1);

        buttonPanel.addOKActionListener(new OKActionListener());
        buttonPanel.addCancelActionListener(new CancelActionListener());

        bNewGroup.setText("New");
        bNewGroup.setMnemonic('N');
        bNewGroup.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bNewGroup_actionPerformed(e);
                }
            });
        bUpdate.setText("Update");
        bUpdate.setMnemonic('U');
        bUpdate.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bUpdate_actionPerformed(e);
                }
            });

        buttonPanel.add (bNewGroup);
        //buttonPanel.add (bUpdate);
        
        jLabel1.setText("Name");
        jLabel2.setText("Type");
        jLabel4.setText("Location");
        jLabel5.setText("Extent X");
        jLabel6.setText("Extent Y");
        groupLocation.setText("0000");
        groupLocation.setHorizontalAlignment(JTextField.TRAILING);
        groupLocation.setColumns(6);
        groupLocation.setMinimumSize(groupLocation.getPreferredSize());

        groupType.addItem(GroupType.DOMAIN);
        groupType.addItem(GroupType.SECTOR);
        groupType.addItem(GroupType.QUADRANT);
        groupType.addItem(GroupType.SUBSECTOR);
        groupType.addItem(GroupType.GROUP);
        groupType.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    groupType_actionPerformed(e);
                }
            });

        infoPanel.add(jLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
        infoPanel.add(groupName, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
        infoPanel.add(jLabel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        infoPanel.add(groupType, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        infoPanel.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
        infoPanel.add(groupLocation, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        infoPanel.add(jLabel4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        infoPanel.add(jLabel5, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        infoPanel.add(extentX, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        infoPanel.add(jLabel6, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        infoPanel.add(extentY, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        /*
        map.setMapStyle(HexLabel.STYLE_SQUARE);
        map.setMapData(data);
        map.setMapScale (MapIcon.SCALE_5);
        map.setMapSize(4,4);
        map.setHexLayout(MapIcon.SCALE_5, EditOptions.getInstance().getScaleLayout(MapIcon.SCALE_5));
        map.setLevel(MapIcon.GROUP_DOMAIN);
        */
        squareMap.setMapData(data);
        squareMap.setScale(MapScale.SCALE_5);
        squareMap.setSize(4,4);
        squareMap.setLayout(MapScale.SCALE_5, EditOptions.getInstance().getScaleLayout(MapScale.SCALE_5));
        squareMap.setLevel(GroupType.DOMAIN);
        map = new MapLabel (squareMap);
        groupLocation.addFocusListener(new java.awt.event.FocusAdapter()
            {
                public void focusLost(FocusEvent e)
                {
                    groupLocation_focusLost(e);
                }
            });
        groupName.addFocusListener(new java.awt.event.FocusAdapter()
            {
                public void focusLost(FocusEvent e)
                {
                    groupName_focusLost(e);
                }
            });
        mapPane.getViewport().add(map, null);

        if (data != null && data.getGroup(0) != null)
        {
            createGroupTree();
            groupTree = new JTree (root);
            groupTree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
            groupTree.addTreeSelectionListener(this);
        
            treeView.getViewport().add(groupTree, null);
            this.getContentPane().add(treeView, BorderLayout.WEST);
            groupTree.setSelectionRow(0);
        }

        jTabbedPane1.addTab("Info", infoPanel);
        jTabbedPane1.addTab("Comments", commentPanel);
        jTabbedPane1.addTab("Map", mapPane);

        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
        this.setTitle("Edit Groups");
        this.pack();
        
        if (groupTree != null) { valueChanged (null); }
    }

    public void valueChanged(TreeSelectionEvent e) 
    {
        MutableTreeNode node = (MutableTreeNode)groupTree.getLastSelectedPathComponent();
        
        if (node == null) return;
        
        GroupRecord g = (GroupRecord)node;
        groupType.setSelectedItem(g.getType());
        groupName.setText(g.getName());
        groupLocation.setText(g.getLocation().toString());
        commentPanel.setDocument(g.getComment());
        squareMap.setLevel (g.getType());
        groupTypeSetExtentsEnabled();
        //squareMap.redrawMap();
        map.redrawMap();
    }

    private void bOK_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
    }

    private void bCancel_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
    }

    private void bNewGroup_actionPerformed(ActionEvent e)
    {
        jTabbedPane1.setSelectedIndex(0);
        groupType.setSelectedIndex(0);
        groupName.setText("");
        groupLocation.setText("");
        commentPanel.newDocument();
        GroupRecord g = new GroupRecord();
        g.setProvider (EditOptions.getInstance().getUserProvider());
        if (groupTree == null) {}
        else
        {
            data.addGroup(g);
        }
    }
    private void bUpdate_actionPerformed (ActionEvent e)
    {
        dataChanged = true;
        GroupRecord g;
        if (groupTree == null)
        {
            g = new GroupRecord();
            g.setProvider(EditOptions.getInstance().getUserProvider());
        }
        else
        {
            g = (GroupRecord) groupTree.getLastSelectedPathComponent();
        }
        g.setType((GroupType)groupType.getSelectedItem());
        
        if ((g.getLocation() == null) ||
            !(groupLocation.getText().equals(g.getLocation().toString())))
        {
            g.setLocation(new HexID (groupLocation.getText()));
            g.getLocation().setHexType(GroupType.GROUP);
            g.getLocation().setHexGroup (g.getParentName());
        }
    
        if ((g.getName() == null) ||
            !(groupName.getText().equals (g.getName())))
        {
            g.setName(groupName.getText());
        }
        
        if (g.getKey() == null)
        {
            g.setKey(g.getName().substring(0,4).toLowerCase() + "." + g.getLocation());
        }
        
        g.setComment(commentPanel.getDocument());
        if (groupTree == null)
        {
            try
            {
                data = new Astrogation (EditOptions.getInstance().getExternalRefsFileName());
            }
            catch (IOException ex) { ex.printStackTrace(); }
            catch (JiBXException ex) { ex.printStackTrace(); } 
            
            data.addGroup(g);
            root = g;
            groupTree = new JTree (root);
            groupTree.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
            groupTree.addTreeSelectionListener(this);
        
            treeView.getViewport().add(groupTree, null);
            this.getContentPane().add(treeView, BorderLayout.WEST);
            groupTree.setSelectionRow(0);
            this.pack();
        }
    }

    private void groupType_actionPerformed(ActionEvent e)
    {
        GroupRecord g;
        
        if (groupTree != null)
        {
            g = (GroupRecord) groupTree.getLastSelectedPathComponent();
            g.setType((GroupType)groupType.getSelectedItem());
            extentX.setValue (new Integer (g.getExtentX()));
            extentY.setValue (new Integer (g.getExtentY()));
            groupTypeSetExtentsEnabled();
        }
    }
    
    private void groupTypeSetExtentsEnabled ()
    {
        if (groupType.getSelectedItem().equals(GroupType.GROUP))
        {
            extentX.setEnabled(true);
            extentY.setEnabled(true);
        }
        else
        {
            extentX.setEnabled(false);
            extentY.setEnabled(false);
        }
    }
    
    private void createGroupTree ()
    {
        for (int i = 0; i < data.getGroupCount(); i++)
        {
            GroupRecord g = data.getGroup(i);
            /*
             * Look through all the groups to find the parent record of this
             * group. 
             */
            if (g.getParentName() != null) 
            {
                for (int j = 0; j < data.getGroupCount(); j++)
                {
                    GroupRecord parent = data.getGroup(j);
                    if (g.getParentName().equals (parent.getName()))
                    {
                        if (parent.getIndex(g) < 0)
                        {
                            parent.insert (g, parent.getChildCount());
                        }
                        break;
                    }
                }
            }
        }
        /*
         * find the root of the group list tree.
         */
        root = data.getGroup(0);
        while (root.getParent() != null)
        {
            root = (MutableTreeNode)root.getParent();
        }

    }

    private void groupName_focusLost(FocusEvent e)
    {
        GroupRecord g = (GroupRecord) groupTree.getLastSelectedPathComponent();
        if (!(groupName.getText().equals (g.getName()))) g.setName(groupName.getText());
    }

    private void groupLocation_focusLost(FocusEvent e)
    {
        GroupRecord g = (GroupRecord) groupTree.getLastSelectedPathComponent();
        if ((g.getLocation() == null) ||
            !(groupLocation.getText().equals(g.getLocation().toString())))
        {
            g.setLocation(new HexID (groupLocation.getText()));
            g.getLocation().setHexType(GroupType.GROUP);
            g.getLocation().setHexGroup (g.getParentName());
        }
    }
}
