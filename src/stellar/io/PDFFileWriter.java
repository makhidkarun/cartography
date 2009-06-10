package stellar.io;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

import com.softstart.VectorImage.HexVectorImage;
import stellar.dialog.ViewTableData;
import stellar.map.HexIcon;
import stellar.map.DrawHexLayout;
import stellar.map.HexLegend;
import stellar.data.GroupRecord;
import stellar.data.GroupType;
import stellar.data.HexID;
import stellar.data.TableRecordKey;
import stellar.map.MapScale;
import stellar.map.MapScope;

import stellar.map.layout.HexLayout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.text.StyleConstants;

public class PDFFileWriter extends HexIcon
{
    private String filename;
    private File   file;
    private Graphics2D g2;
    
    private BasicStroke hexLine = new BasicStroke (0.25f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);
    private BasicStroke thinLine = new BasicStroke (0.5f);
    private BasicStroke medLine = new BasicStroke (1.0f);
    private BasicStroke thickLine = new BasicStroke (2.0f);
    private Font hexDraw = new Font ("Arial", Font.PLAIN, 4);
    private Font legendFont = new Font ("Arial", Font.BOLD, 5);
    
    private DrawHexLayout layout;
    
    public PDFFileWriter (String filename)
    {
        super(true);
        this.filename = filename;
        this.file = new File (filename);
    }
    
    public PDFFileWriter (File file)
    {
        super(true);
        this.file = file;
        this.filename = file.getAbsolutePath();
    }
    
    public void write () throws java.io.IOException, DocumentException
    {
        //PdfReader read = null;
        Document document = new Document (PageSize.LETTER, 36, 36, 36, 36);
        PdfWriter writer = PdfWriter.getInstance (document, new FileOutputStream (file));

        if (this.getGroupData() == null)
        {
            throw new DocumentException ("No group data set");
        }
        if (getScope() != MapScope.SECTOR)
        {
            throw new DocumentException ("Scope not set to Sector, please reset the scope to either Sector to output to PDF.");
        }

        /* Get the blank map as a base write */
/*
        if (getScope() == MapIcon.SCOPE_SECTOR)
        {
            URL url = Resources.getSectorPDFTemplate();
            read = new PdfReader (url);
           
        }
        else if (getScope() == MapIcon.SCOPE_QUADRANT)
        {
            read = new PdfReader (Resources.getQuadrantPDFTemplate());
        }
        
        if (read == null) throw new DocumentException ("Unable to read PDF Template file.");
*/
        /* Add metadata header information */
        document.addTitle(this.getGroupData().getName());
        document.addAuthor("Traveller Stellar Cartographer");
        document.open();
        
        
        PdfContentByte cb = writer.getDirectContent();
        //PdfImportedPage page1 = writer.getImportedPage(read, 1);
        //cb.addTemplate(page1, 0, -30);

        // Create the graphic state         
        cb.saveState();
        cb.concatCTM(1, 0, 0, 1, 0, 0);
        
        g2 = cb.createGraphics(PageSize.LETTER.getWidth(), PageSize.LETTER.getHeight());
        // draw things to g2

        // This also puts the map to a full sector offset. 
        setMapOffset (1,1);

        if (getScope() == MapScope.SECTOR)
        {
            writeSector();
        }
        else if (getScope() == MapScope.QUADRANT)
        {
            setXStart (8.0);
            setYStart (4.8);
            setXSize (10.0);
            setYSize (18.0);
            g2.setFont (new Font ("Arial", Font.PLAIN, 6));
            drawAll();
        }

        g2.setFont(new Font ("Arial", Font.PLAIN, 14));
        Point center = new Point ((int)(PageSize.LETTER.getWidth() / 2.0), 10);
        layout.drawString (this.getGroupData().getName(), StyleConstants.ALIGN_CENTER, center, g2);
        
        g2.dispose();
        cb.restoreState();         
/*
        HeaderFooter header = new HeaderFooter (new Phrase (this.getGroupData().getName()), false);
        header.setBorder(Rectangle.NO_BORDER);
        document.setHeader(header);
*/        
        // Close the document
        document.close();
    }

    
    private void writeSector()
    {
        int mapRows = 40;
        int mapCols = 32;
        int width, height;
        layout = new DrawHexLayout (getLayout());
        // Set an offset from the upper left corner where to start the drawings
        // the display map assumes an offset of 0.0, 0.0, because there is no
        // decoration. 
        setXStart (15.10);
        //setYStart (3.70);
        setYStart (33.5);
        // Reset the xsize/yize here. The setMapScale() sets them to default 
        // sizes based upon the displayed map. This sets them back to the 
        // custom size for the pdf sector map. 
        setXSize (5.25);
        setYSize (8.75);
        width = (int)(((mapCols * 3) + 1) * getXSize());
        height = (int)(((mapRows * 2)+ 1) * getYSize() + 2);
        layout.setScale(5.25, 8.75);

        if (getScale() == MapScale.SCALE_5)
        {
            hexDraw = new Font ("Arial", Font.PLAIN, 3);
            g2.setFont(hexDraw);
            layout.setFontHeight(3.5);
        }
        else if (getScale() == MapScale.SCALE_4)
        {
            g2.setFont(hexDraw);
            layout.setFontHeight(3.7);
        }
        else 
        {
            g2.setFont(hexDraw);
            layout.setFontHeight(-1.0);
        }            

        // set the Layout to draw the legend.
        drawSectorLegend ();

        g2.setColor(layout.getBackgroundColor());
        g2.fillRect (14,32,width+4,height+2);

        // Format the layout system to compress the hex image even more
        //getLayout().setScale(5.25, 8.75);

        
        //Draw the borders
        g2.setStroke(thickLine);
        g2.setColor(layout.getBackgroundColor());
        g2.drawRect(14,32,width+4,height+2);
        /* draw Subsector lines */
        g2.setStroke(medLine);
        height = (int)(21 * getYSize() + 2);
        g2.drawLine(14, height + 32, width+16, height+32);
        height = (int)(41 * getYSize() + 2);
        g2.drawLine(14, height + 32, width+16, height+32);
        height = (int)(61 * getYSize() + 2);
        g2.drawLine(14, height + 32, width+16, height+32);
        height = (int)(81 * getYSize() + 2);
        width = (int)(25 * getXSize());
        g2.drawLine(width+12, 32, width+12, height+32);
        width = (int)(49 * getXSize());
        g2.drawLine(width+12, 32, width+12, height+32);       
        width = (int)(73 * getXSize());
        g2.drawLine(width+12, 32, width+12, height+32);        

        // Draw all the interior stuff
        drawAll();

    }
    
