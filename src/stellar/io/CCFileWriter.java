package stellar.io;
import stellar.map.DrawHexLayout;
import stellar.data.Astrogation;
import stellar.data.GroupRecord;
import stellar.data.GroupType;
import stellar.data.HexID;
import stellar.data.Links;
import stellar.data.StarSystem;
import stellar.data.TableRecord;
import stellar.data.TableRecordKey;
import stellar.dialog.EditOptions;
import stellar.dialog.HexLinePanel;
import stellar.map.MapScale;

import stellar.map.layout.HexLayout;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

public class CCFileWriter 
{
    private String filename;
    private File   file;
    private Astrogation data;
    private BufferedWriter w;
    
    private String lastLayer;
    private String lastFont;
    private String lastLineStyle;
    private String lastFillStyle;
    
    private int lastColor;
    private int lastJustify;
    private int lastStyle;
    
    private double lastHeight;
    private double lastAngle;
    private double lastWidth;
    
    private static final double SECTOR_NAME_X = 495.0;
    private static final double SECTOR_NAME_Y = 0.0;
    private static final int SECTOR_NAME_COLOR = 15;
    private static final String SECTOR_NAME_FONT = "Arial";
    private static final double SECTOR_NAME_HEIGHT = 23.0;
    private static final double SECTOR_NAME_ANGLE = 90.0;
    private static final int SECTOR_NAME_JUSTIFY = 6;
    private static final int SECTOR_NAME_STYLE = 16;

    private static final int BASE_KEY_COLOR = 15;
    private static final double BASE_KEY_Y = 404.5;
    private static final double BASE_KEY_VERTICAL_SEP = 4.1;
    private static final int BASE_KEY_TEXT_X = 503;
    private static final String BASE_KEY_TEXT_FONT = "Arial";
    private static final double BASE_KEY_TEXT_HEIGHT = 2.5;
    private static final double BASE_KEY_TEXT_ANGLE = 0.0;
    private static final int BASE_KEY_TEXT_JUSTIFY = 3;
    private static final int BASE_KEY_TEXT_STYLE = 16;
    private static final int BASE_KEY_SYMBOL_X = 499;
    private static final double BASE_KEY_SYMBOL_SCALE_X = 1.0;
    private static final double BASE_KEY_SYMBOL_SCALE_Y = 1.0;
    private static final double BASE_KEY_SYMBOL_ROTATE = 0.0;    
    private static final int colourMap [] = {
    	0,	/* Black */ 0xFF000000,
        7,	/* Blue */  0xFF0000FF,
    	13,	/* Green */ 0xFF00FF00,
        9,	/* Cyan */  0xFF00FFFF,
    	11,	/* Red */   0xFFFF0000,
        25,	/* Magenta */ 0xFFFF00FF,
    	10,	/* Brown */ 0xFFFFC800,
        16,	/* White */ 0xFFE6E6E6,
    	14,	/* Dark Grey */ 0xFF404040,
        3,	/* Light Blue */ 0xFFAFAFFF,
    	1,	/* Light Green */0xFFAFFFAF,
        5,	/* Light Cyan */0xFFAFFFFF,
    	2,	/* Light Red */0xFFFFAFAF,
        6,	/* Light Magenta */0xFFFFAFFF,
    	4,	/* Light Yellow */0xFFFFFF00,
       15,	/* Bright White */0xFFFFFFFF
};

    public CCFileWriter (String filename)
    {
        this.filename = filename;
        this.file = new File (filename);
    }
    
    public CCFileWriter (File file)
    {
        this.file = file;
        this.filename = file.getAbsolutePath();
    }
    
