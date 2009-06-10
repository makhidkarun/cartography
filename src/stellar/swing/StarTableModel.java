package stellar.swing;
/* $Id: StarTableModel.java,v 1.6 2008/12/12 17:47:13 tjoneslo Exp $ */
import stellar.data.Astrogation;
import stellar.data.StarSystem;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
/**
 * StarTableModel displays the list of stars (systems) in a file as a table of 
 * data. 
 * 
 * @author Thomas Jones-Low
 * @version $Revision: 1.6 $
 */
public class StarTableModel extends AbstractTableModel implements AstrogationChangeListener
{
    String titles[] = new String [] 
    {"Name", "Hex", "UWP", "Base", "Remarks", "Zone", "PBG", "Al", "Primary", "Companion", "Tertiary"};
    //Object[][] data;
    Astrogation starData;

    public StarTableModel (Astrogation stars)
    {
        super();
        starData = stars;
    }
    
    public StarTableModel()
    {
    }

    public void stateChanged (ChangeEvent e) { setMapData ((Astrogation)e.getSource()); } 
    public int getRowCount () { return starData == null ? 0 : starData.getSystemCount(); }
    public int getColumnCount() { return titles.length; }
    public String getColumnName (int c) { return titles[c]; }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        if (columnIndex == 3) return true; 
        return false;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        StarSystem system;
        char cValue;
        String value;
        
        if (starData == null) return;

        value = (String)aValue;
        system = starData.getSystem(rowIndex);
        
        if (columnIndex == 3) 
        {
            if (value.length() == 0) cValue = ' ';
            else cValue = value.charAt(0);
            system.setBase(cValue);
        }
        fireTableCellUpdated(rowIndex,columnIndex);
    }

    public Object getValueAt (int r, int c)
    {
        if (starData == null) return null;
        StarSystem current = starData.getSystem(r);
        switch (c)
        {
            case 0: return current.getName();
            case 1: return current.getLocation().toString();
            case 2: return current.getPlanet().toString();
            case 3: return String.valueOf (current.getBase());
            case 4: return (current.getTradeCodes() == null ? "" : 
                    new String (current.getTradeCodes())) + 
                        (current.getRemarks() == null ? "" : 
                        new String (current.getRemarks()));
            case 5: return String.valueOf(current.getZone());
            case 6: return String.valueOf (current.getMultiplier()) + String.valueOf(current.getBelts()) + String.valueOf(current.getGiants());
            case 7: return new String (current.getPolity());
            case 8: return current.getPrimary();
            case 9: return current.getCompanion();
            case 10: return current.getTertiary();
            default: return null;
        }
    }
    public void setMapData (Astrogation stars)
    {
        starData = stars;
        /* dump data into the object array as strings */
        fireTableDataChanged();
    }
}
