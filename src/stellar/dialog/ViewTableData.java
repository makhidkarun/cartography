package stellar.dialog;
import stellar.data.ProviderRecord;
import stellar.swing.AstrogationChangeListener;

import stellar.data.References;
import stellar.data.TableRecord;
import stellar.data.TableRowRecord;
import stellar.data.Astrogation;
import stellar.data.TableRecordKey;
import stellar.io.AccessXMLFile;
import stellar.swing.ReferenceDataTableModel;
import stellar.swing.ColorTableCellRenderer;
import stellar.swing.StellarComboBoxModel;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import javax.swing.*;
import javax.swing.event.*;

import org.jibx.runtime.*;


public class ViewTableData extends JDialog implements AstrogationChangeListener
{
    private static ViewTableData _instance;
    //private References globalReferences;
    //private References localReferences; 
    private Astrogation data;

    private BorderLayout borderLayout2 = new BorderLayout();

    // Button area
    private ButtonPanel buttonPanel = new ButtonPanel();
    private JButton bSaveData = new JButton();
    private JButton bNewTable = new JButton();
    private JButton bSwitchMode = new JButton();
    private JButton bCopyToLocal = new JButton();

    // Table Area
    private JScrollPane tableScrollPanel = new JScrollPane();
    private JTable tableData;
    private ReferenceDataTableModel tableModel;

    //Table selector area
    private JPanel jPanel3 = new JPanel();
    private BoxLayout boxLayout1;
    private JLabel jLabel1 = new JLabel();
    private JComboBox tableName = new JComboBox(new StellarComboBoxModel());
    //private StellarComboBoxModel tableNameModel = new StellarComboBoxModel();
    
    public ViewTableData()
    {
        this(null, "", false);
    }