    public void drawAll ()
    {
        int mapRows=10, mapCols=8;
        int x, y;
        
        HexID drawHex = new HexID();     
        HexVectorImage hexOutline = new HexVectorImage();
        hexOutline.transform(AffineTransform.getScaleInstance(this.getXSize()/12.0, this.getYSize()/20.0));        
        hexOutline.setStroke(hexLine);
        
        if (getScope() == MapScope.SECTOR) { mapRows = 40; mapCols = 32; }
        if (getScope() == MapScope.QUADRANT) { mapRows=20; mapCols = 16; }
        /* draw the hexagons on the page */
        for (y = 0; y < mapRows; y++)
        {
            for (x = 0; x < mapCols; x++)
            {
                drawHex.x = x+1;
                drawHex.y = y+1;
                if (!isDrawn (drawHex)) continue;
                center(x,y);
                drawHex(g2, hexOutline);
            }
        }

        if (getMapData() != null && layout.getDrawBorders())
        {
            setBorderStroke(medLine);
            drawBorders(g2);
        }

        fillHexIDs(g2);

        if (getMapData() != null && layout.getJumpRoutes())
        {
            fillLinks (g2);
        }

        drawSystemLayout (g2);
    }
    
    private void drawSectorLegend ()
    {
        Point2D.Double start = new Point2D.Double (530.5, 44.0);
        Point2D.Double center = new Point2D.Double ();
        //Point2D.Double textStart = new Point2D.Double();
        //Rectangle2D bound;
        HexID l;
        String name;
        int x, y, height, width;
        int rows;
        Color defaultBackground;

        // Set up the draw Environment        
        defaultBackground = layout.getBackgroundColor();
        layout.setBackgroundColor(Color.WHITE);
        g2.setFont(new Font ("Arial", Font.BOLD, 5));
        g2.setColor(Color.BLACK);
        // Draw the subsectors
        g2.setStroke(thinLine);
        x = (int)start.x + 3; y = (int)start.y; width = 40; height = 64;
        g2.drawRect(x, y, width, height);
        g2.drawLine (x+width/4, y, x+width/4, y+height);
        g2.drawLine(x+width/2, y , x + width/2, y + height);
        g2.drawLine(x+3*width/4, y, x+ 3*width/4, y+height);
        g2.drawLine(x,y+height/4, x+width, y+height/4);
        g2.drawLine(x, y+height/2, x+width, y+height/2);
        g2.drawLine(x, y+3*height/4, x+width, y+3*height/4);

        for (int i=0; i < 16; i++)
        {
            name = Integer.toString(10 + i, 36).toUpperCase();
            center.x = (start.x + (i % 4) * 10)+4;
            center.y = (start.y + (i / 4) * 16);
            layout.drawString(name, StyleConstants.ALIGN_RIGHT, center, g2);
        }
        // Draw the text over the box
        center.x = start.x + 7;
        center.y = start.y + 66;
        layout.drawString("SUBSECTORS", StyleConstants.ALIGN_RIGHT, center, g2);
        
        center.x = start.x + 2;
        center.y = start.y + 71;
        layout.drawString("WITHIN A SECTOR", StyleConstants.ALIGN_RIGHT, center, g2);

        // draw the box around the subsector names
        width = 65;
        g2.setStroke(medLine);
        g2.setColor(Color.BLACK);
        g2.drawRect((int)(start.x), (int)(start.y + 78), width, 80);

        // write the letter codes
        g2.setFont (new Font ("Arial", Font.BOLD, 4));
        center.x = start.x + 5;
        for (int i = 0; i < 16; i++)
        {
            name = Integer.toString(10 + i, 36).toUpperCase();
            center.y = start.y + 79 + 4.7 * i;
            layout.drawString(name, StyleConstants.ALIGN_LEFT, center, g2);
        }

        // Write the names 
        g2.setFont(new Font ("Arial", Font.PLAIN, 4));
        center.x = start.x + 7;
        for (Iterator i = getMapData().getGroups(); i.hasNext();)
        {
            GroupRecord g = (GroupRecord)i.next();
            if (g.getType() == GroupType.SUBSECTOR)
            {
                l = g.getLocation();

                center.y = start.y + 79 + 4.70 * ((l.y-1) * 4 + l.x - 1);
                layout.drawString(g.getName(), StyleConstants.ALIGN_RIGHT, center, g2);
            }
        }
        
        // Draw Legend Title
        center.x = start.x + width/2;
        center.y = start.y + 163;
        drawLegendText ("MAP LEGEND", center);
     
        // Draw Legend Hex 1
        g2.setFont (hexDraw);
        center.y = start.y + 170 + 15;
        center.x = start.x + width/2;
        g2.setStroke(hexLine);
        HexLegend legend = new HexLegend (layout);
        legend.setScale(5.25, 8.75);
        legend.drawLegend(center, g2);
        //getLayout().drawLegend (center, g2);
        rows = 0;
        center.y = start.y + 170 + 15 + 25;
        
        if (layout.isSystemStarportMarkerUsed())
        {
            center.x = start.x + width/2;
            drawLegendText ("STARPORT MARKER", center);
            center.y += 8;
            center.x = start.x + 8;
            rows = legend.drawWorldLegend(center, TableRecordKey.PORTS, g2);
            center.y += rows *  legend.getMarkerHeight();
        }
        
        if (layout.isSystemSizeMarkerUsed())
        {
            center.x = start.x + width / 2;
            drawLegendText ("SIZE MARKER", center);
            center.y += 8;
            center.x = start.x + 8;
            rows = legend.drawWorldLegend(center, TableRecordKey.SIZE, g2);
            center.y += rows * legend.getMarkerHeight();
        }
        
        if (layout.isSystemAtmosphereMarkerUsed())
        {
            center.x = start.x + width/2;
            drawLegendText ("ATMOSPHERE MARKER", center);
            center.y += 8;
            center.x = start.x + 8;
            rows = legend.drawWorldLegend(center, TableRecordKey.ATMOSPHERE, g2);
            center.y += rows * legend.getMarkerHeight();
        }
        
        if (layout.isSystemHydrographicMarkerUsed())
        {
            center.x = start.x + width/2;
            drawLegendText ("HYDROGRAPHIC MARKER", center);
            center.y += 8;
            center.x = start.x + 8;
            rows = legend.drawWorldLegend(center, TableRecordKey.HYDROGRAPHICS, g2);
            center.y += rows * legend.getMarkerHeight();
        }
        
        if (layout.isSystemPopulationMarkerUsed())
        {
            center.x = start.x + width/2;
            drawLegendText ("POPULATION MARKER", center);
            center.y += 8;
            center.x = start.x + 8;
            rows = legend.drawWorldLegend(center, TableRecordKey.POPULATION, g2);
            center.y += rows * legend.getMarkerHeight();
        }
        
        if (layout.isSystemGovernmentMarkerUsed())
        {
            center.x = start.x + width/2;
            drawLegendText ("GOVERNMENT MARKER", center);
            center.y += 8;
            center.x = start.x + 8;
            rows = legend.drawWorldLegend(center, TableRecordKey.GOVERNMENT, g2);
            center.y += rows * legend.getMarkerHeight();
        }
        
        if (layout.isSystemLawLevelMarkerUsed())
        {
            center.x = start.x + width/2;
            drawLegendText ("LAW LEVEL MARKER", center);
            center.y += 8;
            center.x = start.x + 8;
            rows = legend.drawWorldLegend(center, TableRecordKey.LAW_LEVEL, g2);
            center.y += rows * legend.getMarkerHeight();
        }
        
        if (layout.isSystemPrimaryMarkerUsed())
        {
            center.x = start.x + width/2;
            drawLegendText ("PRIMARY MARKER", center);
            center.y += 8;
            center.x = start.x + 8;
            rows = legend.drawWorldLegend(center, TableRecordKey.STAR_SPECTRUM, g2);
            center.y += rows * legend.getMarkerHeight();
        }
        
        // Draw Bases (as needed?)
        if (layout.isBasesUsed())
        {
            center.x = start.x + width/2;
            drawLegendText("BASES", center);
            center.y += 7;
            center.x = start.x + 8;
            rows = legend.drawBaseLegend(center, g2);
            center.y += rows * layout.getFontHeight(g2);
        } 
        
        if (layout.isAllegiancesUsed())
        {
            center.x = start.x + width/2;
            drawLegendText ("ALLEGIANCES", center);
            center.y += 7;
            center.x = start.x + 8;
            rows = legend.drawAllegianceLegend(center, g2);
            center.y += rows * layout.getFontHeight (g2);
        }
        
        if (layout.isStarportUsed())
        {
            center.x = start.x + width/2;
            drawLegendText ("STARPORTS", center);
            center.y += 7;
            center.x = start.x + 8;
            rows = legend.drawStarportLegend(center, g2);
            center.y += rows * layout.getFontHeight(g2);
        }
        if (layout.isTravelZoneUsed())
        {
            center.x = start.x + width/2;
            drawLegendText ("TRAVEL ZONE", center);
            center.y += 8;
            center.x = start.x + 8;
            rows = legend.drawTravelZoneLegend(center, g2);
            center.y += rows * legend.getMarkerHeight();
        }
        
        // Draw legend box
        height = (int)(27 * getYSize() + 2);
        height = (int)(center.y - (start.y + 155));
        g2.setStroke(medLine);
        g2.setColor (Color.BLACK);
        g2.drawRect((int)start.x, (int)start.y + 162, width, height);
        
        // Restore the color for drawing the rest of the map
        layout.setBackgroundColor(defaultBackground);        
    }
    
