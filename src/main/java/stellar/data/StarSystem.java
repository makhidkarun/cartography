package stellar.data;

import java.util.ArrayList;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import org.jibx.runtime.JiBXException;


public class StarSystem
{
    private String key;
    private String providerName;
    private ProviderRecord provider;
    
    private GroupRecord sector;
    HexID  location;
    
    String name;
    String tradeCodes;
    private String remarks;
    private String polity;
    private HTMLDocument comment;

    private UWP planet;    

    private char base;
    private char zone;
    
    int multiplier;
    private int belts;
    int giants;

    private ArrayList stars;
    
    /*
    public static final int MARKER_NONE = -1;
    public static final int MARKER_PRESENCE = 0;
    public static final int MARKER_PORT = 1; 
    public static final int MARKER_SIZE = 2;
    public static final int MARKER_ATMO = 3; 
    public static final int MARKER_HYDRO = 4;
    public static final int MARKER_POP = 5;
    public static final int MARKER_GOV = 6;
    public static final int MARKER_LAW = 7;
    public static final int MARKER_TECH = 9;
    public static final int MARKER_PRIMARY = 10;
    public static final int MARKER_POLITY = 11; 
    public static final int MARKER_REVERSE = 12;
    */
    public StarSystem()
    {
        location = new HexID();
        stars = new ArrayList(5);
    }

    public static char deserializeMaybeEmptyZone(String text) throws JiBXException
    {
        if (text.length() == 0)
        {   
            return ' ';
        } 
        else 
        {
            return text.charAt(0);
        }    
    }

    public static String serializeMaybeEmptyZone (char zone) throws JiBXException
    {
        if (zone == ' ') {return new String("");}
        else {return Character.toString(zone);}
    }

    public boolean isValidSystem () 
    { 
        return location.isValid() & name != null & planet != null;
    }

    public void update()
    {
        return;
    }
    
    public String toString () 
    {
        StringBuffer temp;
        // Name Location UPP Base Codes Zone Polity
        temp = new StringBuffer();
        temp.append(location.toString());
        temp.append(" ");
        temp.append( (name != null) ? name : "");
        temp.append(" ");
        temp.append(planet.toString());
        temp.append(" ");
        temp.append( (polity != null) ? polity : "" );
        temp.append(" ");
        temp.append( (tradeCodes != null) ? tradeCodes : "");
        temp.append( (remarks != null) ? remarks : "" );
        return temp.toString().trim();
    }
    
    public boolean hasPrimary() { return (stars != null) && (stars.size() >= 1) && (stars.get(0) != null); }
    public void setPrimary (Star primary) 
    { 
        if (hasPrimary()) stars.set(0, primary);
        else 
        {
            if (stars == null) stars = new ArrayList(5);
            stars.add(0, primary); 
        }
    } 
    public String getPrimary () 
    { 
        if (hasPrimary())
            return ((Star)(stars.get(0))).toString(); 
        else
            return "";
    } 
    public Star getPrimaryStar() { return (hasPrimary())?(Star)stars.get(0):null; } 
    public void clearPrimary () { if (hasPrimary()) stars.set(0, null); }

    public boolean hasCompanion() { return (stars != null) && (stars.size() >= 2) && (stars.get(1) != null); }
    public Star getCompanionStar() { return (hasCompanion())?(Star)stars.get(1):null; } 
    public void setCompanion (Star companion) 
    { 
        if (hasCompanion()) stars.set(1, companion);
        else stars.add (1, companion);
    }
    
    public String getCompanion() 
    { 
        if (hasCompanion()) 
            return ((Star)(stars.get(1))).toString(); 
        else
            return "";
    }

    public void clearCompanion () { if (hasCompanion()) stars.set(1, null); } 
    
