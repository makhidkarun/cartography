package stellar.swing;
import stellar.io.Resources;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

public class HTMLFormatToolbar extends JToolBar 
{
    private HTMLEditPanel parent; 
    
    private JButton bCut = new JButton (new DefaultEditorKit.CutAction());
    private JButton bCopy = new JButton (new DefaultEditorKit.CopyAction());
    private JButton bPaste = new JButton (new DefaultEditorKit.PasteAction());
    private JButton bBold = new JButton (new StyledEditorKit.BoldAction());
    private JButton bItalic = new JButton (new StyledEditorKit.ItalicAction());
    private JButton bUnderline = new JButton (new StyledEditorKit.UnderlineAction());
    private JButton bAlignLeft = new JButton (new StyledEditorKit.AlignmentAction ("Align Left", StyleConstants.ALIGN_LEFT));
    private JButton bAlignRight = new JButton (new StyledEditorKit.AlignmentAction ("Align Right", StyleConstants.ALIGN_RIGHT));
    private JButton bAlignCenter = new JButton (new StyledEditorKit.AlignmentAction ("Align Center", StyleConstants.ALIGN_CENTER));
    private JButton bAlignJustify = new JButton (new StyledEditorKit.AlignmentAction("Align Justify", StyleConstants.ALIGN_JUSTIFIED));
/*
    private JButton bAnchor;
    private JButton bBulletList;
    private JButton bNumberList;
    private JButton bTableToolbar = new JButton();
    private JButton bFontToolbar = new JButton();
*/
    public HTMLFormatToolbar(HTMLEditPanel parent)
    {
        try
        {
            this.parent = parent;
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void jbInit() throws Exception
    {
        bCut.setIcon(Resources.getIcon("cut"));
        bCut.setText(null);
        bCut.setFocusable(false);
        bCut.setToolTipText("Cut (Ctrl+X)");

        bCopy.setIcon(Resources.getIcon("copy"));
        bCopy.setText(null);
        bCopy.setFocusable(false);
        bCopy.setToolTipText("Copy (Ctrl+C)");
        
        bPaste.setIcon(Resources.getIcon("paste"));
        bPaste.setText(null);
        bPaste.setFocusable(false);
        bPaste.setToolTipText("Paste (Ctrl+V)");
        
        bBold.setIcon(Resources.getIcon("bold"));
        bBold.setText(null);
        bBold.setFocusable(false);
        bBold.setToolTipText("Bold");
        
        bItalic.setIcon (Resources.getIcon("italic"));
        bItalic.setText(null);
        bItalic.setFocusable(false);
        bItalic.setToolTipText("Italic");
        
        bUnderline.setIcon(Resources.getIcon("underline"));
        bUnderline.setText(null);
        bUnderline.setFocusable(false);
        bUnderline.setToolTipText("Underline");
        
        bAlignLeft.setIcon(Resources.getIcon("alignleft"));
        bAlignLeft.setText(null);
        bAlignLeft.setFocusable(false);
        bAlignLeft.setToolTipText("Align Left");
        
        bAlignCenter.setIcon(Resources.getIcon("aligncenter"));
        bAlignCenter.setText(null);
        bAlignCenter.setFocusable(false);
        bAlignCenter.setToolTipText("Align Center");
        
        bAlignRight.setIcon (Resources.getIcon("alignright"));
        bAlignRight.setText(null);
        bAlignRight.setFocusable(false);
        bAlignRight.setToolTipText("Align Right");

        bAlignJustify.setIcon(Resources.getIcon("justify"));
        bAlignJustify.setText(null);
        bAlignJustify.setFocusable(false);
        bAlignJustify.setToolTipText("Align Justify");
/*
        bAnchor = new JButton (new CustomAction (parent, "Insert Anchor", HTML.Tag.A));
        bAnchor.setIcon(Resources.getIcon("link"));
        bAnchor.setText(null);
        bAnchor.setFocusable(false);
        bAnchor.setToolTipText("Insert Anchor");

        bBulletList  = new JButton(new ListAutomationAction (parent, "List Unordered", HTML.Tag.UL));
        bBulletList.setIcon(Resources.getIcon("bullets"));
        bBulletList.setText(null);
        bBulletList.setFocusable(false);
        bBulletList.setToolTipText("Insert Unorderd List");
        
        bNumberList = new JButton(new ListAutomationAction (parent, "List Ordered", HTML.Tag.OL));
        bNumberList.setIcon(Resources.getIcon("numlist"));
        bNumberList.setText(null);
        bNumberList.setFocusable(false);
        bNumberList.setToolTipText("Insert Ordered List");
  */      
        this.add(bCut, null);
        this.add(bCopy, null);
        this.add(bPaste, null);
        this.add(new JToolBar.Separator(), null);
        this.add(bBold, null);
        this.add(bItalic, null);
        this.add(bUnderline, null);
        this.add(new JToolBar.Separator(), null);
        this.add(bAlignLeft, null);
        this.add(bAlignCenter, null);
        this.add(bAlignRight, null);
        this.add(bAlignJustify, null);
    }
    
    public JTextPane getTextPane()
    {
        return parent.getTextPane();
    }
}