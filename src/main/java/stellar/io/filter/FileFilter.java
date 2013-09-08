package stellar.io.filter;
import java.io.File;

/**
 * FileFilter serves to filter the list of files in the <code>JFileChooser</code>
 * dialog. The subclasses of this class set the specific extension and description
 * for the specific type of file. 
 * 
 * @version $Revision: 1.3 $
 * @author $Author$
 */
public class FileFilter extends javax.swing.filechooser.FileFilter 
{
    String description; 
    String extension; 
    
    /**
     * Default Constructor.
     */
    public FileFilter()
    {
    }
    public boolean accept (File f)
    {
        if(f != null) 
        {
            if(f.isDirectory()) { return true; }

            String fileExtension = getExtension (f);
            
            if(fileExtension != null && fileExtension.matches(extension)) 
            {
                return true;
            }
        }
        return false;
    }

    /**
    * Return the extension portion of the file's name .
    *
    * @see #getExtension
    * @see FileFilter#accept
    */
    private String getExtension(File f) 
    {
        String filename = f.getName();
        int i = filename.lastIndexOf('.');
        if(i>0 && i<filename.length()-1) 
        {
            return filename.substring(i+1).toLowerCase();
        };
        return null;
    }

    public String getDescription ()
    {
        return description;
    }
    
    public String getExtension ()
    {
        return extension;
    }

}
