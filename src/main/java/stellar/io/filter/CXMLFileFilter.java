package stellar.io.filter;
import stellar.io.Resources;

/**
 * Adds a Compressed XML file filter to <code>JFileChooser</code> dialog. 
 */
public class CXMLFileFilter extends FileFilter 
{
    public CXMLFileFilter()
    {
        extension = "cxml";
        //description = "Compressed XML file (.cxml)";
        description = Resources.getString ("filter.CXMLFileFilter");        
    }
}