    public ViewTableData(Frame parent, String title, boolean modal)
    {
        super(parent, title, modal);
        try
        {
            jbInit();
            //globalReferences = loadReferences();
            //if (globalReferences != null) tableName.addItems(globalReferences.tablesIterator());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public static synchronized ViewTableData getInstance()
    {
        if (_instance == null) _instance = new ViewTableData();
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
        this.getContentPane().setLayout(borderLayout2);
        this.setTitle("Edit Reference Data");

        // Setup the buttons and button panel
        buttonPanel.addOKActionListener(new OKActionListener());
        buttonPanel.addCancelActionListener(new CancelActionListener());

        bSaveData.setText("Save");
        bSaveData.setToolTipText("Save global reference data set to file");
        bSaveData.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bSaveData_actionPerformed(e);
                }
            });
        bNewTable.setText("New Table");
        bNewTable.setToolTipText("Create new reference table");
        bNewTable.setEnabled(false);
        bNewTable.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bNewTable_actionPerformed(e);
                }
            });
        bSwitchMode.setText("Local");
        bSwitchMode.setToolTipText("Switch to other reference data set");
        bSwitchMode.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bSwitchMode_actionPerformed(e);
                }
            });
        bCopyToLocal.setText("Copy");
        bCopyToLocal.setToolTipText("Copy global table to local reference set");
        bCopyToLocal.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bCopyToLocal_actionPerformed(e);
                }
            });
        buttonPanel.add (bSaveData);
        buttonPanel.add (bNewTable);
        buttonPanel.add (bSwitchMode);
        buttonPanel.add (bCopyToLocal);
        
        // Setup the table
        tableModel = new ReferenceDataTableModel();
        tableData = new JTable (tableModel);
        tableData.setDefaultRenderer(Color.class, new ColorTableCellRenderer());
        
        tableData.setSelectionBackground(new Color(181, 197, 240));
        tableData.setSelectionForeground(Color.black);
        tableData.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(MouseEvent e)
                {
                    tableData_mouseClicked(e);
                }
            });
        tableData.addKeyListener(new KeyAdapter()
            {
                public void keyPressed(KeyEvent e)
                {
                    tableData_keyPressed(e);
                }
            });
        tableScrollPanel.getViewport().add(tableData, null);

        //tableName.setModel(tableNameModel);
        tableName.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    tableName_actionPerformed(e);
                }
            });
        tableName.addItemListener(new ItemListener()
            {
                public void itemStateChanged(ItemEvent e)
                {
                    tableName_itemStateChanged (e);
                }
            });

        //Setup the table selector
        jLabel1.setText("Table Name");
        jLabel1.setSize(new Dimension(80, 19));
        jLabel1.setLabelFor(tableName);

        jLabel1.setFocusable(false);
        boxLayout1 = new BoxLayout (jPanel3, BoxLayout.LINE_AXIS);
        jPanel3.setLayout(boxLayout1);
        jPanel3.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        jPanel3.setFocusable(false);
        jPanel3.add(jLabel1, null);
        jPanel3.add(Box.createRigidArea(new Dimension(3, 25)), null);
        jPanel3.add(tableName, null);
        jPanel3.add(Box.createHorizontalGlue(), null);

        // put it all together
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.getContentPane().add(tableScrollPanel, BorderLayout.CENTER);
        this.getContentPane().add(jPanel3, BorderLayout.NORTH);
        this.pack();

    }

    private void bOK_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
    }

    private void bCancel_actionPerformed(ActionEvent e)
    {
        this.setVisible(false); 
    }

    private void bSaveData_actionPerformed(ActionEvent e)
    {
        if (data.getGlobalReferences() == null) return;
        Astrogation refData = new Astrogation (data.getGlobalReferences());
        File file = new File (EditOptions.getInstance().getWorkingDirName());
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save global reference data");
        chooser.setCurrentDirectory(file);
        int option = chooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            file = chooser.getSelectedFile();
            EditOptions.getInstance().setWorkingDirName(file.getParent());
            EditOptions.getInstance().setCurrentFileName(file.getAbsolutePath());
            String outputFile = file.getAbsolutePath();
            if (!outputFile.endsWith("xml"))
            {
                /* replace the file name with an xml file name */
                outputFile = outputFile + ".xml";
            }
            AccessXMLFile xmlFile = new AccessXMLFile(outputFile);
            xmlFile.setAstrogation(refData);
            try
            {
                xmlFile.write();
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog (this, ex.getMessage(), "Error: IO Exception", JOptionPane.ERROR_MESSAGE);                
            }
            catch (JiBXException ex)
            {
                JOptionPane.showMessageDialog (this, ex.getMessage(), "Error: JiBX XML writer Exception", JOptionPane.ERROR_MESSAGE);                
            }
        }
    }

    private void tableName_actionPerformed(ActionEvent e)
    {
        tableModel.setData((TableRecord)tableName.getSelectedItem());
    }

    private void tableName_itemStateChanged (ItemEvent e)
    {
        if (e.getStateChange() == ItemEvent.SELECTED)
        {
            tableModel.setData ((TableRecord)tableName.getSelectedItem());
        }
    }
    private void tableData_keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_INSERT)
        {
            TableRowRecord row = new TableRowRecord();
            ProviderRecord user = EditOptions.getInstance().getUserProvider();
            row.setProvider(user);
            tableModel.insertRow (tableData.getSelectedRow()+1, row);
            if (bSwitchMode.getText().equals("Local"))
            {
                if (!data.getGlobalReferences().containsProvider(user))
                    data.getGlobalReferences().addProvider(user);
            }                    
            else
            {
                if (!data.getLocalReferences().containsProvider(user))
                    data.getLocalReferences().addProvider(user);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_F2)
        {
            if (tableData.getSelectedColumn() == 3)
            {
                editRecordColor();
            }
        }
    }


    private void bSwitchMode_actionPerformed(ActionEvent e)
    {
        if (bSwitchMode.getText().equals("Local"))
        {
            setLocalData();
        }
        else if (bSwitchMode.getText().equals("Global"))
        {
            setGlobalData();
        }
    }
    
    public void stateChanged (ChangeEvent data)
    {
        setData ((Astrogation)data.getSource());
    }
    
    public void setData (Astrogation data) 
    { 
        this.data = data; 
        if (bSwitchMode.getText().equals ("Local"))
        {
            setGlobalData();
        }
        else if (bSwitchMode.getText().equals ("Global"))
        {
            setLocalData();
        }
    }
    public References getGlobalReferences()
    {
        /*
        if (data == null)
        {
            data = new Astrogation ();
        }
        if (data.getGlobalReferences() == null)
        {
            try {
                data.loadGlobalReferences(null);
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error: IO Exception", JOptionPane.ERROR_MESSAGE);
            }
            catch (JiBXException ex)
            {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error: JiBX IO Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
        */
        return (data == null) ? null : data.getGlobalReferences(); 
    }

    public References getLocalReferences() { return (data == null) ? null : data.getLocalReferences(); }
    
    public TableRecord getTableRecord (TableRecordKey name) { return (data == null) ? null : data.getTableRecord(name); }
