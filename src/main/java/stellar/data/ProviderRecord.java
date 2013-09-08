package stellar.data;

public class ProviderRecord extends Record 
{
    String link;
    String email;
    String name;
    
    public ProviderRecord()
    {
    }

    public ProviderRecord (String key, String value, String name)
    {
        this.setKey(key);
        this.setValue(value);
        this.name = name; 
    }

    public String getProviderName () { return name; } 

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }
}