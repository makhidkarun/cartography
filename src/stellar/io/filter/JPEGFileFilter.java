package stellar.io.filter;
import stellar.io.Resources;

/**
 * Adds a JPEG Image file filter to <code>JFileChooser</code> dialog.
 */
public class JPEGFileFilter extends FileFilter
{
    public JPEGFileFilter()
    {
        extension = "jpeg";
        description = Resources.getString ("filter.JPEGFileFilter");
    }
}
