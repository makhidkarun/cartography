package stellar.dialog;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class FontSelectorDialog extends JDialog implements ItemListener
{
	private Vector vcFontnames = null;
	private String fontName = new String();
	private String defaultText;


	private JComboBox jcmbFontlist;
	private JOptionPane jOptionPane;
	private JTextPane jtpFontPreview = new JTextPane();
    private String attribName;
    
	public FontSelectorDialog(Frame parent, String title, boolean bModal, String attribName, String demoText)
	{
		super(parent, title, bModal);

        this.attribName = attribName;
		if(demoText != null && demoText.length() > 0)
		{
			if(demoText.length() > 24)
			{
				defaultText = demoText.substring(0, 24);
			}
			else
			{
				defaultText = demoText;
			}
		}
		else
		{
			defaultText = "aAbBcCdDeEfFgGhH,.0123";
		}

        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {

		/* Obtain available fonts */
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		vcFontnames = new Vector(fonts.length - 5);
		for(int i = 0; i < fonts.length; i++)
		{
			if(!fonts[i].equals("Dialog") && 
                !fonts[i].equals("DialogInput") && 
                !fonts[i].equals("Monospaced") && 
                !fonts[i].equals("SansSerif") && 
                !fonts[i].equals("Serif"))
			{
				vcFontnames.add(fonts[i]);
			}
		}
		jcmbFontlist = new JComboBox(vcFontnames);
		jcmbFontlist.addItemListener(this);

		jtpFontPreview = new JTextPane();
		final HTMLEditorKit kitFontPreview = new HTMLEditorKit();
		final HTMLDocument docFontPreview = (HTMLDocument)(kitFontPreview.createDefaultDocument());
		jtpFontPreview.setEditorKit(kitFontPreview);
		jtpFontPreview.setDocument(docFontPreview);
		jtpFontPreview.setMargin(new Insets(4, 4, 4, 4));
		jtpFontPreview.setBounds(0, 0, 120, 18);
		jtpFontPreview.setText(getFontSampleString(defaultText));
		Object[] panelContents = { attribName, jcmbFontlist, "FontSample", jtpFontPreview };
		final Object[] buttonLabels = { "OK", "Cancel" };

		jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, 
            JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we)
			{
				jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		jOptionPane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e)
			{
				String prop = e.getPropertyName();
				if(isVisible() 
					&& (e.getSource() == jOptionPane)
					&& (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
				{
					Object value = jOptionPane.getValue();
					if(value == JOptionPane.UNINITIALIZED_VALUE)
					{
						return;
					}
					jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
					if(value.equals(buttonLabels[0]))
					{
						fontName = (String)(jcmbFontlist.getSelectedItem());
						setVisible(false);
					}
					else
					{
						fontName = null;
						setVisible(false);
					}
				}
			}
		});
		this.pack();
                this.setVisible(true);
	}

	/* ItemListener method */
	public void itemStateChanged(ItemEvent ie)
	{
		if(ie.getStateChange() == ItemEvent.SELECTED)
		{
			jtpFontPreview.setText(getFontSampleString(defaultText));
		}
	}

	public FontSelectorDialog(Frame parent, String title, boolean bModal, String attribName)
	{
		this(parent, title, bModal, attribName, "");
	}

	public String getFontName()
	{
		return fontName;
	}

	private String getFontSampleString(String demoText)
	{
		return "<HTML><BODY><FONT FACE=" + '"' + jcmbFontlist.getSelectedItem() + '"' + ">" + demoText + "</FONT></BODY></HTML>";
	}
}