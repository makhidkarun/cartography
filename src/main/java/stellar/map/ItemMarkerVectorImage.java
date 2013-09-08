package stellar.map;
import com.softstart.VectorImage.VectorImage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class ItemMarkerVectorImage extends VectorImage 
{
    public ItemMarkerVectorImage()
    {
        super (new Ellipse2D.Float(0,0,6,6), Color.black, new BasicStroke(1));
        this.setFillShape(false);
    
    }
}