    public void setAstrogation (Astrogation data) { this.data = data; } 
    /**
     * This is the main function to write the set astrogation data to an
     * scr (Campaign Cartrogpher script file), which is then run through the 
     * CC script engine to produce a final map. 
     * Code Path
     *    ConvertSector(name);
     *       BeginOutput();
     *       OutputHeader():
     *       ConvertSubsector();
     *          OutputKeyEntry();
     *          OutputTradeRoute();
     *          OutputSystem();
     *              OutputZone();
     *              OutputHexNumber();
     *              OutputName();
     *              OutputWorldMarker();
     *              OutputPort();
     *              OutputAllegiance();
     *              OutputGasGiant();
     *              OutputBases();
     *       CompleteSystems();
     *       OutputSectorDetails();
     *       EndOutput();
     * @throws java.io.IOException
     */
    public void write () throws java.io.IOException
    {
        w = new BufferedWriter(new FileWriter (file));
        
        /* BeginOuptut() */
        w.write("ECOFF");w.newLine();
        
        /* OutputHeader() */
        beginLayer ("STANDARD");
        
        /* Output Key Entry */
        outputKeyEntries();
        /* outputTradeRoutes () */
        outputTradeRoutes ();
        /* outputSystems  */
        outputSystems();
        /* CompleteSystems () */
        completeSystems ();     
        /* Output Sector Details () */
        beginLayer ("MAP BORDER");
        
        outputText(data.getGroup(1).getName(),
            SECTOR_NAME_X, SECTOR_NAME_Y, 
				   SECTOR_NAME_COLOR, SECTOR_NAME_FONT, SECTOR_NAME_HEIGHT, 
				   SECTOR_NAME_ANGLE, SECTOR_NAME_JUSTIFY, SECTOR_NAME_STYLE);
        outputBaseKey ();
        endLayer();
        /* End Output () */
        w.write ("ECON"); w.newLine();
    }

    private void beginLayer (String name) throws IOException
    {
        if (!(name.equals(lastLayer)))
        {
            lastLayer = name;
            w.write("LAYER "); w.write (name); w.newLine();
        }
    }
    
    private void endLayer()
    {
        lastLayer = null;
        lastFont = null;
        lastColor = Integer.MAX_VALUE;
        lastJustify = Integer.MAX_VALUE;
        lastStyle = Integer.MAX_VALUE;
        lastHeight = Double.MAX_VALUE;
        lastAngle = Double.MAX_VALUE;
    }
    
    /**
     * Outputs a text string to the file. 
     * 
     * @param text
     * @param x
     * @param y
     * @param color
     * @param font
     * @param height
     * @param angle
     * @param justify
     * @param style
     * @throws java.io.IOException
     */
    private void outputText (String text, 
        double x, double y, int color, String font, 
        double height, double angle, int justify, int style) throws IOException
    {
        setColor ( color);
        setTextFont (font);
        setTextHeight (height);
        setTextAngle (angle); 
        setTextJustify (justify);
        setTextStyle (style);

        w.write ("TEXTM "); w.write(text);w.newLine();
        w.write (String.valueOf(x)); w.write(", "); w.write(String.valueOf(y)); w.newLine();
    }

    public void outputLineWithGap (Point2D start, Point2D end, double radius, 
        int color, double width, String style, String fill) throws IOException
    {
        setColor (color);
        setLineWidth (width);
        setLineStyle (style);
        setFillStyle (fill);
        
        double angle = Math.toDegrees(Math.atan((end.getY()-start.getY())/(end.getX()-start.getX())));
        angle = ( 0 > angle) ? angle + 180 : angle - 180; 
        w.write ("LINE REF "); w.write(String.valueOf(start.getX())); w.write(",");
        w.write (String.valueOf(start.getY())); w.write (";<"); 
        w.write(String.valueOf(angle)); w.write(",");
        w.write(String.valueOf(radius)); w.write("; REF ");
        w.write(String.valueOf(end.getX())); w.write(",");
        w.write(String.valueOf(end.getY())); w.write(";<");
        w.write(String.valueOf(angle)); w.write(",");
        w.write(String.valueOf(radius)); w.write(";"); w.newLine();
    }
    /**
     * Put a map symbol by name into the output file. The map symbols are kept
     * in the sector.fcw and subsector.fcw files which were with the GAL2CC 
     * output and should be included in the Cartrographer release.
     * @param symbol name of the symbol
     * @param x x location
     * @param y y locatoin
     * @param hscale horizontal scale, 1.0 = full size
     * @param vscale vertical scale, 1.0 = full size
     * @param rotation rotation clockwise from the define values. 
     * @throws java.io.IOException
     */
    private void outputSymbol (String symbol, double x, double y, double hscale,
        double vscale, double rotation) throws IOException
    {
        w.write ("INSSYM "); w.write(symbol); w.newLine();
        w.write (String.valueOf (hscale)); w.write (";");
        w.write (String.valueOf (vscale)); w.write (";");
        w.write (String.valueOf (rotation)); w.write(";");
        w.write (String.valueOf (x)); w.write (";");
        w.write (String.valueOf (y)); w.write (";"); w.newLine();
    }
    
