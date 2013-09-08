package stellar.swing;

import java.util.Iterator;
import java.util.Collection;

import javax.swing.DefaultComboBoxModel;

public class StellarComboBoxModel extends DefaultComboBoxModel
{
    public StellarComboBoxModel()
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
        while (itemList.hasNext()) this.addElement(itemList.next());
    }
    
    public void addItems (Collection list)
    {
        for (Object x : list) this.addElement(x);
    }

    /**
     * Sets the selected ComboBoxModel item based upon the first character. This
     * was added because the Cartographer application has many data items which use
     * a single character "code" key and a longer description value. The 
     * <code>toString</code> for these data return in the form of 
     * <code>[Code] - [description]</code>. When loading up a new set of data,
     * the process can now select the current value based upon the code. 
     * 
     * @param item character key "code" to select
     */
    public void setSelectedItem (char item)
    {
        for (int i = 0; i < this.getSize(); i++)
        {
            if (this.getElementAt(i).toString().charAt(0) == item)
            {
                this.setSelectedItem(this.getElementAt(i));
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
        for (int i = 0; i < this.getSize(); i++)
        {
            if (this.getElementAt(i).toString().startsWith(item))
            {
                this.setSelectedItem(this.getElementAt(i));
                break;
            }
        }
    }
    
}
