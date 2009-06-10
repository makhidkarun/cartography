package stellar.map;
import com.softstart.VectorImage.HexVectorImage;
import stellar.data.TableRecord;
import stellar.data.TableRecordKey;
import stellar.dialog.HexLinePanel;
import stellar.dialog.ViewTableData;
import stellar.map.layout.HexLine;
import stellar.map.layout.LongLineList;

import stellar.map.layout.ShortLineList;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import javax.swing.text.StyleConstants;

public class HexLegend //implements DisplayOptions 
{
    DrawHexLayout layout;
    private HexVectorImage hexOutline = new HexVectorImage();
    private MarkerVectorImage system = new MarkerVectorImage();
    private double fontHeight;
    private int xsize; 
    private int ysize;
    
    public HexLegend(DrawHexLayout layout)
    {
        this.layout = layout;
    }

    public void setScale (int hexScale)
    {
        setScale (HexIcons.XSTART[hexScale], HexIcons.YSTART[hexScale]);
    }
    
    public void setScale (double xsize, double ysize)
    {
        this.xsize = (int)xsize;
        this.ysize = (int)ysize;
        AffineTransform at = AffineTransform.getScaleInstance(xsize/(float)HexIcons.XSTART[MapScale.SCALE_3.ordinal()], 
                    ysize/(float)HexIcons.YSTART[MapScale.SCALE_3.ordinal()]);
        hexOutline.transform(at);
        system.transform(at);
    }
    
    public Dimension getLegendSize (Graphics2D g2)
    {
        fontHeight = layout.getFontHeight (g2);
        Dimension size = new Dimension ();
        size.height = hexOutline.getIconHeight();
        size.width = hexOutline.getIconWidth();
        
        // increase the height if the top or bottom items are being used
        if (layout.getItemCount() == 5 || layout.getItemCount () == 4 || layout.getItemCount() == 3)
        {
            if (layout.getItem(0).isLongSelected() && 
                layout.getItem(0).getLongItem() != LongLineList.NONE)
            {
                size.height += (fontHeight * 4) - ysize;
            }
            if ((!layout.getItem(0).isThreeShortItems()) &&
                layout.getItem(0).getShortItem1() != ShortLineList.NONE || 
                layout.getItem(0).getShortItem2() != ShortLineList.NONE)
            {
                size.height += (fontHeight * 4) - ysize;
            }
            if (layout.getItemCount() == 5)
            {
                if (layout.getItem(4).isLongSelected() && 
                    layout.getItem(4).getLongItem() != LongLineList.NONE)
                {
                    size.height += fontHeight * 4 - ysize;
                }
            }
        }
        // increase the height if the bottom items are being used
        return size;        
    }
    /**
     * drawLegend draws a legend hex, identifying all the selected elements in 
     * a map. Because the maximum map drawing scale can have five lines, each
     * three elements, the legend can have a large number of elements, though it
     * is assumed that most maps won't use all the elements. These elements are
     * identifed by letters A through M. The elements are groups according to 
     * the line they occupy in each of the layout scales: AB, CDE, FGH, IJK, LM. 
     * Each scale uses a subset of these lines to draw the elements which make
     * the most visual sense. 
     * Level 1: CDE, Level 2: CDE, IJK, Level 3: AB, CDE, LM,
     * Level 4: AB, CDE, FGH, LM, Level 5: AB, CDE, FGH, IJK, LM
     *
     * @param start center point for the Legend hex and text to be drawn
     * @param g2 Graphics area to draw the legend into. 
     */
    public void drawLegend (Point2D start, Graphics2D g2)
    {
        fontHeight = layout.getFontHeight(g2);
        Point2D.Double center = new Point2D.Double ();
        center.x = start.getX() - xsize * 2;
        center.y = start.getY();
        g2.translate(center.x, center.y);
        hexOutline.setStroke((BasicStroke)g2.getStroke());
        hexOutline.drawImage(g2);
        g2.translate(-center.x, -center.y);
        center.x = start.getX();
        center.y = start.getY() + ysize;
        layout.drawLayout(null, center, g2);
                
        if (layout.getItemCount() == 5)
        {
            drawLegendAB(layout.getItem(0), start, g2);
            drawLegendCDE (layout.getItem(1), start, g2);
            drawLegendFGH (layout.getItem(2), start, g2);
            drawLegendIJK (layout.getItem(3), start, g2);
            drawLegendLM (layout.getItem(4), start, g2);
        }
        else if (layout.getItemCount() == 4)
        {
            drawLegendAB(layout.getItem(0), start, g2);
            drawLegendCDE (layout.getItem(1), start, g2);
            drawLegendFGH (layout.getItem(2), start, g2);
            drawLegendLM (layout.getItem(3), start, g2);
        }
        else if (layout.getItemCount() == 3)
        {
            drawLegendAB(layout.getItem(0), start, g2);
            drawLegendCDE (layout.getItem(1), start, g2);
            drawLegendLM (layout.getItem(2), start, g2);
        }
        else if (layout.getItemCount() == 2)
        {
            drawLegendCDE (layout.getItem(0), start, g2);
            drawLegendIJK (layout.getItem(1), start, g2);
        }
        else if (layout.getItemCount() == 1)
        {
            drawLegendCDE (layout.getItem(0), start, g2);
        }
    }
    
