package stellar.swing;
import java.util.Vector;
import javax.swing.JComboBox;

/**
 * This is an update to the JComboBox that forces the combo box not to be in the
 * Java focus list. 
 * @author Thomas Jones-Low
 * @version $Revision: 1.5 $
 */
public class JComboBoxNoFocus extends JComboBox 
{
	public JComboBoxNoFocus(Vector vc) { super(vc);  this.setRequestFocusEnabled(false); }
	public JComboBoxNoFocus()          { super();    this.setRequestFocusEnabled(false); }

	@Override public boolean isFocusable()
	{
		return false;
	}

	// deprecated in 1.4 with isFocusable(), left in for 1.3 compatibility
	@Deprecated public boolean isFocusTraversable()
	{
		return false;
	}
}
