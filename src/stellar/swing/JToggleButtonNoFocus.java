package stellar.swing;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;

public class JToggleButtonNoFocus extends JToggleButton 
{
	public JToggleButtonNoFocus()                       { super();           this.setRequestFocusEnabled(false); }
	public JToggleButtonNoFocus(Action a)               { super(a);          this.setRequestFocusEnabled(false); }
	public JToggleButtonNoFocus(Icon icon)              { super(icon);       this.setRequestFocusEnabled(false); }
	public JToggleButtonNoFocus(String text)            { super(text);       this.setRequestFocusEnabled(false); }
	public JToggleButtonNoFocus(String text, Icon icon) { super(text, icon); this.setRequestFocusEnabled(false); }

	public boolean isFocusable()
	{
		return false;
	}

	// deprecated in 1.4 with isFocusable(), left in for 1.3 compatibility
	@Deprecated public boolean isFocusTraversable()
	{
		return false;
	}
}