package stellar.io.filter;
import stellar.io.Resources;

/**
 * Add an Adobe Acrobat Portable File Format filter to <code>JFileChooser</code> dialog. 
 */
public class PDFFileFilter extends FileFilter 
{
    public PDFFileFilter()
    {
        description = Resources.getString ("filter.PDFFileFilter");
        extension = "pdf";
    }
}