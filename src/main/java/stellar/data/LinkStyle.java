package stellar.data;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Stroke;

public class LinkStyle extends Record 
{
    private Color strokeColor;
    private Stroke stroke;
    
    public LinkStyle()
    {
        strokeColor = Color.BLACK;
        stroke = new BasicStroke (2);
    }

    public Color getStrokeColor()
    {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor)
    {
        this.strokeColor = strokeColor;
    }
    
}