package stellar.data;
import stellar.dialog.ViewTableData;

public class UWP 
{
    private char port;
    private char size;
    private char atmosphere;
    private char hydrograph;
    private char population;
    private char government;
    private char lawlevel;
    private char technology;

    public UWP()
    {
    }

    public UWP (UWP newUWP)
    {
        this.port = newUWP.getPort();
        this.size = newUWP.getSize();
        this.atmosphere = newUWP.getAtmosphere();
        this.hydrograph = newUWP.getHydrograph();
        this.population = newUWP.getPopulation();
        this.government = newUWP.getGovernment();
        this.lawlevel = newUWP.getLawlevel();
        this.technology = newUWP.getTechnology();
    }
    
    public UWP (String input)
    {
        this.parseUPW(input);
    }
    /**
     * parses a UWP string "A123456-7" into the constitutient
     * parts. 
     */
    public void parseUPW (String input)
    {
        setPort(input.charAt(0));
        size = input.charAt(1);
        atmosphere = input.charAt(2);
        hydrograph = input.charAt(3);
        population = input.charAt(4);
        government = input.charAt(5);
        lawlevel = input.charAt(6);
        technology = input.charAt(8);
    }

    /**
     * returns the UWP in the canon "A123456-7" style
     */
    public String toString()
    {
        char[] bytes = new char[9];
        bytes[0] = port;
        bytes[1] = size;
        bytes[2] = atmosphere;
        bytes[3] = hydrograph;
        bytes[4] = population;
        bytes[5] = government;
        bytes[6] = lawlevel;
        bytes[7] = '-';
        bytes[8] = technology;
        return new String (bytes);
    }

    public char getPort()
    {
        return port;
    }
    public int getPortInt() 
    {
        int portRecord;
        TableRecord t = ViewTableData.getInstance().getTableRecord(TableRecordKey.PORTS);
        if (t == null) return 0;
        portRecord = t.indexOf(t.getRecordCode(Character.toString(port)));
        return portRecord;
    }


    public void setPort(char newPort) 
    {
        port = newPort;
    }

    public char getAtmosphere()
    {
        return atmosphere;
    }
    public int getAtmosphereInt() { return Character.digit(atmosphere, 16); } 
    
    public void setAtmosphere(char newAtmosphere)
    {
        atmosphere = newAtmosphere;
    }

    public char getGovernment() { return government; }
    public int getGovernmentInt() { return Character.digit(government, 16); }
    public void setGovernment(char newGovernment) { government = newGovernment; }

    public char getHydrograph()
    {
        return hydrograph;
    }

    public int getHydrographInt() { return Character.digit(hydrograph, 16); } 
    
    public void setHydrograph(char newHydrograph)
    {
        hydrograph = newHydrograph;
    }

    public char getLawlevel() { return lawlevel; }
    public int getLawLevelInt() { return Character.digit(lawlevel, 20); } 
    public void setLawlevel(char newLawlevel)
    {
        lawlevel = newLawlevel;
    }

    public char getPopulation()
    {
        return population;
    }

    public int getPopulationInt() { return Character.digit(population,16); }

    public void setPopulation(char newPopulation)
    {
        population = newPopulation;
    }

    public char getSize()
    {
        return size;
    }

    public int getSizeInt() { return Character.digit(size,16); }
    
    public void setSize(char newSize)
    {
        size = newSize;
    }

    public char getTechnology()
    {
        return technology;
    }

    public int getTechnologyInt() { return  Character.digit (technology, 20); }
    public void setTechnology(char newTechnology)
    {
        technology = newTechnology;
    }
    
}