/*
    public References getReferences ()
    {
        if (bSwitchMode.getText().equals("Global") && localReferences != null)
            return localReferences;
        else
            return globalReferences;
        
    }
*/    
    
    private void bCopyToLocal_actionPerformed(ActionEvent e)
    {
        CopyTableData copy = new CopyTableData (null, "", true);
        copy.setVisible(true);
        if (copy.getCopyOK()) 
        {
            References ref = data.getLocalReferences();
            TableRecord table = (TableRecord)tableName.getSelectedItem();
            
            //int index = ref.getTableIndex(table.getKey());
            if (copy.getCopyEntireTable())
            {
                if (copy.getReplaceLocalData())
                {
                    if (ref.containsTable(table))
                    {
                        ref.removeTable (table);
                    }
                    ref.addTable(table);
                }
                else /* Add data to the local data */
                {
                    if (ref.containsTable (table))
                    {
                        ref.getTable(table.getTableKey()).addAll(table);
                    }
                    else /* No table */
                    {
                        ref.addTable(table);
                    }
                }
            }
            else /* Copy only the reference data */
            {
                /* TODO: This needs the link between the References and the
                 * Data elements.
                 */
            }
        }
        //if (localReferences == null) localReferences = new References();
        //localReferences.addTable((TableRecord)tableName.getSelectedItem());
    }

    private void bNewTable_actionPerformed(ActionEvent e)
    {
        TableRecord table = new TableRecord();
        table.setProvider(EditOptions.getInstance().getUserProvider());
        //table.setKey();
        //table.setValue();
        if (bSwitchMode.getText().equals("Local"))
        {
            data.getGlobalReferences().addTable(table);
        }
        else
        {
            data.getLocalReferences().addTable(table);
        }
            
    }

    private void tableData_mouseClicked(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() >= 2 &&
            tableData.getSelectedColumn() == 3)
        {
            editRecordColor();
        }
    }
    
    private void editRecordColor ()
    {
        Color oldColor = (Color)tableModel.getValueAt(tableData.getSelectedRow(), tableData.getSelectedColumn());
        Color newColor = JColorChooser.showDialog(this, "Select " + tableName.getSelectedItem().toString() + " item color", oldColor);
        tableModel.getRow (tableData.getSelectedRow()).setColor(newColor);
        tableModel.fireTableCellUpdated(tableData.getSelectedRow(), tableData.getSelectedColumn());
    }
    
    private void setGlobalData()
    {
        if (data.getGlobalReferences() != null)
        {
            bSwitchMode.setText("Local");
            tableName.removeAllItems();
            ((StellarComboBoxModel)tableName.getModel()).addItems(data.getGlobalReferences().tablesIterator());
            //tableNameModel.removeAllElements();
            //tableNameModel.addItems(data.getGlobalReferences().tablesIterator());
            bCopyToLocal.setEnabled(true);
        }
    }
    
    private void setLocalData()
    {
        if (data.getLocalReferences() != null)
        {
            bSwitchMode.setText("Global");
            tableName.removeAllItems();
            ((StellarComboBoxModel)tableName.getModel()).addItems(data.getLocalReferences().tablesIterator());
            //tableNameModel.removeAllElements();
            //tableNameModel.addItems(data.getLocalReferences().tablesIterator());
            bCopyToLocal.setEnabled(false);
        }
    }
/*
 * The following code snippet shows how to detect when the user selects a table
 * row. By default, a table allows the user to select multiple rows - not columns
 * or individual cells - and the selected rows need not be next to each other.
 * Using the setSelectionMode method, the following code specifies that only
 * one row at a time can be selected.
table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
...
//Ask to be notified of selection changes.
ListSelectionModel rowSM = table.getSelectionModel();
rowSM.addListSelectionListener(new ListSelectionListener() {
    public void valueChanged(ListSelectionEvent e) {
        //Ignore extra messages.
        if (e.getValueIsAdjusting()) return;

        ListSelectionModel lsm =
            (ListSelectionModel)e.getSource();
        if (lsm.isSelectionEmpty()) {
            ...//no rows are selected
        } else {
            int selectedRow = lsm.getMinSelectionIndex();
            ...//selectedRow is selected
        }
    }
});
 */
}