    private void drawLegendAB (HexLine drawItem, Point2D start, Graphics2D g2)
    {
        Point2D.Double center = new Point2D.Double ();
        center.y = start.getY() + ysize  - fontHeight * 4;

        if (drawItem.isLongSelected())
        {
            if(drawItem.getLongItem() != LongLineList.NONE)
            {
                center.x = start.getX();
                layout.drawString(drawItem.getLongItem().toString(), StyleConstants.ALIGN_CENTER, center, g2);
                g2.drawLine ((int)center.x, (int)(center.y+fontHeight+1), 
                            (int)center.x, (int)(center.y + (fontHeight * 3) - 1));
            }
        }
        else
        {
            if (drawItem.getShortItem1() != ShortLineList.NONE)
            {
                center.x = start.getX() - (xsize * 0.5);
                layout.drawString(drawItem.getShortItem1().toString(), StyleConstants.ALIGN_LEFT, center, g2);
                g2.drawLine ((int)(center.x - xsize), (int)(center.y+fontHeight+1), 
                            (int)(center.x - (xsize * 0.5)), (int)(start.getY() - 1));
            }
            if (drawItem.getShortItem2() != ShortLineList.NONE)
            {
                center.x = start.getX() + (xsize * 0.5);
                layout.drawString(drawItem.getShortItem2().toString(), StyleConstants.ALIGN_RIGHT, center, g2);
                g2.drawLine ((int)(center.x + xsize), (int)(center.y+fontHeight+1), 
                            (int)(center.x + (xsize * 0.5)), (int)(start.getY() - 1));
            }
        }
    }
    
    private void drawLegendCDE (HexLine drawItem, Point2D start, Graphics2D g2)
    {
        Point2D.Double center = new Point2D.Double ();
        double yHeight = 1.0; 
        
        switch (layout.getItemCount())
        {
            case 1: yHeight = 0.5; break;
            case 2: yHeight = 1.0; break;
            case 3: yHeight = 0.5; break;
            case 4: yHeight = 1.0; break;
            case 5: yHeight = 1.0; break;
        }
        
        if (drawItem.isLongSelected())
        {
            if (drawItem.getLongItem() != LongLineList.NONE)
            {
                center.x = start.getX() - xsize * 2;
                center.y = start.getY() + ysize - (fontHeight * 2);
                layout.drawString (drawItem.getLongItem().toString(), StyleConstants.ALIGN_LEFT, center, g2);
                g2.drawLine ((int)(center.x + 1), (int)(center.y + fontHeight), 
                            (int)(center.x + xsize - 2), (int) (start.getY() - 1 + ysize - (fontHeight * (yHeight - 0.5))));
            }
        }
        else
        {
            if (drawItem.getShortItem1() != ShortLineList.NONE)
            {
                center.x = start.getX() - xsize * 2;
                center.y = start.getY() + ysize - (fontHeight * 2);
                layout.drawString (drawItem.getShortItem1().toString(), StyleConstants.ALIGN_LEFT, center, g2);
                g2.drawLine ((int)(center.x + 1), (int)(center.y + fontHeight), 
                            (int)(center.x + xsize - 2), (int)(start.getY() - 1 + ysize - (fontHeight * (yHeight - 0.5))));
            }
            if (drawItem.getShortItem2() != ShortLineList.NONE)
            {
                center.x = start.getX() - xsize * 1.5;
                center.y = start.getY() + ysize - (fontHeight * 3);
                layout.drawString (drawItem.getShortItem2().toString(), StyleConstants.ALIGN_LEFT, center, g2);
                g2.drawLine ((int)(center.x + 1), (int)(center.y + fontHeight), 
                            (int)(center.x + xsize + 1), (int) (start.getY() - 1 + ysize - (fontHeight * (yHeight - 0.5))));
            }
            if (drawItem.getShortItem3() != ShortLineList.NONE)
            {
                center.x = start.getX() + xsize * 1.5;
                center.y = start.getY() + ysize - (fontHeight * 3);
                layout.drawString (drawItem.getShortItem3().toString(), StyleConstants.ALIGN_RIGHT, center, g2);
                g2.drawLine ((int) (center.x -1), (int)(center.y + fontHeight),
                            (int) (start.getX() + xsize + 1), (int)(start.getY() - 1 + ysize - (fontHeight * (yHeight - 0.5))));
            }
        }
    }
    