    private void setColor (int color) throws IOException
    {
        if (lastColor != color)
        {
            lastColor = color;
            w.write ("COLOR "); w.write (String.valueOf(color)); w.newLine();
        }
    }

    private int setColor (Color c) throws IOException
    {
        int color = c.getRGB();
        
        for (int l = 0; l < 32; l+=2)
        {
            if (color == colourMap[l+1]) 
            {
                color = colourMap[l];
                break;
            }
        }
        if (color < 0 || color > 16) color = 5;
        setColor (color);
        return color;
    }
    
    private void setTextStyle (int style) throws IOException
    {
        if (style != lastStyle)
        {
            lastStyle = style;
            w.write ("TSPECS "); w.write(String.valueOf(style)); w.newLine();
        }
    }
    private void setTextJustify (int justify) throws IOException
    {
        if (justify != lastJustify)
        {
            lastJustify = justify;
            w.write ("TSPECJ "); w.write(String.valueOf(justify)); w.newLine();
        }
    }
    private void setTextAngle (double angle) throws IOException
    {
        if (angle != lastAngle)
        {
            lastAngle = angle;
            w.write ("TSPECA "); w.write(String.valueOf(angle)); w.newLine();
        }
    }
    private void setLineWidth (double width) throws IOException
    {
        if (width != lastWidth)
        {
            lastWidth = width;
            w.write ("LWIDTH "); w.write(String.valueOf(width)); w.newLine();
        }
    }
    private void setTextFont (String font) throws IOException
    {
        if (!font.equals(lastFont))
        {
            lastFont = font;
            w.write ("TSPECF "); w.write(font); w.newLine();
        }
    }
    
    private void setLineStyle (String style) throws IOException
    {
        if (!style.equals(lastLineStyle))
        {
            lastLineStyle = style;
            w.write ("LSTYLE "); w.write(style); w.newLine();
        }
    }
    
    private void setFillStyle (String style) throws IOException
    {
        if (style.equals(lastFillStyle))
        {
            lastFillStyle = style;
            w.write ("FSTYLE "); w.write(style); w.newLine();
        }
    }
    
    private void setTextHeight (double height) throws IOException
    {
        if (height != lastHeight)
        {
            lastHeight = height;
            w.write ("TSPECH "); w.write(String.valueOf(height)); w.newLine();
        }
    }
    
    /**
     * Output the Base Key, a small table to the right of the map to show
     * which base symbols (on the map) are which types 
     */
    private void outputBaseKey () throws IOException
    {
    
    	double symbolX = BASE_KEY_SYMBOL_X;
    	double textX = BASE_KEY_TEXT_X;
        double y = BASE_KEY_Y;

        beginLayer ("MAP KEY");
        
        TableRecord d = data.getLocalReferences().getTable(TableRecordKey.BASES);
        for (int i = 0; i < d.size(); i++)
        {
            outputText ( d.getRecord(i).getValue(), textX, y,
            			BASE_KEY_COLOR, BASE_KEY_TEXT_FONT, 
					   BASE_KEY_TEXT_HEIGHT, BASE_KEY_TEXT_ANGLE, 
					   BASE_KEY_TEXT_JUSTIFY, BASE_KEY_TEXT_STYLE);
            outputSymbol (d.getRecord(i).getValue(), symbolX, y, 
                     BASE_KEY_SYMBOL_SCALE_X, BASE_KEY_SYMBOL_SCALE_Y, 
						 BASE_KEY_SYMBOL_ROTATE);
            y -= BASE_KEY_VERTICAL_SEP;                         
        }
        endLayer ();
    }
    
