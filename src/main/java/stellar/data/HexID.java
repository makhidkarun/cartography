/*
 * $Id$
 * Copyright 2006 Softstart Services Inc.
 */
package stellar.data;

/**
 * HexID holds a location, in x,y[,z] format. As currently used these are not
 * cartesian coordinates, rather Hexagon IDs (hence the name). The difference 
 * requires some odd calculations for distance. And the original 2D hex layout 
 * would place "ID" of the format of XXYY in the hexes (sometimes). In addition
 * to the x/y coordinates, this needs to convert to/from the XXYY string. 
 * <p> 
 * There are places where <code>HexID</code> is used as a <code>Point</code>, a 
 * true x/y location. 
 * @version $Revision: 1.5 $
 * @author $Author$
 */
public class HexID 
{
    public int x;
    public int y;
    int z;
    private String hex;
    private GroupType hexType;
    private String hexGroup;
    private String date;


    public HexID (int x, int y) { this.x = x; this.y = y; convertIDtoHex();}
    public HexID (String newHex) { hex = newHex; convertHextoID(); }
    public HexID (int xy) 
    { 
        y = xy % 100;
        x = (xy / 100);
        convertIDtoHex();
    }
    /**
     * Default constructor
     */
    public HexID () { this (0,0); } 
    
    /**
     * Copy constructor
     * @param aHex hex to copy in the constructor
     */
    public HexID (HexID aHex)
    {
        this.x = aHex.x;
        this.y = aHex.y;
        this.z = aHex.z;
        convertIDtoHex();
        this.hexType = aHex.getHexType();
        this.hexGroup = new String (aHex.getHexGroup());
        this.date = new String (aHex.getDate());
    }
    public String toString()
    {   
        if (hex == null)
        {
            convertIDtoHex();
        }
        return hex;
    }

    public void convertHextoID()
    {
        if (hex == null)
        {
            x = 0; y = 0;
        }
        else if (hex.length() != 4) 
        {
            x = 0; y = 0;
        }
        else
        try
        {
            x = Integer.parseInt(hex.substring(0,2));
            y = Integer.parseInt(hex.substring(2,4));
        }
        catch (NumberFormatException ex)
        {
            //ex.printStackTrace();
            x=0; y=0;
        }
    }

    public void convertIDtoHex()
    {

        if (x < 10 & y < 10)        hex = "0" + Integer.toString(x) + "0" +  Integer.toString(y);
        else if (x >= 10 & y < 10)  hex = Integer.toString(x) + "0" + Integer.toString(y);
        else if (x < 10 & y >= 10)  hex = "0" + Integer.toString(x) + Integer.toString(y);
        else if (x >= 10 & y >= 10) hex = Integer.toString(x) + Integer.toString(y);
        if (x < 0 || y < 0)         hex = "0000";
        if (x > 99 || y > 99)       hex = "0000";
    }

    public boolean isValid() 
    {
        return ((hex != null) || (x > 0 && y > 0));
    }

    public GroupType getHexType() { return hexType; }
    public void setHexType(GroupType hexType) { this.hexType = hexType; }
    public String getHexGroup() { return hexGroup; }
    public void setHexGroup(String hexGroup) { this.hexGroup = hexGroup; }
    public String getDate() { return date;  }
    public void setDate(String date)  { this.date = date; }
    public String getHex() { return hex; }
    public void setHex(String hex) { this.hex = hex; convertHextoID(); }
    
    public void setHexGroup (GroupRecord group)
    {
        hexGroup = group.getKey();
        hexType = group.getType();
    }
    
    public static int distance (HexID hex1, HexID hex2)
    {
        int a1 = (hex1.x/2) + (hex1.y);
        int a2 = (hex2.x/2) + (hex2.y);
        
        int d1 = Math.abs (a1 - a2);
        int d2 = Math.abs (hex1.x  - hex2.x);
        int d3 = Math.abs ((a1 - hex1.x) - (a2 - hex2.x));

        if ((d1 >= d2) && (d1 >= d3))
        {
            return d1;
        }
        if ((d2 >= d1) && (d2 >= d3))
        {
            return d2;
        }
        return d3;
        
    }
    public int distance (HexID hex)
    {
        return distance (this, hex);
    }
    
    public boolean equals (Object obj)
    {
        if (obj instanceof HexID) 
        {
            HexID pt = (HexID) obj;
            return (x == pt.x) && (y == pt.y) && (z == pt.z);
        }   
        return super.equals(obj);
    }

}
