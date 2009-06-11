package com.softstart.VectorImage;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.ColorModel;
import javax.swing.Icon;

/**
 * An encapsulation of the Swing/AWT 2D drawing system. This class represents 
 * a single image (like a icon or picture), but consisting of vector image 
 * elements (like a line drawing). The idea is this "icon" will be drawn 
 * repeatly on a 2D drawing suface, at different sizes and with different colors. 
 * 
 * This class is not abstract because the image is (or should be )created in 
 * the constructor, rather than created during the drawing process. 
 * 
 * @version $Id$
 * @author Thomas Jones-Low
 */
public class VectorImage implements Paint, Stroke, Shape, Icon
{
    BasicStroke stroke;
    Color color;
    Shape shape;
    Shape transformedShape;
    boolean fillShape;

    public VectorImage ()
    {
        fillShape = false;
    }
    
    public VectorImage(Shape newShape, Color newColor, BasicStroke newStroke)
    {
        shape = newShape;
        transformedShape = newShape;
        color = newColor;
        stroke = newStroke; 
        fillShape = false;
    }

    public void drawImage(Graphics g) { drawImage((Graphics2D)g); } 
    public void drawImage(Graphics2D g2)
    {
        Paint oldPaint;
        Stroke oldStroke;

        oldPaint = g2.getPaint();
        oldStroke = g2.getStroke();
        g2.setPaint(color);
        g2.setStroke(stroke);
        g2.draw(transformedShape);
        if (fillShape) g2.fill(transformedShape);
        g2.setPaint(oldPaint);
        g2.setStroke(oldStroke);
    }

    /**
     * From the Paint Interface: 
     * Creates and returns a PaintContext used to generate the color pattern. 
     * Since the ColorModel argument to createContext is only a hint, 
     * implementations of Paint should accept a null argument for ColorModel. 
     * Note that if the application does not prefer a specific ColorModel, the 
     * null ColorModel argument will give the Paint implementation full leeway 
     * in using the most efficient ColorModel it prefers for its raster 
     * processing.
     * 
     * Since the API documentation was not specific about this in releases 
     * before 1.4, there may be implementations of Paint that do not accept a 
     * null ColorModel argument. If a developer is writing code which passes a 
     * null ColorModel argument to the createContext method of Paint objects 
     * from arbitrary sources it would be wise to code defensively by 
     * manufacturing a non-null ColorModel for those objects which throw a 
     * NullPointerException.
     */
    public PaintContext createContext(ColorModel cm,
                                  Rectangle deviceBounds,
                                  Rectangle2D userBounds,
                                  AffineTransform xform,
                                  RenderingHints hints)
    {
        return color.createContext(cm, deviceBounds, userBounds, xform, hints);
    }
    /**
     * From the Transparency interface
     * Returns the type of this Transparency.
     * @return the field type of this Transparency, which is either 
     * OPAQUE, BITMASK or TRANSLUCENT.
     */
    public int getTransparency() { return color.getTransparency(); } 

    /**
     * Returns an outline Shape which encloses the area that should be painted 
     * when the Shape is stroked according to the rules defined by the object 
     * implementing the Stroke interface.
     * @param p - a shape to be stroked
     * @return the stroked outline Shape.
     */
    public Shape createStrokedShape(Shape p) { return stroke.createStrokedShape(p); }

    /**
     * Returns an integer Rectangle that completely encloses the Shape. Note 
     * that there is no guarantee that the returned Rectangle is the smallest 
     * bounding box that encloses the Shape, only that the Shape lies entirely 
     * within the indicated Rectangle. The returned Rectangle might also fail 
     * to completely enclose the Shape if the Shape overflows the limited range 
     * of the integer data type. The getBounds2D method generally returns a 
     * tighter bounding box due to its greater flexibility in representation.
     * @return an integer Rectangle that completely encloses the Shape.
     */
    public Rectangle getBounds() { return transformedShape.getBounds(); } 

    /**
     * Returns a high precision and more accurate bounding box of the Shape 
     * than the getBounds method. Note that there is no guarantee that the 
     * returned Rectangle2D is the smallest bounding box that encloses the 
     * Shape, only that the Shape lies entirely within the indicated 
     * Rectangle2D. The bounding box returned by this method is usually tighter 
     * than that returned by the getBounds method and never fails due to 
     * overflow problems since the return value can be an instance of the 
     * Rectangle2D that uses double precision values to store the dimensions.
     * @return an instance of Rectangle2D that is a high-precision bounding 
     * box of the Shape.
     */
    public Rectangle2D getBounds2D() { return transformedShape.getBounds2D(); } 

    /**
     * Tests if the specified coordinates are inside the boundary of the Shape.
     */
    public boolean contains(double x, double y) { return transformedShape.contains(x,y); }

    /**
     * Tests if a specified Point2D is inside the boundary of the Shape.
     */
    public boolean contains(Point2D p) { return transformedShape.contains(p); } 

    /**
     * Tests if the interior of the Shape intersects the interior of a specified 
     * rectangular area. The rectangular area is considered to intersect the 
     * Shape if any point is contained in both the interior of the Shape and the 
     * specified rectangular area.
     * 
     * This method might conservatively return true when:
     *  * there is a high probability that the rectangular area and the 
     *    Shape intersect, but
     *  * the calculations to accurately determine this intersection are 
     *     prohibitively expensive.
     * 
     * This means that this method might return true even though the rectangular 
     * area does not intersect the Shape. The Area class can be used to perform 
     * more accurate computations of geometric intersection for any Shape object 
     * if a more precise answer is required.
     */
    public boolean contains(Rectangle2D r) { return transformedShape.contains(r); } 
    public boolean contains(double x,double y, double w, double h)
    {
        return transformedShape.contains(x,y,w,h); 
    }
    public boolean intersects(double x, double y, double w, double h)
    {
        return transformedShape.intersects(x, y, w, h);
    }
    public boolean intersects(Rectangle2D r) { return transformedShape.intersects(r); } 
    public PathIterator getPathIterator(AffineTransform at) { return transformedShape.getPathIterator(at); } 
    public PathIterator getPathIterator(AffineTransform at, double flatness)
    {
        return transformedShape.getPathIterator(at, flatness);
    }


    public void transform (AffineTransform at)
    {
        transformedShape = at.createTransformedShape(shape);
    }

    public void undoTransform () { transformedShape = shape; } 

    public void setColor (Color newColor) { this.color = newColor; } 
    public void setStroke (BasicStroke  newStroke) { this.stroke = newStroke;}
    public void setFillShape(boolean fill) { fillShape = fill; } 

    public void setShape (Shape newShape)
    {
        this.shape = newShape;
        this.transformedShape = newShape;
    }
    /**
     * Paints the icon.
     * The top-left corner of the icon is drawn at 
     * the point (<code>x</code>, <code>y</code>)
     * in the coordinate space of the graphics context <code>g</code>.
     * If this icon has no image observer,
     * this method uses the <code>c</code> component
     * as the observer.
     *
     * @param c the component to be used as the observer
     *          if this icon has no image observer
     * @param g the graphics context 
     * @param x the X coordinate of the icon's top-left corner
     * @param y the Y coordinate of the icon's top-left corner
     */
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) 
    {
        g.translate(x, y);
        drawImage (g);
        g.translate (-x, -y);
    }
    
    /**
     * Gets the width of the icon.
     *
     * @return the width in pixels of this icon
     */
    public int getIconWidth() { return transformedShape.getBounds().width; }

    /**
     * Gets the height of the icon.
     *
     * @return the height in pixels of this icon
     */
    public int getIconHeight() { return transformedShape.getBounds().height; }
    
}
