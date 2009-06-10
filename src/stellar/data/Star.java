package stellar.data;
import java.util.ArrayList;

public class Star 
{
    private String name; 
    private char spectrum;
    private char modifier;
    private String luminosity;
    
    private int age;
    private float mass;
    private float temperature;
    
    private ArrayList orbits;

    public Star()
    {
    }

    public String toString()
    {
        return 
        String.valueOf (spectrum) + 
        String.valueOf(modifier) + " " + 
        luminosity; 
    }

    public char getSpectrum()
    {
        return spectrum;
    }

    public void setSpectrum(char spectrum)
    {
        this.spectrum = spectrum;
    }

    public char getModifier()
    {
        return modifier;
    }

    public void setModifier(char modifier)
    {
        this.modifier = modifier;
    }

    public String getLuminosity()
    {
        return luminosity;
    }

    public void setLuminosity(String luminosity)
    {
        this.luminosity = luminosity;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public ArrayList getOrbits()
    {
        return orbits;
    }

    public void setOrbits(ArrayList orbits)
    {
        this.orbits = orbits;
    }
    
    public int orbitSize () { return orbits.size(); } 
}