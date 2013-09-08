package stellar.io;
import stellar.dialog.EditOptions;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.io.IOException;
import java.io.StreamTokenizer;
import stellar.data.StarSystem;
import stellar.data.UWP;
import stellar.data.Astrogation;
import stellar.data.GroupRecord;
import stellar.data.Star;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jibx.runtime.JiBXException;

import stellar.MapPreferences;

/**
 * This abstract class reads the generally defined formatted text used for
 * Traveller sector data. There are a number of variants to this data, so this
 * class is abstract, but contains the state machine for reading the base data.
 * Sub-classes will define how to read through the header and extract any useful
 * information.
 * <p>
 * The tokens retrieved are:<br>
 * <pre>
[name] Hex UWP [Base] [Trade/remarks] [Zone] PBG Allegiance [Primary] [primary] [Companion] [Companion] [Teriatry] [Tertiary]
0      1   1   4      5               6      7   8         9         10         11          12          13        14
</pre>
 * Or the Galactic files tokens are:
 * <pre>
[name] Hex UWP [Base] [Trade/Remarks] [Zone] PGB Allegiance [LRX] [*]
0      1   1   4      5               6      7   8          15    16
</pre>
 * Items in brackets are optional
 * Name may have more than one word, meaning it has multiple tokens
 *
 * @version $Id$
 */

public abstract class SectorTextReader implements AstrogationFileReader
{
    private BufferedReader inputStream;
    private URL urlFile;
    File   inputFile;
    Astrogation data;
    StreamTokenizer parser;
    GroupRecord sector;
    GroupRecord subsector;
    
    public SectorTextReader (String filename)
    {
        inputFile = new File (filename);
    }

    public SectorTextReader (File newInputFile)
    {
        inputFile = newInputFile;
    }
    
    public SectorTextReader (URL filename)
    {
        urlFile = filename;
    }
    
    public SectorTextReader (BufferedReader inputStream)
    {
        this.inputStream = inputStream;
    }
    
    /**
     * Abstract method to read the Sector file header information. On some 
     * files this may include sector names, subsector names, locations, or 
     * other information. Mostly this function is to skip over the (human 
     * readable) header. 
     * @throws SECFileStateMachineException
     * @throws java.io.IOException
     */
    public abstract void readHeader() throws IOException, SECFileStateMachineException;
    
    /**
     * Abstract method to read star system link information, if any. Some 
     * files (specifically the Galactic data files) include link information in
     * the subsector data files. In other cases this will do nothing. 
     * @throws SECFileStateMachineException
     * @throws java.io.IOException
     */
    public abstract void readLinks() throws IOException, SECFileStateMachineException;

    /**
     * Gets the <code>Astrogation</code> data structure created by parsing the file. 
     * @return The Astrogation data structure.
     */
    public Astrogation getAstrogation() { return data; }

    private Reader openInputFile () throws IOException, FileNotFoundException
    {
        if (inputStream != null) return inputStream;
        if (urlFile != null) return new InputStreamReader (urlFile.openStream());
        if (inputFile != null) return new FileReader (inputFile);
        return null;
    }

    /**
     * Perform the read and parse of the data file. This creates the Astrogation
     * data structure. 
     * @throws org.jibx.runtime.JiBXException Only if reading the global 
     * references file fails because it has become corrupt. 
     * @throws SECFileStateMachineException if reading
     * the sector file encounters unexpected or corrupt data. 
     * @throws java.io.IOException if reading the file causes an IO error. 
     * @throws java.io.FileNotFoundException if the file name or File provided
     * can not be found.
     */
    public void read () throws FileNotFoundException, IOException, 
        SECFileStateMachineException, JiBXException
    {
        Reader r = openInputFile();

        if (data == null) data = new Astrogation(MapPreferences.getInstance().getExternalRefsFileName());
        parser = new StreamTokenizer(r);
        parser.eolIsSignificant(true);
        parser.wordChars('\'', '\'');
        
        readHeader();
        readLinks();
        readDataRegex(r); //readData();
        r.close();
        data.postRead();
    }
    
