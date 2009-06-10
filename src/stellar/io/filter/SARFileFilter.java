package stellar.io.filter;
import stellar.io.Resources;

/**
 * Adds a Galactic Sector Archive file filter to a <code>JFileChooser</code> dialog
 */
public class SARFileFilter extends FileFilter 
{
    public SARFileFilter()
    {
        description = Resources.getString ("filter.SARFileFilter");
        extension = "sar";
    }
}
