package stellar.io.filter;
import stellar.io.Resources;

/**
 * Adds a generic traveller sector file filter to <code>JFileChooser</code> dialog.
 */
public class SECFileFilter extends FileFilter 
{
    public SECFileFilter()
    {
        description = Resources.getString ("filter.SECFileFilter");
        extension = "sec";
    }
}
