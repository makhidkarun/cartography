package stellar.data;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class LinkLine 
{
    private Links link;
    private Line2D line;
    
    public LinkLine()
    {
    }
    
    public LinkLine (Links link, Line2D line)
    {
        this.link = link;
        this.line = line;
    }
    
    public Rectangle getBounds () 
    {
        Rectangle bound = line.getBounds();
        if (bound.width < 5)
        {
            bound.width = 5;
        }
        if (bound.height < 5)
        {
            bound.height = 5;
        }
        return bound;
    }
    public double ptLineDistSq (int x, int y) { return line.ptLineDistSq((double)x, (double)y); }
    public Links getLink() { return link; } 
    
}