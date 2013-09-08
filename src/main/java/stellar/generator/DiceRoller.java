package stellar.generator;

import java.util.Random;

public class DiceRoller 
{
    private static DiceRoller instance;
    private Random generator;
    
    public DiceRoller()
    {
        generator = new Random();
    }
    
    public DiceRoller (long seed)
    {
        generator = new Random (seed);
    }
    public static synchronized DiceRoller getInstance()
    {
        if (instance == null) instance = new DiceRoller();
        return instance;
    }

    public int roll (int number, int sides, int modifier)
    {
        int results = 0;
        if (number < 2) return (roll (sides) + 1 + modifier);
        for (int die = 0; die < number; die ++)
        {
            results += roll (sides);
        }
        return results + modifier;
    }
    
    public int roll (int sides)
    {
        return generator.nextInt(sides);
    }
    
    public void setSeed (long seed) { generator.setSeed(seed); } 
}
