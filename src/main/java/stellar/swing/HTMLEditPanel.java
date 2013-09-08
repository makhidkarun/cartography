package stellar.swing;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.BorderLayout;
/**
 *
 *
 * HTMLEditPanel - Base Java Swing HTML Editor and Viewer
 *    Based upon:
 * EkitCore - Base Java Swing HTML Editor & Viewer Class (Basic Version)
 * Copyright (C) 2000 Howard Kistler
 * http://ekit.sourceforge.com/
 * Licenced under the GNU General Public Licence. 
 * @author Howard Kistler
 * @version 0.9h
 *
 * REQUIREMENTS
 * Java 2 (JDK 1.3 or 1.4)
 * Swing Library
 */

public class HTMLEditPanel extends JPanel 
    implements ActionListener, KeyListener, FocusListener, DocumentListener
{
	private JTextPane     jtpMain = new JTextPane();
    private JScrollPane   jspViewport = new JScrollPane();
	private HTMLEditorKit htmlKit = new HTMLEditorKit();
	private HTMLDocument  htmlDoc;
	private StyleSheet    styleSheet;
    private HTMLFormatToolbar jToolBar = new HTMLFormatToolbar (this);
    private BorderLayout borderLayout1 = new BorderLayout();
    
    public HTMLEditPanel()
    {
        try
        {
            htmlDoc = (HTMLDocument)(htmlKit.createDefaultDocument());
            styleSheet = htmlDoc.getStyleSheet();
            htmlKit.setDefaultCursor(new Cursor(Cursor.TEXT_CURSOR));
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void jbInit() throws Exception
    {
        jtpMain.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		jtpMain.setEditorKit(htmlKit);
		jtpMain.setDocument(htmlDoc);
		jtpMain.setMargin(new Insets(4, 4, 4, 4));
		jtpMain.addKeyListener(this);
		jtpMain.addFocusListener(this);
		jtpMain.setDragEnabled(true);
		jtpMain.setCaretPosition(0);
		jtpMain.getDocument().addDocumentListener(this);

        jspViewport.setViewportView(jtpMain);
        jspViewport.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        this.setLayout(borderLayout1);
        this.add(jspViewport, BorderLayout.CENTER);
        this.add(jToolBar, BorderLayout.NORTH);
    }

    /**
     * Action Listener method actionPerformed
     */
    public void actionPerformed (ActionEvent e) 
    {
        
    }
    // key event listeners. 
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
    /**
     * Key Listener method keyTyped (KeyEvent)
     */
	public void keyTyped(KeyEvent ke)
	{
    }

    //Focus listeners
	public void focusGained(FocusEvent fe) {}
	public void focusLost(FocusEvent fe) {}

    // Document change listeners
	public void changedUpdate(DocumentEvent de)	{ handleDocumentChange(de); }
	public void insertUpdate(DocumentEvent de)	{ handleDocumentChange(de); }
	public void removeUpdate(DocumentEvent de)	{ handleDocumentChange(de); }
	public void handleDocumentChange(DocumentEvent de)
	{
    }

    public void setDocument(HTMLDocument doc)
    {
        jtpMain.getDocument().removeDocumentListener(this);
        if (doc == null) htmlDoc = (HTMLDocument)(htmlKit.createDefaultDocument());
        else htmlDoc = doc;
        jtpMain.setDocument(htmlDoc);
        jtpMain.getDocument().addDocumentListener(this);
		jtpMain.setCaretPosition(0);
    }
    
    public void newDocument ()
    {
        setDocument ((HTMLDocument)(htmlKit.createDefaultDocument()));
    }
    
    public HTMLDocument getDocument () { return htmlDoc; } 
	public JTextPane getTextPane() { return jtpMain; }

}