    /**
     * This method is an experimental variaion on the readData method, which instead
     * of using a state machine and StreamTokenizer, uses the regular expression 
     * engine java.util.regex to parse each line of data into its constituent
     * parts. As the inital experiment, we'll do the standard SEC file format.
     * <pre>
     [name] Hex UWP [Base] [Trade/remarks] [Zone] PBG Allegiance [Primary] [primary] [Companion] [Companion] [Teriatry] [Tertiary]
     0      1   1   4      5               6      7   8         9         10         11          12          13        14
     </pre>
     *   /^(.{14})(\d\d)(\d\d) (.{9})\w+(.) (.{15}) (.)  (\d\d\d) (.{2}) (.*)$/;
     */
    protected void readDataRegex (Reader r) throws IOException, SECFileStateMachineException 
    {
        BufferedReader read;
        //Pattern p = Pattern.compile("(.+)\\s{2}(\\d{4}) (.{9})\\s+(.) (.{15}) (.)\\s+(\\d\\d\\d) (.{2}) (.*)");
        Pattern p = Pattern.compile ("(.+)\\s{2}(.*)");
        Matcher m;
        String line, item; 
        StarSystem thisSystem;
        UWP uwp;

        if (r instanceof BufferedReader) read = (BufferedReader) r;
        else read = new BufferedReader (r);
        
        line = read.readLine();
        while (line != null)
        {
            // skip short lines (i.e. comments and blank lines). 
            if (line.length() < 64 ) {line = read.readLine(); continue; }
            // skip comment lines 
            if (line.charAt(0) == '#') {line = read.readLine(); continue; }
            m = p.matcher(line);
                                    
            item = m.group(1);
    
            thisSystem = new StarSystem();
            thisSystem.setSector(sector);
            thisSystem.setName(m.group(1).trim());
            thisSystem.getLocation().setHex(m.group(2));
            thisSystem.setPlanet(new UWP (m.group(3)));
            // no base is an empty space, which is translated here as well
            thisSystem.setBase(m.group(4).charAt(0));
            
            if (m.group(5).trim().length() > 2)
                thisSystem.setTradeCodes(m.group(5).trim());
            else
            {
                thisSystem.setTradeCodes("");
                thisSystem.setRemarks("");
            }
            // no zone (or Green) is an empty space,which is translated here             
            thisSystem.setZone(m.group(6).charAt(0));
            
            thisSystem.setMultiplier(Integer.parseInt(m.group(7).substring(0,1)));
            thisSystem.setBelts(Integer.parseInt(m.group(7).substring(1,2)));
            thisSystem.setGiants(Integer.parseInt(m.group(7).substring(2,3)));
            
            thisSystem.setPolity(m.group(8));
            
            data.addSystem(thisSystem);
            
            line = read.readLine();
        }
    }
 
