package stellar.swing;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledEditorKit.StyledTextAction;
import javax.swing.text.html.HTML;

public class CustomAction extends StyledTextAction 
{
    protected HTMLEditPanel parentEdit;
	HTML.Tag htmlTag;
    Hashtable htmlAttribs = new Hashtable();

    public CustomAction(HTMLEditPanel edit, String actionName, HTML.Tag inTag, Hashtable attribs)
	{
		super(actionName);
		parentEdit  = edit;
		htmlTag     = inTag;
		htmlAttribs = attribs;
	}

	public CustomAction(HTMLEditPanel edit, String actionName, HTML.Tag inTag)
	{
		this(edit, actionName, inTag, new Hashtable());
	}

	public void actionPerformed(ActionEvent ae)
	{
		if(this.isEnabled())
		{
			Hashtable htmlAttribs2 = new Hashtable();
			JTextPane parentTextPane = parentEdit.getTextPane();
			String selText = parentTextPane.getSelectedText();
			int textLength = -1;
			if(selText != null)
			{
				textLength = selText.length();
			}
			if(selText == null || textLength < 1)
			{
                JOptionPane.showMessageDialog(parentTextPane, "No Text Selected", "Error", JOptionPane.ERROR_MESSAGE);
				//SimpleInfoDialog sidWarn = new SimpleInfoDialog(parentEkit.getFrame(), 
                //  Translatrix.getTranslationString("Error"), true, 
                //  Translatrix.getTranslationString("ErrorNoTextSelected"), SimpleInfoDialog.ERROR);
			}
			else
			{
				int caretOffset = parentTextPane.getSelectionStart();
				int internalTextLength = selText.length();
				String currentAnchor = "";
				// Somewhat ham-fisted code to obtain the first HREF in the selected text,
				// which (if found) is passed to the URL HREF request dialog.
				if(htmlTag.toString().equals(HTML.Tag.A.toString()))
				{
					SimpleAttributeSet sasText = null;
					for(int i = caretOffset; i < caretOffset + internalTextLength; i++)
					{
						parentTextPane.select(i, i + 1);
						sasText = new SimpleAttributeSet(parentTextPane.getCharacterAttributes());
						Enumeration attribEntries1 = sasText.getAttributeNames();
						while(attribEntries1.hasMoreElements() && currentAnchor.equals(""))
						{
							Object entryKey   = attribEntries1.nextElement();
							Object entryValue = sasText.getAttribute(entryKey);
							if(entryKey.toString().equals(HTML.Tag.A.toString()))
							{
								if(entryValue instanceof SimpleAttributeSet)
								{
									Enumeration subAttributes = ((SimpleAttributeSet)entryValue).getAttributeNames();
									while(subAttributes.hasMoreElements() && currentAnchor.equals(""))
									{
										Object subKey = subAttributes.nextElement();
										if(subKey.toString().toLowerCase().equals("href"))
										{
											currentAnchor = ((SimpleAttributeSet)entryValue).getAttribute(subKey).toString();
											break;
										}
									}
								}
							}
						}
						if(!currentAnchor.equals("")) { break; }
					}
				}
				parentTextPane.select(caretOffset, caretOffset + internalTextLength);
				SimpleAttributeSet sasTag  = new SimpleAttributeSet();
				SimpleAttributeSet sasAttr = new SimpleAttributeSet();
				if(htmlTag.toString().equals(HTML.Tag.A.toString()))
				{
					if(!htmlAttribs.containsKey("href"))
					{
						// UserInputAnchorDialog uidInput = new UserInputAnchorDialog
                        //  (parentEkit, Translatrix.getTranslationString("AnchorDialogTitle"), 
                        //  true, currentAnchor);
						//String newAnchor = uidInput.getInputText();
						//uidInput.dispose();
                        String newAnchor = null;
						if(newAnchor != null)
						{
							htmlAttribs2.put("href", newAnchor);
						}
						else
						{
							parentEdit.repaint();
							return;
						}
					}
				}
				if(htmlAttribs2.size() > 0)
				{
					Enumeration attribEntries = htmlAttribs2.keys();
					while(attribEntries.hasMoreElements())
					{
						Object entryKey   = attribEntries.nextElement();
						Object entryValue = htmlAttribs2.get(entryKey);
						insertAttribute(sasAttr, entryKey, entryValue);
					}
					SimpleAttributeSet baseAttrs = new SimpleAttributeSet(parentEdit.getTextPane().getCharacterAttributes());
					Enumeration attribEntriesOriginal = baseAttrs.getAttributeNames();
					while(attribEntriesOriginal.hasMoreElements())
					{
						Object entryKey   = attribEntriesOriginal.nextElement();
						Object entryValue = baseAttrs.getAttribute(entryKey);
						insertAttribute(sasAttr, entryKey, entryValue);
					}
					sasTag.addAttribute(htmlTag, sasAttr);
					parentTextPane.setCharacterAttributes(sasTag, false);
					//parentEdit.refreshOnUpdate();
				}
				parentTextPane.select(caretOffset, caretOffset + internalTextLength);
				parentTextPane.requestFocus();
			}
		}
	}

	private void insertAttribute(SimpleAttributeSet attrs, Object key, Object value)
	{
		if(value instanceof AttributeSet)
		{
			AttributeSet subSet = (AttributeSet)value;
			Enumeration attribEntriesSub = subSet.getAttributeNames();
			while(attribEntriesSub.hasMoreElements())
			{
				Object subKey   = attribEntriesSub.nextElement();
				Object subValue = subSet.getAttribute(subKey);
				insertAttr(attrs, subKey, subValue);
			}
		}
		else
		{
			insertAttr(attrs, key, value);
		}
		// map CSS font-family declarations to FONT tag face declarations
		if(key.toString().toLowerCase().equals("font-family"))
		{
			if(attrs.isDefined("face"))
			{
				insertAttr(attrs, "face", attrs.getAttribute("face"));
				insertAttr(attrs, "font-family", attrs.getAttribute("face"));
			}
			else
			{
				insertAttr(attrs, "face", value);
			}
		}
		// map CSS font-size declarations to FONT tag size declarations
/*
		if(key.toString().toLowerCase().equals("font-size"))
		{
			if(attrs.isDefined("size"))
			{
				insertAttr(attrs, "size", attrs.getAttribute("size"));
				insertAttr(attrs, "font-size", attrs.getAttribute("size"));
			}
			else
			{
				insertAttr(attrs, "size", value);
			}
		}
*/
	}

	private void insertAttr(SimpleAttributeSet attrs, Object key, Object value)
	{
		while(attrs.isDefined(key))
		{
			attrs.removeAttribute(key);
		}
		attrs.addAttribute(key, value);
	}
    
}