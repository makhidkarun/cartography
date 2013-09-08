package stellar.dialog;
import stellar.data.Astrogation;
import stellar.data.HexID;
import stellar.map.HexIcons;
import stellar.map.MapLabel;
import stellar.map.MapScale;
import stellar.swing.ColorTableCellRenderer;
import stellar.swing.LinksTableModel;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EditLink extends JDialog 
{
    private Color backgroundColor = Color.WHITE;
    private Color foregroundColor = Color.BLACK;
    private HashSet links;
    private int lastRow;
    
    private BorderLayout borderLayout1 = new BorderLayout();
    private ButtonPanel buttonPanel = new ButtonPanel();

    private JTable linkTable;
    private LinksTableModel linkTableModel;
    private SelectionListener selectListener = new SelectionListener();
 
    private JSplitPane splitPane;
    private JScrollPane linkScrollPane = new JScrollPane();
    private JScrollPane mapScrollPane = new JScrollPane();

    private JButton bLinkColorSelector = new JButton();
    private JTextField linkName = new JTextField();

    private MapLabel mapLabel;
    private HexIcons mapHex = new HexIcons (false);
    

    /*    
    private JPanel jPanel1 = new JPanel();
    private JLabel jLabel1 = new JLabel();
    private JComboBox linkGroup = new JComboBox();
    private JPanel mapPane = new JPanel();
    private JLabel jLabel2 = new JLabel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    */
    public EditLink()
    {
        this(null, "", false);
    }

    public EditLink (Astrogation starData)
    {
        this (null, "", false);

        Iterator i;

        links = new HashSet (starData.getLinkCount());

        mapHex.setMapData(starData);
        mapHex.setSize(5,5);
        mapHex.setScale(MapScale.SCALE_4);
        mapLabel = new MapLabel (mapHex);

        i = starData.getLinks();
        while (i.hasNext()) links.add (i.next());
        
        linkTableModel.setLinkData(links);
        linkTableModel.fireTableDataChanged();
        //linkTable.setRowSelectionInterval(0,0);
    }
    /**
     * 
     * @param modal
     * @param title
     * @param parent
     */
    public EditLink(Frame parent, String title, boolean modal)
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

    private void jbInit() throws Exception
    {
        //this.setSize(new Dimension(400, 300));
        this.getContentPane().setLayout(borderLayout1);
        this.setTitle("Edit Jump Links");

        linkTableModel = new LinksTableModel(null);
        linkTable = new JTable (linkTableModel);
        linkTable.setDefaultRenderer(Color.class, new ColorTableCellRenderer());
        linkTable.getSelectionModel().addListSelectionListener(selectListener);
        
        linkScrollPane.getViewport().add(linkTable);
            
        buttonPanel.addOKActionListener(new OKActionListener());
        buttonPanel.addCancelActionListener(new CancelActionListener());
        
        mapScrollPane.getViewport().add (mapLabel);
        mapScrollPane.setPreferredSize(mapLabel.getPreferredSize());
/*

        linkName.setText("Link Name");
        jPanel1.setLayout(gridBagLayout1);
        jLabel1.setText("Link Name");
        bLinkColorSelector.setText("Link Color");
        bLinkColorSelector.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bLinkColorSelector_actionPerformed(e);
                }
            });
        
        
        mapHex.setMapScale(HexIcons.SCALE_3);
        mapHex.setMapSize(1, 1);
        
        mapPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        mapPane.setLayout (borderLayout2);
        mapPane.setPreferredSize(new Dimension(100, 126));
        jLabel2.setText("Link Group");
        mapPane.add(mapHex, BorderLayout.CENTER);

        linkGroup.addItem ("(none)");
        jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
        jPanel1.add(linkName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
        jPanel1.add(jLabel2, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
        jPanel1.add(linkGroup, new GridBagConstraints(4, 0, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 52), 0, 0));
        jPanel1.add(bLinkColorSelector, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(56, 97, 0, 0), 0, 0));
        jPanel1.add(mapPane, new GridBagConstraints(3, 1, 2, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 0, 69, 0), 0, 0));
        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
  */      
        splitPane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT, linkScrollPane, mapScrollPane);
        this.getContentPane().add (splitPane, BorderLayout.CENTER);
        //this.getContentPane().add (mapScrollPane, BorderLayout.EAST);
        //this.getContentPane().add (linkScrollPane, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.pack();
    }
    
    private void bCancel_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
    }
    
    private void bOK_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
    }

    private void bLinkColorSelector_actionPerformed(ActionEvent e)
    {
        Color color;
        color = JColorChooser.showDialog(this, "Select link color", backgroundColor);
        if (color != null)
            setLinkColor (color);
    }
    
    private void tSelection_actionPerformed (ListSelectionEvent e)
    {
        // if the event is generated by changing a row, process it. 
        if (e.getSource() == linkTable.getSelectionModel())
        {
            int first = e.getFirstIndex();
            int last = e.getLastIndex();
            if (first != lastRow) lastRow = first; 
            else if (last != lastRow) lastRow = last;
            HexID start = (HexID) linkTableModel.getValueAt(lastRow, 1);
            HexID end = (HexID) linkTableModel.getValueAt(lastRow, 2);
            mapHex.setMapOffset(Math.min (start.y, end.y), Math.min (start.x, end.x));
            mapHex.setSize(5,5);
            this.repaint();
        }
    }
    public void setLinkColor (Color color)
    {
        backgroundColor = color;
        foregroundColor = new Color ((~color.getRed()) & 0xff, (~color.getGreen()) & 0xff, (~color.getBlue()) & 0xff);
        bLinkColorSelector.setBackground(color);
        bLinkColorSelector.setForeground(foregroundColor);
    }
/*
    public void setLink (Links links)
    {
        this.link = links;
        linkName.setText(links.getValue());
        //linkGroup = links.getLinkTypeName();
        setLinkColor (links.getColor());
    }
*/    

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

    private class SelectionListener implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent e) 
        {
            tSelection_actionPerformed (e);
        }
    }
}