    public boolean hasTertiary() { return (stars != null) && (stars.size() >= 3) && (stars.get(2) != null); }
    public Star getTertiaryStar() { return (hasTertiary())?(Star)stars.get(2):null; } 
    public void setTertiary (Star tertiary) 
    { 
        if (hasTertiary()) stars.set(2, tertiary);
        else stars.add(2, tertiary); 
    } 

    public String getTertiary()
    {
        if (hasTertiary())
            return ((Star)(stars.get(2))).toString();
        else
            return "";
    }

    public void clearTertiary () { if (hasTertiary())stars.set(2, null); }
    
    public char getBase() { return base; }
    public void setBase(char newBase) { base = newBase; }
    public char getZone() { return zone; } 
    public void setZone(char newZone) { zone = newZone; }
    
    public int getBelts()
    {
        return belts;
    }

    public void setBelts(int newBelts)
    {
        belts = newBelts;
    }

    public int getGiants()
    {
        return giants;
    }

    public void setGiants(int newGiants)
    {
        giants = newGiants;
    }

    public UWP getPlanet()
    {
        return planet;
    }

    public void setPlanet(UWP newPlanet)
    {
        planet = newPlanet;
    }

    public String getPolity()
    {
        return polity;
    }

    public void setPolity(String newPolity)
    {
        polity = newPolity;
    }

    public int getSystemMarkerInt (SystemMarkerType marker)
    {
        switch (marker)
        {
            case NONE: return SystemMarkerType.NONE.ordinal(); 
            case PRESENCE: return SystemMarkerType.PRESENCE.ordinal();
            case PORT: return planet.getPortInt();
            case SIZE: return planet.getSizeInt();
            case ATMOSPHERE: return planet.getAtmosphereInt();
            case HYDROGRAPHIC: return planet.getHydrographInt();
            case POPULATION: return planet.getPopulationInt();
            case GOVERNMENT: return planet.getGovernmentInt();
            case LAWLEVEL: return planet.getLawLevelInt();
            case TECHNOLOGY: return planet.getTechnologyInt();
            case PRIMARY:
                if (getPrimaryStar() == null) return SystemMarkerType.PRESENCE.ordinal();
                switch (getPrimaryStar().getSpectrum())
                {
                    case 'M': return 1;
                    case 'K': return 3;
                    case 'G': return 5; 
                    case 'F': return 11;
                    case 'A':
                    case 'B': 
                    case 'O': return 9; 
                    default: return SystemMarkerType.NONE.ordinal(); 
                }
            case REVERSE: return 10;                
            default: return SystemMarkerType.NONE.ordinal(); 
        }        
    }

    public String getSystemMarkerKey (SystemMarkerType marker)
    {
        switch (marker)
        {
            case NONE: return null; 
            case PRESENCE: return null;
            case PORT: return String.valueOf (planet.getPort()); 
            case SIZE: return String.valueOf(planet.getSize());
            case ATMOSPHERE: return String.valueOf(planet.getAtmosphere());
            case HYDROGRAPHIC: return String.valueOf(planet.getHydrograph());
            case POPULATION: return String.valueOf(planet.getPopulation());
            case PRIMARY: return String.valueOf(getPrimaryStar().getSpectrum());
            case REVERSE: return null;                
            default: return null; 
        }        
        
    }
    public int getMultiplier()
    {
        return multiplier;
    }

    public void setMultiplier(int multiplier)
    {
        this.multiplier = multiplier;
    }

    public String getName()
    {
        return name;
    }

    public String getTradeCodes()
    {
        return tradeCodes;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public HexID getLocation()
    {
        return location;
    }

    public void setLocation(HexID location)
    {
        this.location = location;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTradeCodes(String tradeCodes)
    {
        this.tradeCodes = tradeCodes;
    }

    public HTMLDocument getComment() { return comment; }
    public void setComment (Document comment) { this.comment = (HTMLDocument)comment; } 

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public GroupRecord getSector()
    {
        return sector;
    }

    public void setSector(GroupRecord sector)
    {
        this.sector = sector;
        this.provider = sector.getProvider();
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
