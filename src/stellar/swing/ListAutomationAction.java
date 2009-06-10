package stellar.swing;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class ListAutomationAction extends HTMLEditorKit.InsertHTMLTextAction
{
    protected HTMLEditPanel parentEdit;
	HTML.Tag htmlTag;
    Hashtable tags = new Hashtable();
    
	public ListAutomationAction(HTMLEditPanel edit, String sLabel, HTML.Tag listType)
	{
		super(sLabel, "", listType, HTML.Tag.LI);
		parentEdit = edit;
		htmlTag    = listType;
        
		HTML.Tag[] tagList = HTML.getAllTags();
		for(int i = 0; i < tagList.length; i++)
		{
			tags.put(tagList[i].toString(), tagList[i]);
		}
	}
	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			JEditorPane jepEditor = (JEditorPane)(parentEdit.getTextPane());
			String selTextBase = jepEditor.getSelectedText();
			int textLength = -1;
			if(selTextBase != null)
			{
				textLength = selTextBase.length();
			}
			if(selTextBase == null || textLength < 1)
			{
				int pos = parentEdit.getTextPane().getCaretPosition();
				parentEdit.getTextPane().setCaretPosition(pos);
                
				if(ae.getActionCommand() != "newListPoint")
				{
					if(checkParentsTag(HTML.Tag.OL) || checkParentsTag(HTML.Tag.UL))
					{
						//new SimpleInfoDialog(parentEkit.getFrame(), Translatrix.getTranslationString("Error"), 
                        //    true, Translatrix.getTranslationString("ErrorNestedListsNotSupported"));
						return;
					}
				}
				String sListType = (htmlTag == HTML.Tag.OL ? "ol" : "ul");
				StringBuffer sbNew = new StringBuffer();
				if(checkParentsTag(htmlTag))
				{
					sbNew.append("<li></li>");
					insertHTML(parentEdit.getTextPane(), parentEdit.getDocument(), 
                        parentEdit.getTextPane().getCaretPosition(), sbNew.toString(), 0, 0, HTML.Tag.LI);
				}
				else
				{
					sbNew.append("<" + sListType + "><li></li></" + sListType + "><p>&nbsp;</p>");
					insertHTML(parentEdit.getTextPane(), parentEdit.getDocument(), 
                        parentEdit.getTextPane().getCaretPosition(), sbNew.toString(), 0, 0, 
                        (sListType.equals("ol") ? HTML.Tag.OL : HTML.Tag.UL));
				}
			}
			else
			{
				String sListType = (htmlTag == HTML.Tag.OL ? "ol" : "ul");
				HTMLDocument htmlDoc = (HTMLDocument)(jepEditor.getDocument());
				int iStart = jepEditor.getSelectionStart();
				int iEnd   = jepEditor.getSelectionEnd();
				String selText = htmlDoc.getText(iStart, iEnd - iStart);
				StringBuffer sbNew = new StringBuffer();
				String sToken = ((selText.indexOf("\r") > -1) ? "\r" : "\n");
				StringTokenizer stTokenizer = new StringTokenizer(selText, sToken);
				sbNew.append("<" + sListType + ">");
				while(stTokenizer.hasMoreTokens())
				{
					sbNew.append("<li>");
					sbNew.append(stTokenizer.nextToken());
					sbNew.append("</li>");
				}
				sbNew.append("</" + sListType + "><p>&nbsp;</p>");
				htmlDoc.remove(iStart, iEnd - iStart);
				insertHTML(jepEditor, htmlDoc, iStart, sbNew.toString(), 1, 1, null);
			}
		}
		catch (BadLocationException ble) {}
	}

	public boolean checkParentsTag(HTML.Tag tag)
	{
		Element e = parentEdit.getDocument().getParagraphElement(parentEdit.getTextPane().getCaretPosition());
		String tagString = tag.toString();
		if(e.getName().equalsIgnoreCase(tag.toString()))
		{
			return true;
		}
		do
		{
			if((e = e.getParentElement()).getName().equalsIgnoreCase(tagString))
			{
				return true;
			}
		} while(!(e.getName().equalsIgnoreCase("html")));
		return false;
	}
}