    private void drawLegendText (String text, Point2D center)
    {
        Rectangle2D bound;
        g2.setFont (legendFont);
        bound = layout.getStringBounds(text, g2);
        layout.drawString(text, StyleConstants.ALIGN_CENTER, center, g2);
        g2.setStroke(thinLine);
        g2.drawLine((int)(center.getX()- bound.getWidth()/2), (int)center.getY()+7, 
                    (int)(center.getX() + bound.getWidth()/2), (int)center.getY()+7);
        g2.setFont (hexDraw);
    }

    public HexID getLocation (int x, int y) { return null; }     
    public boolean isDrawn (HexID l) { return true; }
    //public HexLayout getLayout () { if (layout == null) return super.getLayout(); else return layout; }
    
    public static void main(String[] args)
    {
        PDFFileWriter writer;
        AccessXMLFile data;
        try
        {
            if (args.length >= 1)
                writer = new PDFFileWriter (args[0]);
            else
                writer = new PDFFileWriter ("SectorMap2.pdf");
        
            if (args.length >= 2)
                data = new AccessXMLFile (args[1]);
            else
                data = new AccessXMLFile("MARANAN.xml");
                
            try {data.read(); data.getAstrogation().loadGlobalReferences(null);} 
            catch (Exception ex) {ex.printStackTrace(); return;}
            writer.setMapData(data.getAstrogation());
            
            if (args.length >= 3)
                writer.setScale (MapScale.get(Integer.parseInt(args[2])));
            else            
                writer.setScale(MapScale.SCALE_5);
                
            writer.setScope(MapScope.SECTOR);
            ViewTableData.getInstance().setData(data.getAstrogation());
            
            writer.write();
        }
        catch (DocumentException ex) {ex.printStackTrace();} 
        catch (IOException ex) {ex.printStackTrace();}
        
        System.exit(0);
    }
}
