/*
 * $Id$
 * Copyright 2005 Softstart Services, Inc. All rights reserved. 
 */

package stellar.swing;

import java.util.Collection;
import java.util.Iterator;
import javax.swing.JComboBox;

/**
 * This class extends <code>JComboBox</code> to allow creation of a list
 * based upon a <code>ListIterator</code>.
 * 
 * @see java.util.ListIterator
 * @see javax.swing.JComboBox
 * @author Thomas Jones-Low
 * @version $Id$
 */
public class JComboTable extends JComboBox 
{
    /**
     * Default Constructor.
     */
    public JComboTable()
    {
    }
    /**
     * Adds items to the <code>JComboBox</code> from the ListIterator. If you have
     * a collection of items (an array, a <code>ArrayList</code>, or 
     * <code>HashSet</code> for example), you can now add all of the items in
     * the collection with one statement. This was a refactoring change to 
     * several dialogs.
     * 
     * @param itemList Collection of items to add to the JComboBox
     */
    public void addItems (Iterator itemList)
    {
        while (itemList.hasNext()) this.addItem(itemList.next());
    }
    
    public void addItems (Collection list)
    {
        addItems(list.iterator());
    }
    
    /**
     * Sets the selected JComboBox item based upon the first character. This
     * was added because the Stellar application has many data items which use
     * a single character "code" key and a longer description value. The 
     * <code>toString</code> for these data return in the form of 
     * <code>[Code] - [description]</code>. When loading up a new set of data,
     * the process can now select the current value based upon the code. 
     * 
     * @param item character key "code" to select
     */
    public void setSelectedItem (char item)
    {
        int i;
        Object x;
        
        for (i = 0; i < this.getItemCount(); i++)
        {   
            x = this.getItemAt(i);
            if (x.toString().charAt(0) == item)
            {
                this.setSelectedIndex(i);
                break;
            }
        }
    }
    
    /**
     * Set the selected JComboBox item based upon the initial string. Sets the
     * selected item based upon matching the inital string of the in the <code>JComboBox</code>
     * against the parameter. 
     * @param item String of character to match
     */
    public void setSelectedItem (String item)
    {
        int i;
        Object x;
        
        for (i = 0; i < this.getItemCount(); i++)
        {   
            x = this.getItemAt(i);
            if (x.toString().startsWith(item))
            {
                this.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Returns the first character of the selected item text in the JComboBox. 
     * This is a get method to <code>setSelectedItem (char)</code>. It assumes
     * the key "code" and value description setup, and the process needs only 
     * the specific selected code value. 
     * 
     * @return first character of the selected item.
     */
    public char getSelectedCode () { return getSelectedItem().toString().charAt(0); } 
}
