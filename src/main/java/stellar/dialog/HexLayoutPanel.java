package stellar.dialog;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import javax.swing.border.BevelBorder;

import stellar.MapPreferences;

import stellar.map.MapScale;
import stellar.map.MapScope;

/**
 * HexLayoutPanel encapuslates the entire panel which holds the hex layout.
 * There are five of these panels, one for each Map Scale. These panels hold
 * one HexOptionsPanel, plus a number of HexLinePanels (1 to 5) depending upon
 * map scale.
 * @author Thomas Jones-Low
 * @version $Id$
 */
public class HexLayoutPanel extends JPanel
{
    private MapScale scale;
    private HexOptionPanel hexOptions; 
    
    private JPanel hexLines = new JPanel();
    private BorderLayout layout = new BorderLayout();
    
    private MapPreferences prefs = MapPreferences.getInstance();
    
    public HexLayoutPanel(MapScale scale)
    {
        this.scale = scale;
        try
        {
            jbInit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        this.setLayout(layout);
        
        FormLayout formLayout1 = new FormLayout("fill:pref, $rgap, pref");
        PanelBuilder builder = new PanelBuilder (formLayout1, hexLines);
        CellConstraints cc = new CellConstraints();
        builder.border(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
       
        int lines = prefs.getScaleLayout(scale).getLineCount();
        
        builder.appendRow("$rgap:grow");
        
        for (int i = 0; i < lines; i++)
        {
            builder.appendRow(FormSpecs.PREF_ROWSPEC);
            builder.appendRow(FormSpecs.RELATED_GAP_ROWSPEC);
            builder.add (new HexLinePanel (prefs.getScaleLayout(scale).getLine(i)),
                        cc.xy(1, i * 2 + 2));
        }
        builder.appendRow ("$rgap:grow");
        
        hexOptions = new HexOptionPanel (prefs.getScaleLayout(scale));
        hexOptions.setMapData(prefs.getMapData());
        hexOptions.setMapScale(scale);
        switch (scale)
        {
            case SCALE_1: hexOptions.setMapScope(MapScope.DOMAIN); break;
            case SCALE_2: hexOptions.setMapScope(MapScope.SECTOR); break;
            case SCALE_3: hexOptions.setMapScope(MapScope.QUADRANT); break;
            case SCALE_4:
            case SCALE_5: hexOptions.setMapScope(MapScope.SUBSECTOR); break;
        }
        
        add (hexLines, BorderLayout.CENTER);
        add (hexOptions, BorderLayout.EAST);
    }
}
