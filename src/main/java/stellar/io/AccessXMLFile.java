package stellar.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.IMarshallingContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import stellar.data.Astrogation;
import org.jibx.runtime.JiBXException;

public class AccessXMLFile implements AstrogationFileReader
{
    private Astrogation data;
    private File   inputFile;
    private File   outputFile;
    private URL    urlFile;
    private boolean useCompressedFile = false;

    public AccessXMLFile(String filename)
    {
        inputFile = new File (filename);
        outputFile = new File (filename);
    }

    public AccessXMLFile (File file)
    {
        inputFile = file;
        outputFile = file;
    }
    
    public AccessXMLFile (URL file)
    {
        urlFile = file;
    }

    private InputStream openInputStream() throws IOException, FileNotFoundException
    {
        if (urlFile != null) return urlFile.openStream();
        if (inputFile != null) return new FileInputStream (inputFile);
        return null;
    }
    
    public void read() throws JiBXException, IOException
    {
        IBindingFactory bfact = BindingDirectory.getFactory(Astrogation.class);
        IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
        if (useCompressedFile)
        {
            data = (Astrogation) uctx.unmarshalDocument(new GZIPInputStream (openInputStream()), null);
        }
        else
        {
            data = (Astrogation) uctx.unmarshalDocument (openInputStream(), null);
        }
    }
    public void write () throws JiBXException, IOException
    {
        IBindingFactory bfact = BindingDirectory.getFactory(Astrogation.class);
        IMarshallingContext mctx = bfact.createMarshallingContext();
        mctx.setIndent(4);
        if (useCompressedFile)
        {
            mctx.marshalDocument(data, "UTF-8", null, new GZIPOutputStream(new FileOutputStream(outputFile)));
        }
        else
        {
            mctx.marshalDocument(data, "UTF-8", null, new FileOutputStream(outputFile));
        }
    }
    public Astrogation getAstrogation () { return data; }
    public void setAstrogation (Astrogation data) { this.data = data; } 
    public void setUseCompressedFile (boolean compress) { this.useCompressedFile = compress; }
    
    public static void main(String[] args)
    {
        SECFileReader readFile = new SECFileReader("C:\\thom\\projects\\cartography\\fornast.sec");
        AccessXMLFile writeFile = new AccessXMLFile("C:\\thom\\projects\\cartography\\fornast.xml");

        try {readFile.read(); } 
        catch (Exception ex) { ex.printStackTrace();} 
        
        try { writeFile.read(); }
        catch (Exception ex) { ex.printStackTrace();}
    }
}