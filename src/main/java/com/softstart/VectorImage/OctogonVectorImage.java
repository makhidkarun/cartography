package com.softstart.VectorImage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.GeneralPath;
/**
 *OctogonVector image draws a small octogon (eight sided) figure. This may be used
 * for the markers, to make a clearer image than the circles. 
 * @author Thomas Jones-Low
 * @version $Revision: 1.2 $
 */
public class OctogonVectorImage extends VectorImage
{
    public OctogonVectorImage()
    {
        super();
        GeneralPath outline = new GeneralPath (GeneralPath.WIND_EVEN_ODD, 9);
        
        outline.moveTo (5, 0); 
        outline.lineTo (12, 0);
        outline.lineTo (17, 5);
        outline.lineTo (17, 12);
        outline.lineTo (12, 17);
        outline.lineTo (5, 17);
        outline.lineTo(0, 12);
        outline.lineTo(0, 5);
        outline.closePath();
        
        setShape (outline);
        setColor (Color.GRAY);
        setStroke (new BasicStroke (1));

    }
}
