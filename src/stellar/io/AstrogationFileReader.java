package stellar.io;
import stellar.data.Astrogation;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.jibx.runtime.JiBXException;

public interface AstrogationFileReader 
{
    public Astrogation getAstrogation ();
    public void read() throws FileNotFoundException, JiBXException, IOException, SECFileStateMachineException;
    
}