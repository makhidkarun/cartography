package stellar.io;

import stellar.data.Astrogation;
import stellar.data.TableRecord;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import stellar.data.GroupRecord;
import stellar.data.GroupType;
import stellar.data.HexID;
import stellar.data.ProviderRecord;
import stellar.data.Links;
import stellar.data.TableRecordKey;

import java.io.StreamTokenizer;

/**
 * This class reads the .dat files used by Galactic 2.4.
 * These are fix formatted files with two sections:
 * <br>
 * <pre>
 * \@SUB-SUBSECTOR: [name] SECTOR: [sector name]
 * $[hex 1] [hex 2] [x dir] [y dir] [color]
 * #
 * #--------1---------2---------3---------4---------5---------6---
 * #PlanetName   Loc. UPP Code   B   Notes         Z  PBG Al LRX *
 * #----------   ---- ---------  - --------------- -  --- -- --- -
 * Name : Location : UWP : Base : Notes [Trade codes] : Zone : PBG : Allegiance : LRX : Data Flag 
 * </pre><br>
 * The header line starts with '@' and contains both the short subsector sector
 * name for reference. <br>
 * The next section is the links section. 
 * This is optional and is terminated by the header shown. Each link line 
 * begins with a '$'. The [hex 1] and [hex 2] are sector hex references. The 
 * [x dir] and [y dir] are normally 0, but if the link goes out of the subsector
 * these indicated which direction off the map the link goes. The link reader
 * ignores these references unless the link goes outside the sector map. 
 * <br>
 * The last section is the subsector planetary information. This is read by the
 * {@link SectorTextReader}code. 
 */
public class GalDATReader extends SectorTextReader
{
    private final String SECTOR = "SECTOR";
    private ProviderRecord provider = new ProviderRecord("gal", "Galactic 2.4 by Jim Vassilakos", "Galactic 2.4");
    private TableRecord colorTable;
    boolean skipHeader = false;
    
    /**
     * Constructor, using a file name to read from. 
     * @param file the file to read from.
     */
    public GalDATReader (String file) { super(file); }
    /**
     * Constructor, using a <code>File</code> to read from.
     * @param file the file to read from.
     * @see java.io.File
     */
    public GalDATReader (File  file) { super(file); } 
    
    /**
     * Constructor, using a <code>BufferedReader</code> of an already opened 
     * file to read from.
     * @param stream The buffered stream.
     */
    public GalDATReader (BufferedReader stream) { super(stream); } 
    
    /**
     * Constructor, using a <code>BufferedReader</code> of an already open file,
     * and an existing <code>Astrogation</code> data to append to.
     * @param stream The buffered stream.
     * @param data An existing data set to append.
     */
    public GalDATReader (BufferedReader stream, Astrogation data) 
    {
        super (stream);
        this.data = data;
        skipHeader = true;
    }
    
    public void readHeader() throws IOException
    {
        StringBuffer name;
        sector = new GroupRecord();
        sector.setType(GroupType.SECTOR);
        sector.setLocation(new HexID ("0101"));
        
        subsector = new GroupRecord();
        subsector.setType(GroupType.SUBSECTOR);
        subsector.setLocation(new HexID("0101"));
        
        if (!skipHeader)
        {
            data.setProvider(provider);
            data.setDefaultProvider(provider);
        }

        parser.commentChar('#');
        /* First line is "@SUB-SECTOR: <name> SECTOR: <name> */
        parser.nextToken (); /* @ */
        parser.nextToken(); /* SUB-SECTOR */
        parser.nextToken(); /* : */
        parser.nextToken();
        name = new StringBuffer();
        while (!SECTOR.equals(parser.sval))
        {
            name.append(" ");
            if (parser.ttype == StreamTokenizer.TT_NUMBER)
            {
                name.append (parser.nval);
            }
            else if (parser.ttype == StreamTokenizer.TT_WORD)
            {
                name.append(parser.sval);
            }
            else
            {
                name.append (parser.ttype);
            }
            parser.nextToken(); /* <name> */
        }        
        subsector.setName(name.toString().trim());
        parser.nextToken(); /* : */
        parser.nextToken(); /* <name> */
        name = new StringBuffer();
        while (parser.ttype != StreamTokenizer.TT_EOL)
        {
            name.append (" ");
            if (parser.ttype == StreamTokenizer.TT_NUMBER)
            {
                name.append ((int)parser.nval);
            }
            else if (parser.ttype == StreamTokenizer.TT_WORD)
            {
                name.append(parser.sval);
            }
            else
            {
                name.append (parser.ttype);
            }
            parser.nextToken();
        }
        sector.setName(name.toString().trim());
        
        sector.setKey(sector.getName().substring(0,4).toLowerCase() + ".0000");
        sector.setValue(sector.getName());
        sector.setProvider(data.getDefaultProvider());
        sector.getLocation().setHexGroup(sector);
        sector.getLocation().setHexType(GroupType.GROUP);
        sector.setExtentX(32);
        sector.setExtentY(40);

        subsector.setKey(sector.getName().substring(0,4).toLowerCase() + ".0016");
        subsector.setValue(subsector.getName());
        subsector.setProvider(data.getDefaultProvider());
        subsector.getLocation().setHexGroup(subsector);
        subsector.getLocation().setHexType(GroupType.GROUP);
        subsector.setExtentX(8);
        subsector.setExtentY(10);
        subsector.setParent(sector);

        if (!skipHeader)
        {
            data.addGroup(sector);
            data.addGroup(subsector);
            subsector.setParent(sector);
        }
    } 

