package stellar.io.filter;
import stellar.io.Resources;

/**
 * Add XML file filter to a <code>JFileChooser</code> dialog. 
 */
public class XMLFileFilter extends FileFilter 
{
    public XMLFileFilter()
    {
        description = Resources.getString ("filter.XMLFileFilter");
        extension = "xml";
    }
}
