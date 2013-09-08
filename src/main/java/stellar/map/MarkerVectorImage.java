package stellar.map;

import stellar.data.TableRecord;
import stellar.dialog.ViewTableData;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import com.softstart.VectorImage.VectorImage;
import stellar.data.StarSystem;

import stellar.data.SystemMarkerType;
import stellar.data.TableRecordKey;

/**
 * MarkerVectorImage is used for creating and drawing a marker (a small colored 
 * circle) on the maps. 
 * 
 * The default uses a sequence of 15 colors (black, red, orange, yellow, green, cyan, blue, magenta, white)
 * 
 * You can 
 * @author Thomas Jones-Low
 * @version $Revision: 1.9 $
 */
public class MarkerVectorImage extends VectorImage 
{

    Color colorMap [] = {Color.black, Color.red, Color.red, Color.orange, Color.orange,
        Color.yellow, Color.yellow, Color.green, Color.green, 
        Color.cyan, Color.cyan, Color.blue, Color.blue, Color.magenta, Color.magenta,
                         Color.white};
        
    StarSystem system; 
    SystemMarkerType marker = SystemMarkerType.PRESENCE;
    TableRecord colorRecord = null;
    
    public MarkerVectorImage()
    {
        super (new Ellipse2D.Float(0,0,8,8), Color.black, new BasicStroke(1));
        this.setFillShape(true);
    }

    public void drawWorld (Graphics2D g2, Point2D center, StarSystem system)
    {
        Color color;
        if (marker == SystemMarkerType.NONE) return; 
        this.system = system;
        
        if (colorRecord != null)
        {
            color = colorRecord.getRecordCode(system.getSystemMarkerKey(marker)).getColor();
        }
        else
        {
            int index = system.getSystemMarkerInt(marker);
            if (index >= colorMap.length) 
                color = colorMap[colorMap.length-1];
            else
                color = colorMap[index];
        }
        drawMarker (color, center, g2);
    }

    public void drawMarker (Color color, Point2D center, Graphics2D g2)
    {
        this.setColor (color);
        g2.translate (center.getX(), center.getY());
        this.drawImage(g2);
        g2.translate(-center.getX(), -center.getY());
        
    }
    public void setStarSystem (StarSystem newSystem) { this.system = newSystem; }
    public SystemMarkerType getMarker () { return marker; } 

    public void setMarker (SystemMarkerType newMarker)
    { 
        if (this.marker == newMarker) return;
        this.marker = newMarker; 
        TableRecord table;

        //References ref = ViewTableData.getInstance().getGlobalReferences();
        //if (ref == null) { colorRecord = null; return; }

        switch (newMarker)
        {
            case PRESENCE: table = null; break;
            case REVERSE: table = null; break;
            case PORT:  table = ViewTableData.getInstance().getTableRecord(TableRecordKey.PORTS); break;
            case SIZE:  table = ViewTableData.getInstance().getTableRecord(TableRecordKey.SIZE); break;
            case ATMOSPHERE:  table = ViewTableData.getInstance().getTableRecord(TableRecordKey.ATMOSPHERE); break;
            case HYDROGRAPHIC: table = ViewTableData.getInstance().getTableRecord(TableRecordKey.HYDROGRAPHICS); break;
            case POPULATION:   table = ViewTableData.getInstance().getTableRecord(TableRecordKey.POPULATION); break;
            case GOVERNMENT:   table = ViewTableData.getInstance().getTableRecord(TableRecordKey.GOVERNMENT); break;
            case LAWLEVEL:   table = ViewTableData.getInstance().getTableRecord(TableRecordKey.LAW_LEVEL); break;
            case TECHNOLOGY:  table = ViewTableData.getInstance().getTableRecord(TableRecordKey.TECHNOLOGY); break;
            case PRIMARY: table = ViewTableData.getInstance().getTableRecord(TableRecordKey.STAR_SPECTRUM); break;
            default: throw new IllegalArgumentException ();
        }
        if (table == null) { colorRecord = null; return; }
        if (table.getRecord(0).getColor() != null) colorRecord = table;
        else colorRecord = null;
    } 
    public void setMarkerColors (TableRecord table)
    {
        colorRecord = table;
    }
}