    private void drawLegendFGH (HexLine drawItem, Point2D start, Graphics2D g2)
    {
        Point2D.Double center = new Point2D.Double ();
        double yHeight = 1.0; 
        
        switch (layout.getItemCount())
        {
            case 1: yHeight = 0.5; break;
            case 2: yHeight = 1.0; break;
            case 3: yHeight = 0.5; break;
            case 4: yHeight = 1.0; break;
            case 5: yHeight = 1.0; break;
        }
        
        if (drawItem.isLongSelected())
        {
            if (drawItem.getLongItem() != LongLineList.NONE)
            {
                center.x = start.getX() - 2 * xsize - 1; 
                center.y = start.getY() + ysize;
                layout.drawString (drawItem.getLongItem().toString(), StyleConstants.ALIGN_LEFT, center, g2);
            }
        }
        else
        {
            /* Draw F */
            if (drawItem.getShortItem1() != ShortLineList.NONE) 
            {
                center.x = start.getX() - 2 * xsize - 1;
                center.y = start.getY() + ysize;
                layout.drawString (drawItem.getShortItem1().toString(), StyleConstants.ALIGN_LEFT, center, g2);
            }
            /* Draw G */
            if (drawItem.getShortItem2() != ShortLineList.NONE) 
            {
                center.x = start.getX() + xsize * 2;
                center.y = start.getY() + ysize - (fontHeight * 2);
                layout.drawString (drawItem.getShortItem2().toString(), StyleConstants.ALIGN_RIGHT, center, g2);
                g2.drawLine ((int)(center.x - 1), (int)(center.y + fontHeight), 
                            (int)(center.x - xsize - 1), (int)(start.getY() + ysize - (fontHeight * (yHeight - 0.5))));
            }
            /* Draw H */
            if (drawItem.getShortItem3() != ShortLineList.NONE) 
            {
                center.x = start.getX() + 2 * xsize + 1;
                center.y = start.getY() + ysize;
                layout.drawString (drawItem.getShortItem3().toString(), StyleConstants.ALIGN_RIGHT, center, g2);
            }
        }
    }
    private void drawLegendIJK (HexLine drawItem, Point2D start, Graphics2D g2)
    {
        Point2D.Double center = new Point2D.Double ();
        double yHeight = 1.0; 
        
        switch (layout.getItemCount())
        {
            case 2: yHeight = 0.75; break;
            case 5: yHeight = 0.25; break;
        }
        
        if (drawItem.isLongSelected())
        {
            if (drawItem.getLongItem() != LongLineList.NONE)
            {
                center.x = start.getX() - xsize * 2;
                center.y = start.getY() + ysize + (fontHeight);
                layout.drawString (drawItem.getLongItem().toString(), StyleConstants.ALIGN_LEFT, center, g2);
                g2.drawLine ((int)(center.x + 1), (int)(center.y + fontHeight * 0.5), 
                            (int)(center.x + xsize - 2), (int) (center.y - fontHeight * (yHeight-0.5)));
            }
        }
        else
        {
            if (drawItem.getShortItem1() != ShortLineList.NONE) 
            {
                center.x = start.getX() - xsize * 2;
                center.y = start.getY() + ysize + (fontHeight);
                layout.drawString (drawItem.getShortItem1().toString(), StyleConstants.ALIGN_LEFT, center, g2);
                g2.drawLine ((int)(center.x + 1), (int)(center.y + fontHeight * 0.5), 
                            (int)(center.x + xsize - 2), (int) (center.y - fontHeight * (yHeight-0.5)));
            }
            if (drawItem.getShortItem2() != ShortLineList.NONE)
            {
                center.x = start.getX() + xsize * 2;
                center.y = start.getY() + ysize + (fontHeight);
                layout.drawString (drawItem.getShortItem2().toString(), StyleConstants.ALIGN_RIGHT, center, g2);
                g2.drawLine ((int)(center.x), (int)(center.y + fontHeight * 0.5), 
                            (int)(center.x - xsize * 1.5), (int) (center.y - fontHeight * (yHeight-0.5)));
            }
            if (drawItem.getShortItem3() != ShortLineList.NONE)
            {
                center.x = start.getX() + xsize * 1.5;
                center.y = start.getY() + ysize + fontHeight * 2;
                layout.drawString (drawItem.getShortItem3().toString(), StyleConstants.ALIGN_RIGHT, center, g2);
                g2.drawLine ((int)(start.getX() + xsize * 2), (int)(center.y + fontHeight * 0.5),
                            (int)(center.x), (int)(center.y - fontHeight * (1 + yHeight - 0.5)));
            }
        }
    }    
    private void drawLegendLM (HexLine drawItem, Point2D start, Graphics2D g2)
    {
        Point2D.Double center = new Point2D.Double ();
        center.y = start.getY() + ysize * 2 + fontHeight;
        if (drawItem.isLongSelected())
        {
            if(drawItem.getLongItem() != LongLineList.NONE)
            {
                center.x = start.getX();
                layout.drawString(drawItem.getLongItem().toString(), StyleConstants.ALIGN_CENTER, center, g2);
                g2.drawLine ((int)center.x, (int)(center.y + 1), 
                            (int)center.x, (int)(center.y - 1));
            }
        }
        else
        {
            if (drawItem.getShortItem1() != ShortLineList.NONE)
            {
                center.x = start.getX() - (xsize * 0.5);
                layout.drawString(drawItem.getShortItem1().toString(), StyleConstants.ALIGN_LEFT, center, g2);
                g2.drawLine ((int)(center.x - xsize), (int)(center.y + 1), 
                            (int)(center.x - (xsize * 0.5)), (int)(center.y - 1));
            }
            if (drawItem.getShortItem2() != ShortLineList.NONE)
            {
                center.x = start.getX() + (xsize * 0.5);
                layout.drawString(drawItem.getShortItem2().toString(), StyleConstants.ALIGN_RIGHT, center, g2);
                g2.drawLine ((int)(center.x + xsize), (int)(center.y + 1), 
                            (int)(center.x + (xsize * 0.5)), (int)(center.y - 1));
            }
        }
    }

