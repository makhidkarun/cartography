package stellar.io.filter;
import stellar.io.Resources;

/**
 * Adds a World Builder Deluxe sector file filter to <code>JFileChooser</code> 
 * dialog
 */
public class WBSFileFilter extends FileFilter 
{
    public WBSFileFilter()
    {
        description = Resources.getString ("filter.WBSFileFilter");
        extension = "wbs";
    }
}
