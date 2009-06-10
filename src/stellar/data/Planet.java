package stellar.data;
import java.util.ArrayList;

/**
 * Represents a single planet within a star system. Contains specific data about
 * inhabited and uninhabited planets.
 * 
 * @version $Revision: 1.4 $
 * @author $Author: tjoneslo $
 */
public class Planet 
{
    private UWP profile;
    private String name;
    
    private int diameter;
    private float density;
    private float mass;
    private float gravity;
    private float rotationalPeriod;
    private float axialTilt;
    
    private float atmosPressure;
    private String atmoType;
    private String atmoTaint;
    
    private float hydroPercentage;
    private String hydroTaint;
    
    private String climate;
    private float atmoTemperature;
    private float albedo;
    private float greenhouseFactor;
    
    private ArrayList moons;

    private String biosphere;

    
    /**
     * Default Constructor.
     */
    public Planet()
    {
    }

    public UWP getProfile()
    {
        return profile;
    }

    public void setProfile(UWP profile)
    {
        this.profile = profile;
    }

    public int getDiameter()
    {
        return diameter;
    }

    public void setDiameter(int diameter)
    {
        this.diameter = diameter;
    }

    public float getHydroPercentage()
    {
        return hydroPercentage;
    }

    public void setHydroPercentage(float hydroPercentage)
    {
        this.hydroPercentage = hydroPercentage;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
