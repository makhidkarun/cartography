package stellar.io;

import java.io.File;
import java.io.IOException;
import stellar.data.GroupRecord;
import stellar.data.GroupType;
import stellar.data.HexID;
import java.io.StreamTokenizer;
/**
 * This class reads the .SEC files as found on http://maps.travellercentral.com . 
 * These files are maintained by Anthony Jackson for the purpose of generating 
 * maps and analysis using the GURPS Traveller: Far Trader trade rules. 
 */
public class SECFileReader extends SectorTextReader
{
    public SECFileReader (String file) { super(file); } 
    public SECFileReader (File file) { super(file);  }
    
    public void readHeader() throws IOException, SECFileStateMachineException
    {
        sector = new GroupRecord();
        sector.setType(GroupType.SECTOR);
        //parser.commentChar('#');

        parser.nextToken(); // initial name 

        if (parser.ttype == StreamTokenizer.TT_WORD)
        {
            if (parser.sval.equals("Name")) // generic .sec file
            {
                readGENheader();
            }
            else // Anthony .sec file
            {
                readAJheader();
            }
        }
        else if (parser.ttype == StreamTokenizer.TT_NUMBER) { } 
        else if (parser.ttype == StreamTokenizer.TT_EOL) { } 
        else
        {
            /* if the first character is "#", this is a Galactic sector file
             * We assume...
             */
            if ((char)parser.ttype == '#')
            {
               readGALheader();
            }
        }
        if (sector.getName() == null)
        {
            throw new SECFileStateMachineException ("No sector name found in .sec file, probably due to unknown header configuration. Please contact support");
        }
        sector.setKey(sector.getName().substring(0,4).toLowerCase() + ".0000");
        sector.setValue(sector.getName());
        sector.setProvider(data.getDefaultProvider());
        sector.getLocation().setHexGroup(sector);
        sector.setExtentX(32);
        sector.setExtentY(40);
        data.addGroup(sector);
    }
    
    public void readLinks() { }

    /* Galactic .sec files have a 5 line comment header
    * 1: sector name / galaxy name 
    * 2: Blank
    * 3: marker line with character positions
    * 4: column header line
    * 5: marker line
    */
    public void readGALheader() throws IOException
    {
        boolean found = false; 
        
        parser.nextToken();
        sector.setName(parser.sval);
        parser.nextToken();
        while ((char)parser.ttype != '/' && parser.ttype != StreamTokenizer.TT_EOL)
        {
            if (parser.ttype == StreamTokenizer.TT_WORD)
                sector.setName(sector.getName() + " " + parser.sval);
            else if (parser.ttype == StreamTokenizer.TT_NUMBER)
                sector.setName(sector.getName() + " " + Double.toString(parser.nval));
            else
                sector.setName(sector.getName().concat(Character.toString((char)parser.ttype)));
            parser.nextToken();                
        }
        while (!found)
        {
            parser.nextToken();
            found = (parser.ttype == StreamTokenizer.TT_EOL);
        }
        sector.setLocation(new HexID (1,1));
        parser.commentChar('#');
    }

    /* Anthony's .sec files have a three line
     * header, consiting of a sector name, a x/y position
     * and a line of numbers to id the location of 
     * the important data elements he uses for the trade
     * maps. 
     */
    public void readAJheader() throws IOException
    {
        boolean found = false; 
        int x, y;         
        sector.setName(parser.sval);
        parser.nextToken(); // carriage return
        while (parser.ttype != StreamTokenizer.TT_EOL)
        {
            sector.setName(sector.getName() + " " + parser.sval);
            parser.nextToken();
        }
        parser.nextToken(); // x coord
        x = (int)parser.nval;
        parser.nextToken(); // y coord
        y = (int)parser.nval;
        sector.setLocation(new HexID(x,y));
        sector.getLocation().setHexType(GroupType.GROUP);
        parser.nextToken(); //CRLF
        while (!found)
        {
            parser.nextToken();
            found = (parser.ttype == StreamTokenizer.TT_EOL);
        }
        parser.commentChar('#');
    }

    /* This is a generic, unnamed and widely available .sec file and header
     * The header looks like below, as we are using the initial "Name" token
     * to identifiy it. We parse throught it looking for thr four dots on the
     * ruler line. 
Name UWP Bases Codes PBG Allegiance/Stellar Data
 1-13: Name
15-18: HexNbr
20-28: UWP
   31: Bases
33-47: Codes & Comments
   49: Zone
   49: Zone
52-54: PBG
56-57: Allegiance
59-74: Stellar Data

....+....1....+....2....+....3....+....4....+....5....+....6....+....7....+....8
     */
    
    public void readGENheader() throws IOException
    {
        boolean found = false;
        int dotCount = 0;
        sector.setName("Unknown");
        
        //read until the four dots are found
        //parser interprets a lone '.' as a number of a value 0.0
        while (!found)
        {
            parser.nextToken();
            if (parser.ttype == StreamTokenizer.TT_NUMBER)
                dotCount++;
            else
                dotCount = 0;
            if (dotCount == 4) found = true;
        }
        found = false;
        // read until the end of the line. 
        while (!found)
        {
            parser.nextToken();
            found = (parser.ttype == StreamTokenizer.TT_EOL);
        }
        sector.setLocation(new HexID (1,1));
    }

    public static void main(String[] args)
    {
        SECFileReader readSECFile = new SECFileReader("C:\\thom\\projects\\cartography\\fornast.sec");
        try
        {
            readSECFile.read();
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }

}