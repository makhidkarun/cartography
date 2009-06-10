package stellar.data;

public class Orbit 
{
    private int orbitNumber;
    
    private String type;
    private Planet details;
    private Star   companion;
    private String zone;
    private float distance;
    private float eccentricity;

    public static final String TYPE_PLANET = "planet";
    public static final String TYPE_GASGIANT = "gasgiant";
    public static final String TYPE_BELT = "belt";
    public static final String TYPE_STAR = "star";
    public static final String TYPE_EMTPY = "empty";

    public static final String ZONE_INNER = "inner";    
    public static final String ZONE_LIFE = "life";
    public static final String ZONE_SNOW = "snow";
    public static final String ZONE_OUTER = "outer";
    public static final String ZONE_FORBIDDEN = "forbidden";
    
    public Orbit()
    {
    }

    public String getName()
    {
        return details.getName();
    }

    public void setName(String newName)
    {
        details.setName(newName);;
    }

    public UWP getPlanet()
    {
        return details.getProfile();
    }

    public void setPlanet(UWP newPlanet)
    {
        details.setProfile(newPlanet);
    }

    public String getType()
    {
        return type;
    }

    public void setType(String newType)
    {
        type = newType;
    }

    public int getOrbitNumber()
    {
        return orbitNumber;
    }

    public void setOrbitNumber(int newOrbitNumber)
    {
        orbitNumber = newOrbitNumber;
    }

    public float getDistance()
    {
        return distance;
    }

    public void setDistance(float distance)
    {
        this.distance = distance;
    }

    public float getEccentricity()
    {
        return eccentricity;
    }

    public void setEccentricity(float eccentricity)
    {
        this.eccentricity = eccentricity;
    }

    public String getZone()
    {
        return zone;
    }

    public void setZone(String zone)
    {
        this.zone = zone;
    }
}