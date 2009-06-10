package stellar.map.layout;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.prefs.Preferences;


public class HexLine
{
    LongLineList longItem;
    boolean longSelected;
    ShortLineList shortItem1;
    ShortLineList shortItem2;
    ShortLineList shortItem3;
    boolean threeShortItems;
    int lineNumber;
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public HexLine()
    {
    }
    
    public HexLine (int lineNumber, boolean threeShortItems)
    {
        this.lineNumber = lineNumber;
        this.threeShortItems = threeShortItems;
    }
    
    public HexLine (HexLine old)
    {
        this.lineNumber = old.lineNumber;
        longItem = old.longItem;
        longSelected = old.longSelected;
        shortItem1 = old.shortItem1;
        shortItem2 = old.shortItem2;
        shortItem3 = old.shortItem3;
        threeShortItems = old.threeShortItems;
    }

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public void setLongSelected(boolean longSelected)
    {
        boolean oldLongSelected = longSelected;
        this.longSelected = longSelected;
        propertyChangeSupport.firePropertyChange(HexLineProperties.ISLONG.toString(), oldLongSelected, longSelected);
    }

    public boolean isLongSelected()
    {
        return longSelected;
    }
    public void setLongItem(LongLineList longItem)
    {
        LongLineList oldLongItem = longItem;
        this.longItem = longItem;
        propertyChangeSupport.firePropertyChange(HexLineProperties.LONG_OPTION.toString(), oldLongItem, longItem);
    }

    public LongLineList getLongItem()
    {
        return longItem;
    }

    public void setShortItem1(ShortLineList shortItem1)
    {
        ShortLineList oldShortItem1 = shortItem1;
        this.shortItem1 = shortItem1;
        propertyChangeSupport.firePropertyChange(HexLineProperties.SHORT_OPTION1.toString(), oldShortItem1, shortItem1);
    }

    public ShortLineList getShortItem1()
    {
        return shortItem1;
    }

    public void setShortItem2(ShortLineList shortItem2)
    {
        ShortLineList oldShortItem2 = shortItem2;
        this.shortItem2 = shortItem2;
        propertyChangeSupport.firePropertyChange(HexLineProperties.SHORT_OPTION2.toString(), oldShortItem2, shortItem2);
    }

    public ShortLineList getShortItem2()
    {
        return shortItem2;
    }


    public void setShortItem3(ShortLineList shortItem3)
    {
        ShortLineList oldShortItem3 = shortItem3;
        this.shortItem3 = shortItem3;
        propertyChangeSupport.firePropertyChange(HexLineProperties.SHORT_OPTION3.toString(), oldShortItem3, shortItem3);
    }

    public ShortLineList getShortItem3()
    {
        return shortItem3;
    }
    
    public void load (Preferences layoutPrefs)
    {
        Preferences node = layoutPrefs.node ("line" + String.valueOf(lineNumber));
        
        int index;
        setLongSelected(node.getBoolean(HexLineProperties.ISLONG.toString(), true));
        setThreeShortItems(node.getBoolean (HexLineProperties.ISTHREESHORT.toString(), true));
        
        index = node.getInt(HexLineProperties.LONG_OPTION.toString(), 0);
        setLongItem (LongLineList.values()[index]);
        
        index = node.getInt(HexLineProperties.SHORT_OPTION1.toString(), 0);
        setShortItem1(ShortLineList.values()[index]);
        
        index = node.getInt (HexLineProperties.SHORT_OPTION2.toString(), 0);
        setShortItem2 (ShortLineList.values()[index]);
        
        index = node.getInt(HexLineProperties.SHORT_OPTION3.toString(), 0);
        setShortItem3 (ShortLineList.values()[index]);
        
    }
    
    public void save (Preferences layoutPrefs)
    {
        Preferences node = layoutPrefs.node ("line" + String.valueOf(lineNumber));
        node.putBoolean(HexLineProperties.ISLONG.toString(), longSelected);
        node.putBoolean(HexLineProperties.ISTHREESHORT.toString(), threeShortItems);
        node.putInt (HexLineProperties.LONG_OPTION.toString(), longItem.ordinal());
        node.putInt (HexLineProperties.SHORT_OPTION1.toString(), shortItem1.ordinal());
        node.putInt(HexLineProperties.SHORT_OPTION2.toString(), shortItem2.ordinal());
        node.putInt (HexLineProperties.SHORT_OPTION3.toString(), shortItem3.ordinal());
    }

    public void setThreeShortItems(boolean threeShortItems)
    {
        boolean oldThreeShortItems = threeShortItems;
        this.threeShortItems = threeShortItems;
        propertyChangeSupport.firePropertyChange("ThreeShortItems", oldThreeShortItems, threeShortItems);
    }

    public boolean isThreeShortItems()
    {
        return threeShortItems;
    }

    public void setLineNumber(int lineNumber)
    {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber()
    {
        return lineNumber;
    }
}
