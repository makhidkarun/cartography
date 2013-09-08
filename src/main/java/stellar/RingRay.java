package stellar;

public class RingRay extends java.awt.Point
{
    public RingRay (int x, int y)
    {
        super (x, y);
    }

    public int getRing ()
    {
        return x;
    }

    public int getRay ()
    {
        return y;
    }

    public void setRing (int ring)
    {
        this.x = ring;
    }

    public void setRay (int ray)
    {
        this.y = ray;
    }

    public String toString ()
    {
        return Integer.toString (x) + ", " + Integer.toString (y);
    }
}