    public int drawWorldLegend (Point2D start, TableRecordKey marker, Graphics2D g2)
    {
        Point2D.Double center = new Point2D.Double();
        
        TableRecord t = ViewTableData.getInstance().getTableRecord (marker);
        
        if (t == null) return 0;
        
        for (int i = 0; i < t.size(); i++)
        {
            center.x = start.getX();
            center.y = start.getY() + (system.getBounds2D().getWidth() * 1.5 * i);
            system.drawMarker(t.getRecord(i).getColor(), center, g2);
            center.x += (system.getBounds2D().getWidth() * 1.5);
            layout.drawString (t.getRecord(i).getValue(), StyleConstants.ALIGN_RIGHT, center, g2);
        }
        return t.size();
    }

    public int drawBaseLegend (Point2D start, Graphics2D g2)
    {
        TableRecord t = ViewTableData.getInstance().getTableRecord (TableRecordKey.BASES);
        if (t == null) return 0;
        drawLegendText (t, start, g2);
        return t.size();
    }
    
    public int drawAllegianceLegend (Point2D start, Graphics2D g2)
    {
        TableRecord t = ViewTableData.getInstance().getTableRecord(TableRecordKey.POLITIES);
        if (t == null) return 0;
        drawLegendText (t, start, g2);
        return t.size();
    }
    
    public int drawStarportLegend (Point2D start, Graphics2D g2)
    {
        TableRecord t = ViewTableData.getInstance().getTableRecord(TableRecordKey.PORTS);
        if (t == null) return 0;
        drawLegendText (t, start, g2);
        return t.size();
    }
    
    public int drawTravelZoneLegend (Point2D start, Graphics2D g2)
    {
        Point2D.Double center = new Point2D.Double();
        TableRecord t = ViewTableData.getInstance().getTableRecord(TableRecordKey.TRAVEL_ZONE);
        if (t == null) return 0;
        for (int i = 0; i < t.size(); i++)
        {
            center.x = start.getX();
            center.y = start.getY() + (system.getBounds2D().getWidth() * 1.5 * i);
            system.drawMarker(t.getRecord(i).getColor(), center, g2);
            center.x += (system.getBounds2D().getWidth()) + fontHeight;
            layout.drawString (t.getRecord(i).getCode(), StyleConstants.ALIGN_LEFT, center, g2);
            center.x += fontHeight;
            layout.drawString (t.getRecord(i).getValue(), StyleConstants.ALIGN_RIGHT, center, g2);
        }
        return t.size();
    }
    
    public double getMarkerHeight() {return system.getBounds2D().getHeight() * 1.5; }

    private void drawLegendText (TableRecord t, Point2D start, Graphics2D g2)
    {
        Point2D.Double center = new Point2D.Double ();

        for (int i = 0; i < t.size(); i++)
        {
            center.x = start.getX();
            center.y = start.getY() + (fontHeight * i) ;
            layout.drawString (t.getRecord(i).getCode(), StyleConstants.ALIGN_LEFT, center, g2);
            center.x += fontHeight;
            layout.drawString(t.getRecord(i).getValue(), StyleConstants.ALIGN_RIGHT, center, g2);
        }
    }
}