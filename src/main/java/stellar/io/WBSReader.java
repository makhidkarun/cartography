package stellar.io;
import stellar.data.GroupRecord;
import stellar.data.GroupType;
import stellar.data.HexID;
import stellar.data.ProviderRecord;
import stellar.data.References;
import stellar.data.StarSystem;
import stellar.data.UWP;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;

public class WBSReader extends SectorTextReader
{
    public WBSReader (String file) { super(file); } 
    public WBSReader (File file) { super(file);  }
    
    public void readHeader() throws IOException, SECFileStateMachineException
    {
        int linecount = 0; 
        ProviderRecord provider = null;

        References ref = data.getGlobalReferences();
        data.setProvider(ref.getProvider("wbd"));

        sector = new GroupRecord();
        sector.setType(GroupType.SECTOR);
        sector.setName (inputFile.getName().substring(0,inputFile.getName().length()-4));
        sector.setKey(sector.getName().substring(0,4).toLowerCase() + ".0000");
        sector.setValue(sector.getName());
        sector.setProvider(provider);
        sector.setLocation(new HexID (1,1));
        sector.getLocation().setHexType(GroupType.GROUP);
        sector.getLocation().setHexGroup(sector);
        sector.setExtentX(32);
        sector.setExtentY(40);
        data.addGroup(sector);
/*        
WBS File Format (V1.1.0)
------------------------

Hex Location    = 00 - 03
World Name      = 06 - 19
UWP Code        = 22 - 30
Trade Code      = 33 - 43
PBG Code        = 47 - 49
Base Code       = 52
Allegiance Code = 55 - 56
Satellite Code  = 59
Stellar Details = 62 - 81

00000000001111111111222222222233333333334444444444555555555566666666667777777777888
01234567890123456789012345678901234567890123456789012345678901234567890123456789012
*/        
        
        parser.nextToken();
        if (!(parser.ttype == StreamTokenizer.TT_WORD && parser.sval.equals("WBS")))
        {
            throw new SECFileStateMachineException ("WBS File header not found");
        }

        while (true)
        {
            parser.nextToken();
            if (parser.ttype == StreamTokenizer.TT_EOL) linecount++;
            if (linecount == 15) break;
        }
    }
    public void readLinks() throws IOException, SECFileStateMachineException {}