    public void readLinks() throws IOException, SECFileStateMachineException
    {
        HexID startHex;
        HexID endHex;
        Links newLink;
        int linkCount = 0;
        int offsetX, offsetY;
        int color;
        
        /* two or Three comment lines: Jump Routes */
        parser.nextToken();
        while (parser.ttype == StreamTokenizer.TT_EOL) { parser.nextToken(); }
        if (parser.ttype == '$')
        {
            parser.pushBack();
        }
        else // no longer end of line, but not a link line either.
        {
            parser.pushBack();
            return;
        }
        /* Items are: $<start hex> <end hex> <subsector x> <subsector y> <link color> */
        while (true)
        {
            color = -1;
            parser.nextToken(); /* $ */
            if (parser.ttype == StreamTokenizer.TT_EOL) break;
            if (parser.ttype != '$')
                throw new SECFileStateMachineException ("GAL DAT Links out of synch at line: " + parser.lineno());
            parser.nextToken();
            startHex = new HexID((int)(parser.nval));
            startHex.setHexGroup(data.getGroup(0));
            parser.nextToken();
            endHex = new HexID ((int)(parser.nval));
            // if the subsector is not zero, the end hex is outside the subsector
            // and the line crosses subsector boundry. 
            // If this link goes outside the subsector, we need to mark it so
            parser.nextToken(); /* Subsector x */
            offsetX = (int)parser.nval;
            parser.nextToken(); /* Subsector y */
            offsetY = (int)parser.nval;
            if ((startHex.x  + (offsetX * 8) < 0))
            {
                endHex.setHexGroup ("extern.0000");
                endHex.setHexType(GroupType.SECTOR);
            }
            else if (startHex.x + (offsetX * 8) > 32)
            {
                endHex.setHexGroup ("extern.0002");
                endHex.setHexType(GroupType.SECTOR);
            }
            else if (startHex.y + (offsetY * 10) < 0)
            {
                endHex.setHexGroup ("extern.0001");
                endHex.setHexType(GroupType.SECTOR);

            }
            else if (startHex.y + (offsetY * 10) > 40)
            {
                endHex.setHexGroup ("extern.0003");
                endHex.setHexType(GroupType.SECTOR);
            }
            else
            {
                endHex.setHexGroup(data.getGroup(0));
            }

            parser.nextToken(); /* link color || TT_EOL*/
            if (parser.ttype != StreamTokenizer.TT_EOL) 
            {
                color = (int)parser.nval;
                parser.nextToken(); /* TT_EOL */
            }
            newLink = new Links();
            newLink.setKey(TableRecordKey.LINKS.toString() + "." + Integer.toString(linkCount));
            newLink.setProvider(data.getDefaultProvider());
            newLink.setValue("Galactic Link for " + subsector.getName() );
            newLink.add(startHex);
            newLink.add(endHex);
            newLink.setLinkColor(getGalacticColor (color));
            data.addLink(newLink);
            linkCount++;
        }
    }
    
    private String getGalacticColor(int color)
    {
        if (colorTable == null) 
            colorTable = data.getGlobalReferences().getTable("gal.color");
            //ViewTableData.getInstance().getGlobalReferences().getTable("gal.color"); 
        if (colorTable != null && color >= 0)
        {
            String key = TableRecordKey.COLOR.toString() + "." + (color < 10 ? "0" : "") + String.valueOf (color);
            return colorTable.getRecord (key).getCode();
        }
        return null;
    }
    
    /**
     * A test run menthod, reads the file in, but otherwise does nothing with it.
     * @param args file name of the DAT file to read. 
     */
    public static void main(String[] args)
    {
        GalDATReader readDATFile;
       if (args.length > 0)
            readDATFile = new GalDATReader (args[0]);
        else
            readDATFile = new GalDATReader("C:\\thom\\projects\\cartography\\SEC_P.DAT");
        try
        {
            readDATFile.read();
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }

}