package stellar.generator;
import stellar.data.Astrogation;
import stellar.data.HexID;
import stellar.data.StarSystem;
import stellar.data.UWP;

public class Book3Generate implements StarSystemGenerate
{
    private Astrogation data;
    private long seed;
    
    char starport[] = {'A', 'A', 'A', 'B', 'B', 'C', 'C', 'D', 'E', 'E', 'X'};
    char list[] =     {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    
    public Book3Generate()
    {
    }
    
    public void setSeed(long seed) { this.seed = seed; }
    
    public StarSystem create()
    {
        UWP planet = new UWP();
        StarSystem system = new StarSystem();
        DiceRoller dice = new DiceRoller(seed);
        
        planet.setPort(starport[dice.roll(2,6,-2)]);
        planet.setSize(list[dice.roll(2,6,-2)]);
        if (planet.getSizeInt() == 0) planet.setAtmosphere(list[0]);
        else
        {
            int atmo = dice.roll(2,6,planet.getSizeInt()-7);
            if (atmo < 0) atmo = 0;
            if (atmo > 12) atmo = 12;
            planet.setAtmosphere(list[atmo]);
        }
        if (planet.getSizeInt() == 0 || planet.getSizeInt() == 1)
        {
            planet.setHydrograph(list[0]);
        }
        else
        {
            int hydro = dice.roll(2,6,planet.getSizeInt()-7);
            if (planet.getAtmosphereInt() == 0 || planet.getAtmosphereInt() == 1 ||
                planet.getAtmosphereInt() > 9)
            {
                hydro -= 4;
            }
            if (hydro < 0) hydro = 0;
            if (hydro > 10) hydro = 10;
            planet.setHydrograph(list[hydro]);
        }
        
        planet.setPopulation(list[dice.roll(2,6,-2)]);
        
        int gov = dice.roll(2,6,planet.getPopulationInt() - 7);
        if (gov < 0) gov = 0;
        planet.setGovernment(list[gov]);
        
        int law = dice.roll(2,6, planet.getGovernmentInt()-7);
        if (law < 0) law = 0;
        if (law > 9) law = 9;
        planet.setLawlevel(list[law]);
        
        int tech = dice.roll (1,6,0);
        if (planet.getPort() == 'A') tech +=6;
        if (planet.getPort() == 'B') tech +=4;
        if (planet.getPort() == 'C') tech +=2;
        if (planet.getPort() == 'X') tech -=4;
        if (planet.getSizeInt() <= 1) tech += 1;
        if (planet.getSizeInt() <= 4) tech += 1;
        if (planet.getAtmosphereInt() <= 3) tech += 1;
        if (planet.getAtmosphereInt() >=10) tech +=1;
        if (planet.getHydrographInt() >=9) tech +=1;
        if (planet.getHydrographInt() >= 10) tech +=1;
        if (planet.getPopulationInt() <= 5) tech += 1;
        if (planet.getPopulationInt() == 0) tech -= 1;
        if (planet.getPopulationInt() >= 9) tech += 2;
        if (planet.getPopulationInt() >= 10) tech += 2;
        if (planet.getGovernmentInt() == 0) tech += 1;
        if (planet.getGovernmentInt() == 5) tech += 1;
        if (planet.getGovernmentInt() == 13) tech -= 1;
        
        if (tech < 0) tech = 0;
        planet.setTechnology(Integer.toString(tech,20).charAt(0));
        system.setPlanet(planet);
        system.setLocation(new HexID (0,0));
        system.setKey("Book 3 Generated");
        return system;        
    }

}