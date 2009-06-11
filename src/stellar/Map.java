package stellar;

import stellar.data.StarSystem;
import stellar.data.GroupRecord;
import stellar.data.GroupType;
import stellar.dialog.EditOptions;
import stellar.dialog.EditSystem;
import stellar.dialog.EditViewOptions;
import stellar.dialog.ViewTableData;
import stellar.io.Resources;
import stellar.map.HexIcons;
import stellar.map.MapIcon;
import stellar.map.MapLabel;
import stellar.map.MapScope;
import stellar.map.SquareIcons;
import stellar.swing.AstrogationChangeListener;
import stellar.swing.StarTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.ComponentAdapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;

/**
 * Map is the core map of the cartogrphy program. It hold the menu bar, toolbar, 
 * the basic map display and table on the scroll pane. 
 * <p>
 * $Id$
 * @version $Revision: 1.14 $
 * 
 */
public class Map extends JFrame implements AstrogationChangeListener, 
                                           PropertyChangeListener
{
    public static final int VIEW_MODE_MAP = 0;
    public static final int VIEW_MODE_TABLE = 1;
    private int viewMode = 0;

    private JLabel statusBar = new JLabel ();

    private BorderLayout layoutMain = new BorderLayout ();

    private MapLabel mapGraphic;
    private HexIcons hexMap = new HexIcons (true);
    private SquareIcons squareMap = new SquareIcons (true);
    private MapIcon map = hexMap;

    private JTable mapTable;
    private StarTableModel mapTableModel;

    //private EditSystem dialogEditSystem;
    private EditOptions dialogEditOptions = EditOptions.getInstance ();
    //private EditViewOptions dialogEditViewOptions = EditViewOptions.getInstance();
    //private ViewTableData dialogViewTables;

    private JScrollPane mapScrollPanel = new JScrollPane ();

    private MapMenu menuBar;
    private MapToolbar mapToolbar;
    private MapCommands mapCommands = new MapCommands (this, null);

    /**
     * Default constructor.
     */
    public Map ()
    {
        try
        {
            jbInit ();
        } catch (Exception e)
        {
            e.printStackTrace ();
        }
        //userPrefs = Preferences.userNodeForPackage(this.getClass());
    }

    /**
     * Handle the <code>ChangeEvent</code>. This is usually the 
     * <code>AstrogrationChangeEvent</code> indicating the map data has changed.
     * While this code does not keep the astrogation data, this method forces
     * the map display to be updated as required. 
     * @param e ChangeEvent to handle. 
     */
    public void stateChanged (ChangeEvent e)
    {
        map.setScale (EditViewOptions.getInstance ().getScale ());
        mapGraphic.invalidate ();
    }

    /**
     * Handle the property change indication. The <code>PropertyChangeEvent</code>
     * is generated by the menu/toolbar to ask for large scale map changes. This
     * method asks the graphics to refresh.
     * @param e <code>PropertyChangeEvent</code> to handle. 
     */
    public void propertyChange (PropertyChangeEvent e)
    {
        if (e.getPropertyName () == MapMenu.VIEW_MAP)
        {
            switchViewMode (VIEW_MODE_MAP);
        }
        if (e.getPropertyName () == MapMenu.VIEW_TABLE)
        {
            switchViewMode (VIEW_MODE_TABLE);
        }
        if (e.getPropertyName () == MapMenu.VIEW_DISPLAY)
        {
            mapViewDisplay_actionPerformed ();
        }
        if (e.getPropertyName () == MapMenu.VIEW_REFRESH)
        {
            mapViewRefresh ();
        }
    }


    private void jbInit ()
    {
        mapToolbar = new MapToolbar (mapCommands);
        menuBar = new MapMenu (mapCommands);
        this.setJMenuBar (menuBar);
        this.getContentPane ().setLayout (layoutMain);
        this.setTitle (Resources.getString ("map.Title"));
        this.addComponentListener (new ComponentAdapter ()
                {
                    public void componentResized (ComponentEvent e)
                    {
                        this_componentResized (e);
                    }

                    public void componentMoved (ComponentEvent e)
                    {
                        this_componentMoved (e);
                    }
                });


        /* Map Hex */
        mapGraphic = new MapLabel (map);
        mapGraphic.setVerticalAlignment (SwingConstants.TOP);
        mapGraphic.addMouseMotionListener (new MouseMotionAdapter ()
                {
                    public void mouseMoved (MouseEvent e)
                    {
                        StringBuffer statusText = new StringBuffer ("");
                        GroupRecord g = map.getGroupData ();
                        if (g != null)
                        {
                            statusText.append (g.getType () + ":" + 
                                               g.getName ());
                        }
                        statusText.append (" -- Location: ");
                        statusText.append (map.getLocationInfo (map.getLocation (e.getX (), 
                                                                                 e.getY ())));
                        //statusText.append (mapGraphic.getMouseLocation(e.getX(), e.getY()));
                        statusText.append ("  ");
                        //statusText.append (mapGraphic.getMouseLink(e.getX(), e.getY()));
                        statusBar.setText (statusText.toString ());
                    }
                });
        mapGraphic.addMouseListener (new MouseAdapter ()
                {
                    public void mouseClicked (MouseEvent e)
                    {
                        mapGraphic_mouseClicked (e);
                    }
                });

        /* map Table */
        mapTableModel = new StarTableModel ();
        mapTable = new JTable (mapTableModel);
        mapTable.setCellSelectionEnabled (true);
        mapTable.setAutoResizeMode (JTable.AUTO_RESIZE_OFF);

        mapScrollPanel.setBackground (Color.green);
        Color backColor = map.getLayout().getOptions().getBackgroundColor();
            //dialogEditOptions.getScaleLayout (map.getScale ()).getBackgroundColor ();
        mapScrollPanel.setViewportBorder (new LineBorder (backColor, 6));
        mapScrollPanel.getViewport ().setBackground (backColor);

        statusBar.setBorder (BorderFactory.createEtchedBorder (EtchedBorder.RAISED));
        //statusBar.setPreferredSize(new Dimension (300,20));
        statusBar.setText ("");


        /* Program always starts in map mode */
        mapScrollPanel.setPreferredSize (mapGraphic.getPreferredSize ());

        this.getContentPane ().add (mapToolbar, BorderLayout.NORTH);
        this.getContentPane ().add (statusBar, BorderLayout.SOUTH);
        mapScrollPanel.getViewport ().add (mapGraphic);
        this.getContentPane ().add (mapScrollPanel, BorderLayout.CENTER);
        //this.getContentPane().add(panelCenter, BorderLayout.CENTER);
        mapCommands.addAstrogationChangeListener (hexMap);
        mapCommands.addAstrogationChangeListener (squareMap);
        mapCommands.addAstrogationChangeListener (mapTableModel);
        //mapCommands.addAstrogationChangeListener (ViewTableData.getInstance ());
        mapCommands.addAstrogationChangeListener (EditSystem.getInstance ());
        mapCommands.addAstrogationChangeListener (EditViewOptions.getInstance ());
        mapCommands.addPropertyChangeListener (mapGraphic);
        mapCommands.addPropertyChangeListener (this);
        mapCommands.setMapIcon (hexMap);
        mapCommands.fileNew (null);
    }

    private void switchViewMode (int mode)
    {
        if (mode == VIEW_MODE_TABLE && viewMode != VIEW_MODE_TABLE)
        {
            viewMode = VIEW_MODE_TABLE;
            mapScrollPanel.getViewport ().remove (mapGraphic);
            mapScrollPanel.getViewport ().add (mapTable);
        }
        if (mode == VIEW_MODE_MAP && viewMode != VIEW_MODE_MAP)
        {
            viewMode = VIEW_MODE_MAP;
            mapScrollPanel.getViewport ().remove (mapTable);
            mapScrollPanel.getViewport ().add (mapGraphic);
        }
    }

    private void mapViewRefresh ()
    {
        Color backColor = map.getLayout().getOptions().getBackgroundColor();
        mapScrollPanel.setViewportBorder (new LineBorder (backColor, 6));
        mapScrollPanel.getViewport ().setBackground (backColor);
        mapGraphic.invalidate ();
        this.repaint ();
    }

    private void mapViewDisplay_actionPerformed ()
    {
        EditViewOptions dialogEditViewOptions = EditViewOptions.getInstance ();
        GroupType level = dialogEditViewOptions.getLevel ();
        MapScope scope = dialogEditViewOptions.getScope ();
        if (map.getLevel () != level)
        {
            if (level == GroupType.SYSTEM)
            {
                map = hexMap;
                mapGraphic.setIcon (map);
                map.setLevel (level);
            } else
            {
                map = squareMap;
                mapGraphic.setIcon (map);
                map.setLevel (level);
            }
            map.setSize ();
        }

        switch (scope)
        {
        case SUBSECTOR:
            map.setSize (10, 8);
            break;
        case QUADRANT:
            if (level == GroupType.SYSTEM)
            {
                map.setSize (20, 16);
            }
            if (level == GroupType.SUBSECTOR)
            {
                map.setSize (2, 2);
            }
            break;
        case SECTOR:
            if (level == GroupType.SYSTEM)
            {
                map.setSize (40, 32);
            }
            if (level == GroupType.QUADRANT)
            {
                map.setSize (2, 2);
            }
            if (level == GroupType.SUBSECTOR)
            {
                map.setSize (4, 4);
            }
            break;
        case DOMAIN:
            if (level == GroupType.SYSTEM)
            {
                map.setSize (80, 64);
            }
            if (level == GroupType.SECTOR)
            {
                map.setSize (2, 2);
            }
            if (level == GroupType.QUADRANT)
            {
                map.setSize (4, 4);
            }
            if (level == GroupType.SUBSECTOR)
            {
                map.setSize (8, 8);
            }
            break;

        case ALL:
            map.setSize ();
            break;
        }

        map.setScope (scope);
        map.setScale (dialogEditViewOptions.getScale ());
        mapGraphic.invalidate ();
        this.repaint ();
    }


    private void mapGraphic_mouseClicked (MouseEvent e)
    {
        StarSystem s;
        EditSystem dialogEditSystem = EditSystem.getInstance ();

        s = map.getSystemData (e.getX (), e.getY ());
        if (s != null)
        {
            dialogEditSystem.setSystem (s);
            dialogEditSystem.setVisible (true);
            if (dialogEditSystem.isDuplicate ())
            {
                map.getMapData ().addSystem (dialogEditSystem.getSystem ());
            }
        }
    }

    private void this_componentResized (ComponentEvent e)
    {
        dialogEditOptions.setAppWidth (e.getComponent ().getWidth ());
        dialogEditOptions.setAppHeight (e.getComponent ().getHeight ());
    }

    private void this_componentMoved (ComponentEvent e)
    {
        dialogEditOptions.setAppX (e.getComponent ().getLocation ().x);
        dialogEditOptions.setAppY (e.getComponent ().getLocation ().y);
    }

}