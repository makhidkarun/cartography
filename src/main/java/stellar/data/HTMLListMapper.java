package stellar.data;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.HTMLWriter;
import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.IXMLReader;
import org.jibx.runtime.IXMLWriter;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

public class HTMLListMapper implements IMarshaller, IUnmarshaller, IAliasable
{
    /** Root element namespace URI. */
    private final String m_uri;
    
    /** Namespace URI index in binding. */
    private final int m_index;
    
    /** Root element name. */
    private final String m_name;

    /** Context being used for unmarshalling. */
    protected UnmarshallingContext m_unmarshalContext;

    /** Start position of the last block. */
    private int lastBlockStartPos;
    
    /** Parser Callback class to place the parsed elements into */
    protected ParserCallback reader;
    private boolean foundHead = false;
    private boolean foundBody = false;
    private SimpleAttributeSet emptyAttributes = new SimpleAttributeSet();
   
    public HTMLListMapper()
    {
        m_uri = null;
        m_index = -1;
        m_name = null;
    }
    /**
     * Aliased constructor. This takes a name definition for the element. It'll
     * be used by JiBX when a name is supplied by the mapping which references
     * this custom marshaller/unmarshaller.
     *
     * @param uri namespace URI for the top-level element
     * @param index namespace index corresponding to the defined URI within the
     * marshalling context definitions
     * @param name local name for the top-level element
     * @throws JiBXException on error creating document
     */
    
    public HTMLListMapper (String uri, int index, String name)
        throws JiBXException {
        // save the simple values
        m_uri = uri;
        m_index = index;
        m_name = name;
    }
    
    /* (non-Javadoc)
     * @see org.jibx.runtime.IMarshaller#isExtension(int)
     */
    
    public boolean isExtension(int index) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.jibx.runtime.IUnmarshaller#isPresent(org.jibx.runtime.IUnmarshallingContext)
     */
     
    public boolean isPresent(IUnmarshallingContext ctx) throws JiBXException 
    {
        if (m_name == null) 
        {
            if (!(ctx instanceof UnmarshallingContext)) 
            {
                throw new JiBXException
                    ("Unmarshalling context not of expected type");
            } 
            else 
            {
                return !((UnmarshallingContext)ctx).isEnd();
            }
        } 
        else 
        {
            return ctx.isAt(m_uri, m_name);
        }
    }

    /* (non-Javadoc)
     * @see org.jibx.runtime.IMarshaller#marshal(java.lang.Object,
     *  org.jibx.runtime.IMarshallingContext)
     */
    
    public void marshal(Object obj, IMarshallingContext ictx)
        throws JiBXException {
        if (!(obj instanceof HTMLDocument))
            throw new JiBXException ("Marshalling object not of expected type");
        
        if (!(ictx instanceof MarshallingContext))
            throw new JiBXException ("Marshalling context not of expected type");

        StringWriter w = new StringWriter ();
        HTMLWriter hw = new HTMLWriter (w, (HTMLDocument)obj);
        try {
        hw.write();
        } catch (IOException ex) { throw new JiBXException ("Failure writing to StringWriter:", ex); } 
          catch (BadLocationException ex) { throw new JiBXException ("Failure writing to HTMLWriter:", ex); } 
        MarshallingContext ctx = (MarshallingContext)ictx;
        
        ctx.startTag(m_index, m_name);
        IXMLWriter xw = ctx.getXmlWriter();
        try {
        xw.writeCData(w.toString());
        } catch (IOException ex) { throw new JiBXException ("Failure writing to Context markup:", ex); }
        ctx.endTag(m_index, m_name);
    }    
        
    /* (non-Javadoc)
     * @see org.jibx.runtime.IUnmarshaller#unmarshal(java.lang.Object,
     *  org.jibx.runtime.IUnmarshallingContext)
     */
     
    public Object unmarshal(Object obj, IUnmarshallingContext ictx)
        throws JiBXException
    { 
        HTMLEditorKit kit;
        HTMLDocument  doc;
        if ( !(ictx instanceof UnmarshallingContext)) 
        {
            throw new JiBXException ("Unmarshalling context not of expected type");
        }
        else if (m_name != null && !ictx.isAt(m_uri, m_name))
        {
            ((UnmarshallingContext)ictx).throwStartTagNameError(m_uri, m_name);
        }

        if (obj == null)
        {
            kit = new HTMLEditorKit();
            doc = (HTMLDocument)kit.createDefaultDocument();
        }
        else
        {
            doc = (HTMLDocument) obj;
        }
        reader = doc.getReader(0);
        m_unmarshalContext = (UnmarshallingContext)ictx;
        foundHead = false;
        foundBody = false;
   
        // skip past the start tag
        m_unmarshalContext.nextToken();
        
        while (true)
        {
            if ((m_unmarshalContext.currentEvent() == IXMLReader.END_TAG) &&
                (m_unmarshalContext.getName().equals(m_name)))
            {
                m_unmarshalContext.nextToken();
                break;
            }
            try {
                if (IXMLReader.CDSECT == m_unmarshalContext.currentEvent())
                {
                    String text = m_unmarshalContext.getText();  
                    HTMLEditorKit.Parser p = doc.getParser();
                    p.parse(new StringReader(text), reader, true);
                    m_unmarshalContext.nextToken();
                }
                else
                    unmarshalNode (reader);
            }
            catch (IOException ex) { throw new JiBXException ("Error reading from document", ex); } 
        }
        try {
        reader.flush();
        }
        catch (BadLocationException ex) { throw new JiBXException ("Error reading from document", ex); }
        return doc;
    }        

