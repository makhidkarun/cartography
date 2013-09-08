package stellar.data;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

public class Links extends Record
{
    private ArrayList<HexID> hexes;
    private LinkStyle linkStyle;
    private String linkTypeName;
    private TableRowRecord linkType;
    
    public Links()
    {
    }
    public void add (HexID newHex) 
    {
        if (hexes == null) { hexes = new ArrayList<HexID>(); }
        hexes.add(newHex); 
    }

    public Iterator getHexes () { return hexes.iterator(); }
    public HexID getHex (int index) { return hexes.get(index); } 
    
    public void setLinkColor (Color linkColor)
    {
        if (linkStyle == null) linkStyle = new LinkStyle();
        linkStyle.setStrokeColor(linkColor);
        this.setColor (linkColor);
    }
    
    public void setLinkColor (String linkColor)
    {
        setColor (linkColor);
        setLinkColor (this.getColor());
        
    }
    public Color getLinkColor () 
    {
        if (linkStyle != null) return linkStyle.getStrokeColor();
        if (getColor() != null)
        {
            setLinkColor(getColor());
            return linkStyle.getStrokeColor();
        }
        else if (linkType.getColor() != null)
        {
            setLinkColor(linkType.getColor());
            return linkStyle.getStrokeColor();
        }
        return null;
    }


    public String getLinkTypeName () { return linkTypeName; } 
    
    protected void setLinkType (TableRowRecord lt)
    {
        linkType = lt;
        linkTypeName = (lt == null ? null : lt.getKey());
    }
}