/*
 * $Id$
 * Copyright 2005 Softstart Services, Inc. All rights reserved. 
 */
package stellar.swing;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Extends JButton to automatically make the button not focusable. This is 
 * designed for toolbar buttons, which never need to have the focus. It removes
 * having to call explicitly <code>setRequestFocusEnabled(false)</code> for 
 * every toobar button. 
 * @author Thomas Jones-Low
 * @version $Id$
 */
public class JButtonNoFocus extends JButton
{
	public JButtonNoFocus()                       { super();           this.setRequestFocusEnabled(false); }
	public JButtonNoFocus(Action a)               { super(a);          this.setRequestFocusEnabled(false); }
	public JButtonNoFocus(Icon icon)              { super(icon);       this.setRequestFocusEnabled(false); }
	public JButtonNoFocus(String text)            { super(text);       this.setRequestFocusEnabled(false); }
	public JButtonNoFocus(String text, Icon icon) { super(text, icon); this.setRequestFocusEnabled(false); }

    /**
     * Returns whether this Component can be focused. Always returns false.
     *
     * @return false, as this button does not want focus. 
     * @see java.awt.Component#setFocusable
     * @since 1.4
     */
	public boolean isFocusable()
	{
		return false;
	}

    /**
     * Returns whether this <code>Component</code> can become the focus
     * owner. Always returns false. 
     *
     * @return false, as this button does not want focus.
     * @see java.awt.Component#setFocusable
     * @since JDK1.1
     * @deprecated As of 1.4, replaced by <code>isFocusable()</code>.
     */
 	@Deprecated public boolean isFocusTraversable()
	{
		return false;
	}

    /**
     * Override the setAction command in the JButton class. This version removes
     * the button text. 
     * @param a action to set, which determines the look/feel and handler.
     */
    public void setAction(Action a) {
        super.setAction (a);
        setText(null);
    }
}