    /**
     * Unmarshal single node. This unmarshals the next node from the input
     * stream, up to the close tag of the containing element.
     *
     * @exception JiBXException on error in unmarshalling
     * @exception IOException on error reading input
     */
    
    protected void unmarshalNode(ParserCallback cb) throws JiBXException, IOException {
        while (true) {
            int cev = m_unmarshalContext.currentEvent();
            switch (cev) {
                
                case IXMLReader.CDSECT:
                    {
                        String text = m_unmarshalContext.getText();  
                        cb.handleText(text.toCharArray(), getBlockStartPosition());
                        m_unmarshalContext.nextToken();
                        return;
                    }
                
                case IXMLReader.COMMENT:
                    {
                        String text = m_unmarshalContext.getText();
                        cb.handleComment(text.toCharArray(), getBlockStartPosition());
                        m_unmarshalContext.nextToken();
                        return;
                    }
                
                case IXMLReader.END_TAG:
                    unmarshalEndTag ();
                    m_unmarshalContext.nextToken();
                    return;

                case IXMLReader.TEXT:
                    unmarshalText();
                    return;

                case IXMLReader.START_TAG:
                    unmarshalStartTag();
                    m_unmarshalContext.nextToken();
                    return;

                default:
                    m_unmarshalContext.nextToken();
/*                
                case UnmarshallingContext.ENTITY_REF:
                    if (m_unmarshalContext.getText() == null) {
                        String name = m_unmarshalContext.getName();
                        m_unmarshalContext.nextToken();
                        return m_document.createEntityReference(name);
                    } else {
                        String text = accumulateText();
                        return m_document.createTextNode(text);
                    }
                
                case UnmarshallingContext.PROCESSING_INSTRUCTION:
                    {
                        String text = m_unmarshalContext.getText();
                        m_unmarshalContext.nextToken();
                        int index = 0;
                        while (++index < text.length() &&
                            !isWhitespace(text.charAt(index)));
                        if (index < text.length()) {
                            String target = text.substring(0, index);
                            while (++index < text.length() &&
                                isWhitespace(text.charAt(index)));
                            String data = text.substring(index);
                            return m_document.
                                createProcessingInstruction(target, data);
                        } else {
                            return m_document.
                                createProcessingInstruction(text, "");
                        }
                    }
*/                
            }
        }
    }

    /**
     * Returns the start position of the current block. Block is
     * overloaded here, it really means the current start position for
     * the current comment tag, text, block.... This is provided for
     * subclassers that wish to know the start of the current block when
     * called with one of the handleXXX methods.
     */
    int getBlockStartPosition() 
    {
    	return Math.max(0, lastBlockStartPos - 1);
    }

    /**
     * Unmashall the end tag
     */
    protected void unmarshalEndTag() throws JiBXException
    {
        String name = m_unmarshalContext.getName();
        reader.handleEndTag(HTML.getTag(name), getBlockStartPosition());
        lastBlockStartPos += name.length() + 3;
    }
    
    /**
     * unmarshall a start tag
     */
    protected void unmarshalStartTag() throws JiBXException
    {
        SimpleAttributeSet attributes = new SimpleAttributeSet ();
        String name = m_unmarshalContext.getName();
        if (HTML.getTag(name) == null) m_unmarshalContext.throwStartTagException(name);
        int acount = m_unmarshalContext.getAttributeCount();
        if (acount > 0)
        {
       
            for (int i = 0; i < acount; i++)
            {
                attributes.addAttribute (m_unmarshalContext.getAttributeName(i), 
                                        m_unmarshalContext.getAttributeValue(i));
            }
            
        }
        reader.handleStartTag(HTML.getTag(name), attributes, getBlockStartPosition());
        lastBlockStartPos += name.length() + 2;
        if (HTML.getTag(name) == HTML.getTag("html")) foundHead = true;
        if (HTML.getTag(name) == HTML.getTag("body")) foundBody = true;
    }

    protected void unmarshalText() throws JiBXException
    {
        //if (!foundHead) insertHeadTag();
        //if (!foundBody) insertBodyTag();
        String text = m_unmarshalContext.accumulateText();
        if (text.trim().length() > 0)
        {
            reader.handleText(text.toCharArray(), getBlockStartPosition());
        }
        lastBlockStartPos += 1;
    }
    
    protected void insertHeadTag()
    {
        reader.handleStartTag(HTML.getTag("html"), emptyAttributes, getBlockStartPosition());
        lastBlockStartPos += 6;
        foundHead = true;
    }
    
    protected void insertBodyTag()
    {
        reader.handleStartTag (HTML.getTag("body"), emptyAttributes, getBlockStartPosition());
        lastBlockStartPos += 6;
        foundBody = true;
    }
}