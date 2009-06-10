package stellar.map.layout;

import stellar.dialog.HexLinePanel;
import stellar.map.MapScale;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.beans.PropertyChangeSupport;

import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * HexLayout is a complete set of layout options for a single hex, collecting the
 * overall options for the hex with the selections for each of the (up to 5) lines.
 *
 * This also implements the PropertyChange listener support so any changes made to
 * the options or lines will be propigated upward to listeners.
 *
 * @author Thomas Jones-Low
 * @version $Revision$
 */
public class HexLayout implements PropertyChangeListener
{
    private HexOptions options;
    private ArrayList<HexLine> lines;
    private MapScale scale;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    
    public HexLayout()
    {
    }
    
    public HexLayout (HexLayout old)
    {
        scale = old.scale;
        options = new HexOptions (old.getOptions());
        lines = new ArrayList<HexLine> (old.getLineCount());
        for (int i = 0; i < old.getLineCount(); i++)
        {
            lines.add (new HexLine (old.getLine(i)));
        }
    }
    
    public HexLayout (int lineCount, MapScale scale)
    {
        this.scale = scale;
        options = new HexOptions ();
        options.addPropertyChangeListener(this);
        lines = new ArrayList<HexLine> (lineCount);
        for (int i = 0; i < lineCount; i++)
        {
            if (lineCount >= 3 && (i == 0 || i == lineCount -1))
                lines.add (new HexLine (i+1, false));
            else
                lines.add (new HexLine (i+1, true));
            lines.get(i).addPropertyChangeListener(this);
        }
    }
    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
    
    public void propertyChange (PropertyChangeEvent e)
    {
        propertyChangeSupport.firePropertyChange(e);
    }
    
    public HexOptions getOptions () { return options; }
    public HexLine    getLine (int index) { return lines.get(index); }
    public int        getLineCount () { return lines.size(); }

    public void setScale(MapScale scale)
    {
        MapScale oldScale = scale;
        this.scale = scale;
        propertyChangeSupport.firePropertyChange("Scale", oldScale, scale);
    }

    public MapScale getScale()
    {
        return scale;
    }
    
    public int getScaleOrdinal () { return scale.ordinal(); }
    
    public void load (Preferences layoutPrefs)
    {
        Preferences node = layoutPrefs.node("scale" + String.valueOf(scale.ordinal()));
        options.load (node);
        for (HexLine line : lines)
        {
            line.load(node);
        }
    }
    
    public void save (Preferences layoutPrefs)
    {
        Preferences node = layoutPrefs.node("scale" + String.valueOf(scale.ordinal()));
        options.save(node);
        for (HexLine line : lines)
        {
            line.save(node);
        }
        
    }
    
    public boolean isShortIndexUsed (ShortLineList index)
    {
        for (HexLine l : lines)
        {
            if (l.isLongSelected()) continue;
            if (l.getShortItem1() == index) return true;
            if (l.getShortItem2() == index) return true;
            if (l.isThreeShortItems() && l.getShortItem3() == index) return true;
        }
        return false;
    }
}
