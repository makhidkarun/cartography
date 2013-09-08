package com.softstart.VectorImage;

import java.awt.geom.GeneralPath;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * LineFillVectorImage is designed to produce a series of lines, to fill another
 * vector image shape. You can use vertical, horizontal lines, angled lines, or
 * crossed lines. 
 * 
 * @version $Id$
 * @author Thomas Jones-Low
 */

public class LineFillVectorImage extends VectorImage 
{
    private int spacing; 
    private Dimension size;
    private int direction;

    public static final int LINES_HORIZONTAL = 0;
    public static final int LINES_VERTICAL = 1;
    public static final int LINES_45UP = 4;
    public static final int LINES_45DOWN= 5;
    public static final int LINES_90CROSS = 10;
    public static final int LINES_45CROSS = 11;
    
    
    public LineFillVectorImage()
    {
        size = new Dimension (10, 10);
        direction = LINES_HORIZONTAL;
        spacing = 3;
        setColor(Color.gray);
        setStroke(new BasicStroke(1));
        regenerateShape();
    }

    public LineFillVectorImage (int direction, Dimension size, int spacing)
    {
        setColor(Color.gray);
        setStroke(new BasicStroke(1));
        this.size = size;
        this.direction = direction;
        this.spacing = spacing;
        regenerateShape();
    }

    public LineFillVectorImage (int direction, Rectangle bounds, int spacing)
    {
        setColor (Color.gray);
        setStroke(new BasicStroke(1));
        this.direction = direction;
        this.spacing = spacing; 
        this.size = new Dimension (bounds.width - bounds.x, bounds.height - bounds.y);
        regenerateShape();
    }

    private void regenerateShape ()
    {
        Point xy = new Point();
        int index; 
        int x;
        int x2, y2; 
        
        GeneralPath lines = new GeneralPath();
        switch (direction)
        {
            case LINES_90CROSS:
            case LINES_HORIZONTAL:
                for (index = spacing; index < size.height; index += spacing)
                {
                    lines.moveTo(0, index);
                    lines.lineTo(size.width, index);
                }
                if (direction == LINES_HORIZONTAL) break;
            case LINES_VERTICAL:
                for (index = spacing; index < size.width; index += spacing)
                {
                    lines.moveTo (index, 0);
                    lines.lineTo (index, size.height);
                }
                break;
            case LINES_45CROSS:
            case LINES_45UP:
                for (x = spacing; x < size.width + size.height; x += spacing)
                {
                    x2 = (x > size.height) ? x - size.height : 0; 
                    y2 = (x > size.height) ? size.height : x;
                    xy.x = (x > size.width) ? size.width : x; 
                    xy.y = (x > size.width) ? x - size.width : 0; 
                    lines.moveTo (xy.x,xy.y);
                    lines.lineTo (x2, y2);
                }
                if (direction == LINES_45UP) break;
            case LINES_45DOWN:
                for (x = spacing; x < size.width + size.height; x += spacing)
                {
                    xy.x = (x > size.height) ? x - size.height : 0;
                    xy.y = (size.height - x > 0) ? size.height - x : 0 ;
                    x2 = (x < size.width) ? x : size.width;
                    y2 = (x < size.width) ? size.height : size.width + size.height - x;
                    lines.moveTo (xy.x,xy.y);
                    lines.lineTo (x2, y2);
                    
                }
                break;
        }
        setShape(lines);
    }
    public void setDirection(int newDirection)
    {
        direction = newDirection;
        regenerateShape();
    }

    public void setSize(Dimension newSize)
    {
        size = newSize;
        regenerateShape();
    }

    public void setSize (int width, int height)
    {
        size = new Dimension (width, height);
        regenerateShape();
    }
    public void setSpacing(int newSpacing)
    {
        spacing = newSpacing;
        regenerateShape();
    }

    public void setFillShape (boolean fill) { fillShape = false; } 

}
