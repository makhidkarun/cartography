package stellar.map;
import com.softstart.VectorImage.VectorImage;
import stellar.data.TableRecord;
import stellar.data.TableRecordKey;
import stellar.dialog.ViewTableData;
import java.awt.geom.Ellipse2D;
import java.awt.BasicStroke;
import java.awt.Color;

public class ZoneVectorImage extends VectorImage 
{
    TableRecord colorRecord = null;
    
    public ZoneVectorImage()
    {
        super (new Ellipse2D.Float(0,0,20,20), Color.black, new BasicStroke(1));
        this.setFillShape(true);
/*
        Arc2D drawShape = new Arc2D.Float (0, 0, 15, 15, 315, 270, Arc2D.OPEN);
        this.setShape(drawShape);
        this.setStroke(new BasicStroke(3));
*/        
    }

    public void setColors ()
    {
        TableRecord table = ViewTableData.getInstance().getGlobalReferences().getTable(TableRecordKey.TRAVEL_ZONE);
        if (table.getRecord(0).getColor() != null) colorRecord = table;
        else colorRecord = null;        
    }
    
    public void setZone (char zone)
    {
        if (colorRecord != null)
        {
            Color color = colorRecord.getRecordCode(String.valueOf(zone)).getColor();
            this.setColor (color);    
        }
        else if (zone == 'R') { this.setColor (Color.RED); }
        else if (zone == 'A') { this.setColor (Color.YELLOW); } 
        else if (zone == 'G') { this.setColor (Color.GREEN); } 
    }
    
}