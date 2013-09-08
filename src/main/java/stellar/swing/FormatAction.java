package stellar.swing;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledEditorKit.StyledTextAction;
import javax.swing.text.html.HTML;

public class FormatAction extends StyledTextAction 
{
    protected HTMLEditPanel parentEdit;
	HTML.Tag htmlTag;

	public FormatAction(HTMLEditPanel edit, String actionName, HTML.Tag inTag)
	{
		super(actionName);
        parentEdit = edit;
		htmlTag    = inTag;
	}
    
	public void actionPerformed(ActionEvent ae)
	{
		JTextPane parentTextPane = parentEdit.getTextPane();
		String selText = parentTextPane.getSelectedText();
		int textLength = -1;
		if(selText != null)
		{
			textLength = selText.length();
		}
		if(selText == null || textLength < 1)
		{
			//SimpleInfoDialog sidWarn = new SimpleInfoDialog(parentEkit.getFrame(), "", true, 
            //  Translatrix.getTranslationString("ErrorNoTextSelected"), SimpleInfoDialog.ERROR);
		}
		else
		{
			SimpleAttributeSet sasText = new SimpleAttributeSet(parentTextPane.getCharacterAttributes());
			sasText.addAttribute(htmlTag, new SimpleAttributeSet());
			int caretOffset = parentTextPane.getSelectionStart();
			parentTextPane.select(caretOffset, caretOffset + textLength);
			parentTextPane.setCharacterAttributes(sasText, false);
			//parentEdit.refreshOnUpdate();
			parentTextPane.select(caretOffset, caretOffset + textLength);
		}
	}
    

}