    /**
     * This method is the actual state machine parser of the data file. In 
     * general you should never need to call this method (call <code>read</code> 
     * which will call this method.
     * @throws SECFileStateMachineException
     * @throws java.io.IOException
     */
    protected void readData() throws IOException, SECFileStateMachineException
    {
        States state; 
        StarSystem thisSystem;
        UWP uwp;
        String lastName = "";

        state = States.NAME_WORD;
        thisSystem = new StarSystem();
        
        while (parser.ttype != StreamTokenizer.TT_EOF)
        {
            if (parser.ttype == StreamTokenizer.TT_EOL)
            {
                state = States.NAME_WORD;
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
                case NAME_WORD: /* Name word */
                    if (parser.ttype == StreamTokenizer.TT_WORD)
                    {
                        thisSystem.setName(lastName + " " + parser.sval);
                        lastName = thisSystem.getName();
                        state = States.NAME_WORD;
                    }
                    else if (parser.ttype == StreamTokenizer.TT_NUMBER)
                    {
                        if (Math.floor(parser.nval) != parser.nval)
                        {
                            thisSystem.setName(lastName + " " + Double.toString(parser.nval));
                            lastName = thisSystem.getName();
                            state = States.NAME_WORD;
                        }
                        else
                        {
                            lastName = Integer.toString((int)parser.nval);
                            state = States.NAME_NUMBER;
                        }
                    }
                    else
                    {
                        thisSystem.setName(lastName.concat(String.valueOf((char)parser.ttype)));
                        lastName = thisSystem.getName();
                        state = States.NAME_WORD;
                    }
                    break; 
                case NAME_NUMBER: /* Name as number, maybe a hexID and UWP or more name.  */
                    if (parser.ttype == StreamTokenizer.TT_WORD)
                    {
                        /* new word is UWP, lastName should be hexID */
                        if (parser.sval.length() == 9 && parser.sval.charAt(7) == '-') 
                        {
                            /* Set the HEXID value into the System.hex */
                            if (lastName.length() == 3)
                            {
                                thisSystem.getLocation().setHex("0" + lastName);
                            }
                            else 
                            {
                                thisSystem.getLocation().setHex(lastName);
                            }
                            thisSystem.getLocation().setHexGroup (sector);
                            thisSystem.setKey(sector.getKey() + "_" + thisSystem.getLocation());
                            /* Set the UWP into the system.UWP */
                            uwp = new UWP();
                            uwp.parseUPW(parser.sval);
                            thisSystem.setPlanet(uwp);
                            thisSystem.setName(thisSystem.getName().trim());
                            state = States.BASE;
                        }
                        else
                        {
                            if (thisSystem.getName() == null) 
                            {
                                thisSystem.setName(lastName + " " + parser.sval);
                            }
                            else
                            {
                                thisSystem.setName(thisSystem.getName() + " " + lastName + " " + parser.sval);
                            }
                            state = States.NAME_WORD; 
                        }
                    }
                    else if (parser.ttype == StreamTokenizer.TT_NUMBER)
                    {
                        if (thisSystem.getName() == null)
                        {
                            thisSystem.setName(lastName);
                        }
                        else
                        {
                            thisSystem.setName(thisSystem.getName() + lastName); 
                        }
                        lastName = Integer.toString((int)parser.nval);
                        state = States.NAME_NUMBER;
                    }
                    else 
                    {
                        if (thisSystem.getName() == null)
                        {
                            thisSystem.setName (lastName.concat(String.valueOf((char)parser.ttype)));
                        }
                        else 
                        {
                            thisSystem.setName( thisSystem.getName() + " " + lastName.concat(String.valueOf((char)parser.ttype)));
                        }
                        lastName = thisSystem.getName(); 
                        state = States.NAME_WORD;
                    }

                    if (thisSystem.getName().length() > 40)
                        throw new SECFileStateMachineException ("System name: " + thisSystem.getName() + " is too long at line: " + parser.lineno());
                        
                    break;
                case HEXID: /* OLD : Hex ID parser */
                    throw new SECFileStateMachineException ("State machine failure at line: " + parser.lineno());

                case UWP: /* OLD: UWP selection */
                    throw new SECFileStateMachineException ("State machine failure at line: " + parser.lineno());

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
                        parser.pushBack(); state = States.PBG; break;
                    }
                    state = States.REMARK;
                    break;
                case REMARK: /* trade code & remarks */
                    if (parser.ttype == StreamTokenizer.TT_NUMBER)
                    {
                        parser.pushBack(); state = States.REMARK; break;
                    }
                    else if (parser.ttype == StreamTokenizer.TT_WORD)
                    {
                        if (parser.sval.length() == 1)
                        {
                            parser.pushBack(); state = States.ZONE; break;
                        }
                        if (thisSystem.getTradeCodes() == null) {thisSystem.setTradeCodes(parser.sval);}
                        else { thisSystem.setTradeCodes(thisSystem.getTradeCodes() + " " + parser.sval); }
                    }
                    break;
                case ZONE: /* travel zone */
                    if (parser.ttype == StreamTokenizer.TT_WORD)
                    {
                        if (parser.sval.length() == 1)
                        {
                            thisSystem.setZone(parser.sval.charAt(0));
                        }
                        else
                        {
                            parser.pushBack(); state = States.REMARK; break;
                        }
                    }
                    else if (parser.ttype == StreamTokenizer.TT_NUMBER)
                    {
                        parser.pushBack(); state = States.PBG; break;
                    }
                    state = States.PBG; 
                    break;
                case PBG: /* PBG Code */
                    readPBGCode (thisSystem);
                    state = States.ALEG;
                    break;
                    
                case ALEG: /* Polity code(s) */
                    readPolityCode(thisSystem);
                    state = States.PRIMARY_CODE;
                    break;

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
                    state = States.TERTIARY_CODE;
                    break;
                case TERTIARY_CODE: /* Tertiary Type */
                    thisSystem.setTertiary(readStar());
                    state = States.TERTIARY_TYPE;
                    break;
                case TERTIARY_TYPE: /* Tertiary Luminosity */
                    thisSystem.getTertiaryStar().setLuminosity (parser.sval);
                    state = States.ERROR;
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
    
    void readPBGCode (StarSystem thisSystem) throws SECFileStateMachineException
    {
        if (parser.ttype != StreamTokenizer.TT_NUMBER) 
            throw new SECFileStateMachineException ("State machine failure: Expecting PBG code but not found at line: " + parser.lineno());
        int i = (int)parser.nval;
        thisSystem.setGiants(i % 10);
        thisSystem.setBelts ((i / 10) % 10);
        thisSystem.setMultiplier ((i/100) % 10);
    }
    
    void readPolityCode (StarSystem thisSystem) throws SECFileStateMachineException
    {
        if (parser.ttype != StreamTokenizer.TT_WORD)
            throw new SECFileStateMachineException ("State machine failure: Expecting Allegiance code but not found at line: " + parser.lineno());
        thisSystem.setPolity (parser.sval);
    }        

    Star readStar ()
    {
        if (parser.ttype == StreamTokenizer.TT_WORD && parser.sval.length() == 2)
        {
            Star star = new Star();
            /* OBAFGKM : 1234567890 */
            star.setSpectrum(parser.sval.charAt(0));
            star.setModifier(parser.sval.charAt(1));
            return star;
        } else return null;
    }
}