    /**
     * output the trade routes
     */
    private static final double TRADE_ROUTE_EXCLUSION = 5.0;
    private static final double TRADE_ROUTE_LINE_WIDTH = 0.75;
    private static final String TRADE_ROUTE_LINE_STYLE = "Solid";
    private static final String TRADE_ROUTE_FILL_STYLE = "Solid";
    
    private void outputTradeRoutes () throws IOException
    {
        boolean first = true;
        int color;
        Point2D start, end; 
        Links current; 
        HexID hex;
        Iterator i = data.getLinks();
        while (i.hasNext())
        {   
            current = (Links)i.next();
            first = true;
            start = null;

            color = setColor(current.getLinkColor());
            Iterator j = current.getHexes();
            while (j.hasNext())
            {
                hex = (HexID)j.next();
                if (first) 
                {
                    start = hexToCoord (hex);
                    first = false;
                }
                else
                {
                    end = hexToCoord (hex);
                    outputLineWithGap (start, end, TRADE_ROUTE_EXCLUSION,
                        color, TRADE_ROUTE_LINE_WIDTH, TRADE_ROUTE_LINE_STYLE,
                        TRADE_ROUTE_FILL_STYLE);
                    start = end;
                }
            }/* end for hexes in link */
        } /* end for links */
    }

    private final static double HEX_INITAL_X = 10.0;
    private final static double HEX_INITIAL_Y = 8.66025404;
    private final static double HEX_HORIZONTAL_SEPARATION = 15.0;
    private final static double HEX_HEIGHT = 17.32050808;

    private Point2D hexToCoord (HexID hex)
    {
        Point2D.Double l = new Point2D.Double();
        l.x = HEX_INITAL_X + 
            HEX_HORIZONTAL_SEPARATION * (hex.x - 1);
        l.y = HEX_INITIAL_Y + 
            HEX_HEIGHT * (40 - hex.y);
        if (hex.x % 2 == 1) l.y += HEX_HEIGHT / 2;
        return l;
    }
    /**
     * Print the hex numbers in the hexes without systems (if required)
     */
    private void completeSystems()
    {
           
    }

    private static final double SUBSECTOR_KEY_Y = 611.0;
    private static final double SUBSECTOR_KEY_X = 501.0;
    private static final double SUBSECTOR_KEY_VERTICAL_SEP = 4.5;
    private static final int    SUBSECTOR_KEY_COLOR = 16;
    private static final String SUBSECTOR_KEY_FONT = "Arial";
    private static final double SUBSECTOR_KEY_HEIGHT = 3.0;
    private static final double SUBSECTOR_KEY_ANGLE = 0.0;
    private static final int    SUBSECTOR_KEY_JUSTIFY = 0;
    private static final int    SUBSECTOR_KEY_STYLE = 16;
 
    private void outputKeyEntries () throws IOException
    {
        beginLayer("MAP BORDER");
        for (Iterator i = data.getGroups(); i.hasNext();)
        {
            GroupRecord r = (GroupRecord)i.next();
            if (r.getType() == GroupType.SUBSECTOR)
            {
                int index = r.getLocation().x + r.getLocation().y * 4;
            
                outputText (r.getName(), SUBSECTOR_KEY_X, 
                    SUBSECTOR_KEY_Y - SUBSECTOR_KEY_VERTICAL_SEP * index,
                    SUBSECTOR_KEY_COLOR, SUBSECTOR_KEY_FONT,
                    SUBSECTOR_KEY_HEIGHT, SUBSECTOR_KEY_ANGLE, 
                    SUBSECTOR_KEY_JUSTIFY, SUBSECTOR_KEY_STYLE);
            }
        }
        endLayer();
    }
    
    private void outputSystems () throws IOException
    {
        HexLayout layout = EditOptions.getInstance().getScaleLayout(MapScale.SCALE_3);
        HexLinePanel currentItem;
        
        for (Iterator i = data.getSystems(); i.hasNext();)
        {
            StarSystem s = (StarSystem)i.next();
            //layout.drawLayout(s,hexToCoord(s.getLocation()),this);
        }
    }
}