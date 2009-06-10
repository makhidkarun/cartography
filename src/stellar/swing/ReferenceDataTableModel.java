package stellar.swing;

import java.awt.Color;
import javax.swing.table.AbstractTableModel;
import stellar.data.TableRecord;
import stellar.data.TableRowRecord;

public class ReferenceDataTableModel extends AbstractTableModel
{
    String titles[] = new String []
    {"Code", "Description", "Comment", "Color (F2 to edit)"};
    //Object [][] data;
    TableRecord table;
    Class types [] = new Class [] {String.class, String.class, String.class, Color.class};
    
    public ReferenceDataTableModel()
    {
    }
    
    public Object getValueAt (int r, int c)
    {
        TableRowRecord row;
        if (table == null) return null;
        row = table.getRecord(r);
        switch (c)
        {
            case 0: return row.getCode();
            case 1: return row.getValue();
            case 2: return row.getComment();
            case 3: return row.getColor();
        }
        return null;
    }
    public int getRowCount () { return table == null ? 0 : table.size(); } 
    public int getColumnCount() { return titles.length; }
    public String getColumnName (int c) { return titles[c]; }
    public Class getColumnClass (int c) { return types[c]; }

    public boolean isCellEditable(int rowIndex, int columnIndex) 
    { 
        if (columnIndex == 3) return false;
        else return true; 
    }
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) 
    { 
        TableRowRecord row;
        if (table == null) return;
        row = table.getRecord(rowIndex);
        switch (columnIndex)
        {
            case 0: row.setCode((String)aValue); break;
            case 1: row.setValue((String)aValue); break;
            case 2: row.setComment((String)aValue); break;
            case 3: row.setColor((String)aValue); break;
        }
        fireTableCellUpdated(rowIndex,columnIndex);
    }

    public void setData (TableRecord table)
    {
        this.table = table; 
        fireTableDataChanged();
    }
    public void insertRow( int index, TableRowRecord rowData)
    {
        table.add(index,rowData);
        fireTableRowsInserted(index, index);
    }
    
    public TableRowRecord getRow (int index) { return (TableRowRecord)table.get(index); } 
    // public void addRow (TableRowRecord rowData) (fireTableRowsInserted (index, index); } 
    // public void removeRow (int index) { fireTableRowsDeleted (index, index); } 
    
}