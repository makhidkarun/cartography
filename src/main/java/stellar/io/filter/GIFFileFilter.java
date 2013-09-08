package stellar.io.filter;
import stellar.io.Resources;

/**
 * Adds a GIF Image file filter to <code>JFileChooser</code> dialog.
 */
public class GIFFileFilter extends FileFilter 
{
    public GIFFileFilter()
    {
        extension = "gif";
        description = "GIF file (.gif)";
        description = Resources.getString ("filter.GIFFileFilter");
    }
}
