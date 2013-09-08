package stellar.io.filter;
import stellar.io.Resources;

/**
 * Adds a Hevean and Earth sector file filter to <code>JFileChooser</code> dialog.
 */
public class HESFileFilter extends FileFilter 
{
    public HESFileFilter()
    {
        description = Resources.getString ("filter.HESFileFilter");
        extension = "hes";
    }
}
