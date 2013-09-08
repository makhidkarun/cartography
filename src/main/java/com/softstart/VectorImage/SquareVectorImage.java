package com.softstart.VectorImage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.GeneralPath;

/**
 * Creates a vector image of a square (an equilateral rectangle).
 * 
 * @version $Id SquareVectorImage.java,v 1.3 2006/05/02 19:49:22 tjoneslo Exp $
 * @author Thomas Jones-Low
 */
public class SquareVectorImage extends VectorImage 
{
    /**
     * Creates a square of a standard size. Use an affinity transform 
     * to change the size of the shape. 
     */
    public SquareVectorImage()
    {
        super();
        GeneralPath outline = new GeneralPath (GeneralPath.WIND_EVEN_ODD, 5);
        
        outline.moveTo (0,0);
        outline.lineTo (0,10);
        outline.lineTo(10,10);
        outline.lineTo(10,0);
        outline.closePath();
        
        setShape (outline);
        setColor (Color.GRAY);
        setStroke (new BasicStroke (1));
        
        
    }
}