    public void readData() throws IOException, SECFileStateMachineException
    {
        States state; 
        boolean found = false;
        StarSystem thisSystem;
        UWP uwp;
        String lastName = "";

        state = States.HEXID;
        thisSystem = new StarSystem();
        
        while (parser.ttype != StreamTokenizer.TT_EOF)
        {
            if (parser.ttype == StreamTokenizer.TT_EOL)
            {
                state = States.HEXID;
                lastName = "";
                // skip comment lines
                while (parser.ttype == StreamTokenizer.TT_EOL)
                {
                    parser.nextToken();
                }
                if (thisSystem.isValidSystem())
                {
                    if (thisSystem.getBase() == '\0') thisSystem.setBase(' ');
                    if (thisSystem.getZone() == '\0') thisSystem.setZone(' ');
                    if (thisSystem.getTradeCodes() == null) thisSystem.setTradeCodes("");
                    if (thisSystem.getRemarks() == null) thisSystem.setRemarks("");
                    //systems.add(thisSystem);
                    data.addSystem(thisSystem);
                }
                thisSystem = new StarSystem();
                thisSystem.setSector(sector);
            }
            switch (state)
            {
                case HEXID: /* Hex ID */
                    if (parser.ttype == StreamTokenizer.TT_EOF) break;
                    if (parser.ttype != StreamTokenizer.TT_NUMBER) 
                        throw new SECFileStateMachineException ("Line not begining with a hex ID at line: " + parser.lineno());
                    String hexID = Integer.toString((int)parser.nval);
                    if (hexID.length() == 3)
                    {
                        thisSystem.getLocation().setHex("0" + hexID);
                    }
                    else 
                    {
                        thisSystem.getLocation().setHex(hexID);
                    }
                    thisSystem.getLocation().setHexGroup (sector);
                    thisSystem.setKey(sector.getKey() + "_" + thisSystem.getLocation());
                    state = States.NAME_WORD;
                    break;
                case NAME_WORD: /* Name word */
                    if (parser.ttype == StreamTokenizer.TT_WORD)
                    {
                        if (parser.sval.length() == 9 && parser.sval.charAt(7) == '-') 
                        {
                            thisSystem.setName(lastName);
                            parser.pushBack();
                            state = States.UWP;
                        }
                        else
                        {
                            thisSystem.setName(lastName + " " + parser.sval);
                            lastName = thisSystem.getName();
                            state = States.NAME_WORD;
                        }
                    }
                    else if (parser.ttype == StreamTokenizer.TT_NUMBER)
                    {
                            thisSystem.setName(lastName + " " + Double.toString(parser.nval));
                            lastName = thisSystem.getName();
                            state = States.NAME_WORD;
                    }
                    else
                    {
                        thisSystem.setName(lastName.concat(String.valueOf((char)parser.ttype)));
                        lastName = thisSystem.getName();
                        state = States.NAME_WORD;
                    }
                    if (thisSystem.getName().length() > 13)
                        throw new SECFileStateMachineException ("System name: " + thisSystem.getName() + " is too long at line: " + parser.lineno());
                    break; 
                case UWP: /* UWP  */
                    if (parser.ttype == StreamTokenizer.TT_WORD)
                    {
                        if (parser.sval.length() == 9 && parser.sval.charAt(7) == '-') 
                        {
                            /* Set the UWP into the system.UWP */
                            uwp = new UWP();
                            uwp.parseUPW(parser.sval);
                            thisSystem.setPlanet(uwp);
                            thisSystem.setName(thisSystem.getName().trim());
                        }
                        else throw new SECFileStateMachineException ("State machine failure at line: " + parser.lineno());
                    }
                    else throw new SECFileStateMachineException ("State machine failure at line: " + parser.lineno());
                    state = States.REMARK;    
                    break;
                    
                case REMARK: /* trade code & remarks */
                    if (parser.ttype == StreamTokenizer.TT_NUMBER)
                    {
                        if (found) 
                        {
                            thisSystem.setTradeCodes(thisSystem.getTradeCodes() + Integer.toString((int)parser.nval));
                            found = false;
                        }
                        else
                        {
                            parser.pushBack(); 
                            state = States.PBG;
                        }
                    }
                    else if (parser.ttype == StreamTokenizer.TT_WORD)
                    {
                        if (thisSystem.getTradeCodes() == null) {thisSystem.setTradeCodes(parser.sval);}
                        else { thisSystem.setTradeCodes(thisSystem.getTradeCodes() + " " + parser.sval); }
                        if (parser.sval.charAt(0) == 'O') /* Owned marker */
                        { found = true; } 
                    }
                    else if (parser.ttype == ':' && found)
                    {
                        thisSystem.setTradeCodes(thisSystem.getTradeCodes() + (char)parser.ttype);
                    }
                    else 
                        throw new SECFileStateMachineException ("State machine failure at line: " + parser.lineno());
                    break;
                case PBG: /* PBG CODE */
                    readPBGCode (thisSystem);
                    state = States.BASE; 
                    break;

                case BASE: /* base code */
                    if (parser.ttype == StreamTokenizer.TT_WORD)
                    {
                        if (parser.sval.length() == 1)
                        {
                            thisSystem.setBase(parser.sval.charAt(0));
                        }
                        else
                        {
                            parser.pushBack();
                        }
                    }
                    else if (parser.ttype == StreamTokenizer.TT_NUMBER)
                    {
                        throw new SECFileStateMachineException ("State machine failure at line: " + parser.lineno());
                    }
                    state = States.ALEG;
                    break;
                    
                case ALEG: /* Polity code(s) */
                    if (parser.ttype != StreamTokenizer.TT_WORD)
                        throw new SECFileStateMachineException ("State machine failure: Expecting Allegiance code but not found at line: " + parser.lineno());
                    thisSystem.setPolity (parser.sval);
                    state = States.SATELLITE;
                    break;
                case SATELLITE: /* optional satellite code */
                    if (parser.ttype == StreamTokenizer.TT_WORD)
                    {
                        if (parser.sval.length() == 1)
                        {
                            /* We have a code but ignore it for now*/
                        }
                        else parser.pushBack();
                    }
                    else
                        throw new SECFileStateMachineException ("State machine failure at line: " + parser.lineno());
                    state = States.PRIMARY_CODE;
                    break;
                    
                case UNUSED: /* Unused */
                case PRIMARY_CODE: /* Primary Type */
                    if (parser.ttype == StreamTokenizer.TT_NUMBER) /* LRX CODE */
                    {
                        parser.pushBack(); state = States.LRX; break;
                    }
                    if (parser.ttype == StreamTokenizer.TT_WORD) 
                    {
                        if (parser.sval.length() == 2)
                        {
                            thisSystem.setPrimary(readStar());
                            state = States.PRIMARY_TYPE;
                            break;
                        }
                        else
                        {
                            parser.pushBack(); state = States.LRX; break; /* LRX CODE */
                        }
                    }
                    state = States.ERROR;
                    break;
                case PRIMARY_TYPE: /* Primary Luminosity */
                    thisSystem.getPrimaryStar().setLuminosity (parser.sval);
                    state = States.SECONDARY_CODE;
                    break;
                case SECONDARY_CODE: /* Companion Type */
                    thisSystem.setCompanion (readStar());
                    state = States.SECONDARY_TYPE;
                    break;
                case SECONDARY_TYPE: /* Companion Luminosity */
                    thisSystem.getCompanionStar().setLuminosity (parser.sval);
                    state = States.TERTIARY_TYPE;
                    break;
                case TERTIARY_TYPE: /* Tertiary Type */
                    thisSystem.setTertiary(readStar());
                    state = States.TERTIARY_CODE;
                    break;
                case TERTIARY_CODE: /* Tertiary Luminosity */
                    thisSystem.getTertiaryStar().setLuminosity (parser.sval);
                    state = States.FINISHED;
                    break;
                case LRX: /* LRX code from Galactic sector files */
                    /* starting with a number may be 1,2,3 digits. */
                    if (parser.ttype == StreamTokenizer.TT_NUMBER)
                    {
                        if (parser.nval < 100.0)
                        {
                            state = States.LRX; break; 
                        }
                    }
                    state = States.FINISHED;
                    break; 
                case FINISHED: /* * code from galactic sector files */
                    /* f for file or m for menu */
                    /* x for world map */
                    /* y for system map */
                    state = States.FINISHED; 
                    break; 
                default:
                    throw new SECFileStateMachineException ("State machine failure at line: " + parser.lineno());
            }
            parser.nextToken();
        }
    }
        
    public static void main(String[] args)
    {
        WBSReader readSECFile = new WBSReader("C:\\thom\\projects\\cartography\\Delphi.WBS");
        try
        {
            readSECFile.read();
        }
        catch (Exception ex) { ex.printStackTrace(); }
    }
    
}