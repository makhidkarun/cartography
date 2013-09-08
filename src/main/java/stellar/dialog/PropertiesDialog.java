package stellar.dialog;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class PropertiesDialog extends JDialog 
{
	private JOptionPane jOptionPane;
	private Hashtable htInputFields;

	public PropertiesDialog(Frame parent, String[] fields, String[] types, String[] values, String title, boolean bModal)
	{
		super(parent, title, bModal);
		htInputFields = new Hashtable();
		final Object[] buttonLabels = { "OK", "Cancel" };
		Object[] panelContents = new Object[(fields.length * 2)];
		int objectCount = 0;
		for(int iter = 0; iter < fields.length; iter++)
		{
			String fieldName = fields[iter];
			String fieldType = types[iter];
			Object fieldComponent;
			if(fieldType.equals("text"))
			{
				fieldComponent = new JTextField(3);
				if(values[iter] != null && values[iter].length() > 0)
				{
					((JTextField)(fieldComponent)).setText(values[iter]);
				}
			}
			else if(fieldType.equals("bool"))
			{
				fieldComponent = new JCheckBox();
				if(values[iter] != null)
				{
					((JCheckBox)(fieldComponent)).setSelected(values[iter] == "true");
				}
			}
			else if(fieldType.equals("combo"))
			{
				fieldComponent = new JComboBox();
				if(values[iter] != null)
				{
					StringTokenizer stParse = new StringTokenizer(values[iter], ",", false);
					while(stParse.hasMoreTokens())
					{
						((JComboBox)(fieldComponent)).addItem(stParse.nextToken());
					}
				}
			}
			else
			{
				fieldComponent = new JTextField(3);
			}
			htInputFields.put(fieldName, fieldComponent);
			panelContents[objectCount] = fieldName; // Translatrix.getTranslationString(fieldName);
			panelContents[objectCount + 1] = fieldComponent;
			objectCount += 2;
		}
		jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);

		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		jOptionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				String prop = e.getPropertyName();
				if(isVisible() && (e.getSource() == jOptionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
				{
					Object value = jOptionPane.getValue();
					if(value == JOptionPane.UNINITIALIZED_VALUE)
					{
						return;
					}
					if(value.equals(buttonLabels[0]))
					{
						setVisible(false);
					}
					else
					{
						setVisible(false);
					}
				}
			}
		});
		this.pack();
	}

	public PropertiesDialog(Frame parent, String[] fields, String[] types, String title, boolean bModal)
	{
		this(parent, fields, types, new String[fields.length], title, bModal);
	}

	public String getFieldValue(String fieldName)
	{
		Object dataField = htInputFields.get(fieldName);
		if(dataField instanceof JTextField)
		{
			return ((JTextField)dataField).getText();
		}
		else if(dataField instanceof JCheckBox)
		{
			if(((JCheckBox)dataField).isSelected())
			{
				return "true";
			}
			else
			{
				return "false";
			}
		}
		else if(dataField instanceof JComboBox)
		{
			return (String)(((JComboBox)dataField).getSelectedItem());
		}
		else
		{
			return (String)null;
		}
	}

	public String getDecisionValue()
	{
		return jOptionPane.getValue().toString();
	}
}