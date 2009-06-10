package stellar.swing;
import stellar.data.HexID;
import stellar.data.Links;
import java.awt.Color;
import java.util.HashSet;
import javax.swing.table.AbstractTableModel;

public class LinksTableModel extends AbstractTableModel 
{
    String titles[] = {"Description", "Start", "End", "Distance", "Color (F2 to edit)"};
    Class types [] = new Class [] { String.class, HexID.class, HexID.class, String.class, Color.class };
    Links [] data;
    HashSet links;
    
    public LinksTableModel(HashSet links)
    {
        this.links = links;
    }
    
    public int getRowCount () { return links == null ? 0 : links.size(); } 
    public int getColumnCount() { return titles.length; }
    public String getColumnName (int c) { return titles[c]; }
    public Class getColumnClass (int c) { return types[c]; }
    public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
    
    public Object getValueAt (int r, int c)
    {
        Links link;
        if (data == null) return null;
        link = data[r];        
        switch (c)
        {
            case 0: return link.getValue();
            case 1: return link.getHex(0);
            case 2: return link.getHex(1);
            case 3: return new Integer(HexID.distance (link.getHex(0), link.getHex(1)));
            case 4: return link.getColor();
        }
        return null;
    }

    public void setLinkData (HashSet links)
    {
        this.links = links;
        data = new Links [links.size()];
        links.toArray(data);
        fireTableDataChanged();
    }

}