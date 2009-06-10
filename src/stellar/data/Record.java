package stellar.data;
import java.awt.Color;

public class Record implements Comparable<Record>
{
    private String key;
    private String value;
    private ProviderRecord provider;
    private String providerName;
    private String providerKey; 
    private String color;
    
    public Record()
    {
    }

    public Record (Record aRecord)
    {
        this.key = new String(aRecord.getKey());
        this.value = new String(aRecord.getValue());
        setProvider (aRecord.getProvider());
        setColor (aRecord.getColor());
    }
    
    public boolean hasProvider() { return provider != null; } 
    public String getKey() { return key;}
    public String getValue() { return value; }
    public ProviderRecord getProvider() {return provider; }
    public String getProviderName() 
    { 
        return (provider == null) ? providerName : provider.getProviderName();
    }

    public void setKey (String key) { this.key = key; }
    public void setValue (String value) { this.value = value; }
    public void setProvider (ProviderRecord provider) 
    { 
        this.provider = provider; 
        if (provider == null) return;
        providerName = provider.getProviderName();
        providerKey = provider.getKey();
    }

    public String getProviderKey()
    {
        return (provider == null) ? providerKey : provider.getKey();
    }
    
    public int compareTo (Record r)
    {
            return key.compareTo(r.getKey());
    }
    
    public boolean equals (Object o)
    {
        if (! (o instanceof Record)) return false;
        Record e = (Record) o;
        String k1 = getKey();
        String k2 = e.getKey();
        if (k1 == k2 || (k1 != null && k1.equals(k2))) 
        {
            String v1 = getValue();
            String v2 = e.getValue();
            if (v1 == v2 || (v1 != null && v1.equals(v2))) 
                return true;
        }
        return false;
    }

    public int hashCode() 
    {
        return (key == null   ? 0 : key.hashCode()) ^
               (value == null ? 0 : value.hashCode());
    }

    public String getColorString ()  { return color; }
    public void setColor(String color) { this.color = color; }
    public void setColor (Color newColor)
    {
        if (newColor == null) 
            this.color = null;
        else 
            this.color = "0x" + Integer.toHexString(newColor.getRGB()).toUpperCase();
    }
    public Color getColor ()
    {
        if (this.color == null) return null;
        return new Color (Long.decode(this.color).intValue());
    }
}