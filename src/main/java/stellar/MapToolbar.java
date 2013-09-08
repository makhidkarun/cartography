package stellar;

import stellar.io.AppDefaults;
import stellar.io.Resources;
import stellar.swing.Command;
import stellar.swing.JButtonNoFocus;

import java.awt.event.ActionListener;

import javax.swing.JToolBar;

/**
 * MapToolbar creates the top level toolbar for the map. This uses 
 * <code>AppDefaults</code> and <code>Command</code> to construct the icons,
 * text and other settings for the buttons from the proper .properties file. 
 * 
 * @see AppDefaults
 * @see MapCommands
 * @see com.softstart.stellar.swing.Command
 * @version $Id$
 */
public class MapToolbar extends JToolBar
{
    private MapCommands commands;
    private AppDefaults defaults = new AppDefaults ();

    private JButtonNoFocus fileOpen;
    private JButtonNoFocus fileSave;

    private JButtonNoFocus viewMap;
    private JButtonNoFocus viewTable;
    private JButtonNoFocus viewProperties;

    private JButtonNoFocus viewZoomIn;
    private JButtonNoFocus viewZoomOut;
    private JButtonNoFocus viewMoveLeft;
    private JButtonNoFocus viewMoveRight;
    private JButtonNoFocus viewMoveUp;
    private JButtonNoFocus viewMoveDown;

    /**
     * Default constructor using the MapCommands appropriate to handle the 
     * set of icon buttons. 
     * @param commands
     */
    public MapToolbar (MapCommands commands)
    {
        this.commands = commands;
        try
        {
            jbInit ();
        } catch (Exception e)
        {
            e.printStackTrace ();
        }

    }

    private void jbInit ()
    {
        this.setFocusable (false);
        defaults.addResourceBundle (Resources.getResourceBundleName ());
        fileOpen = 
                new JButtonNoFocus (new Command (commands, "fileOpen", defaults));
        fileSave = 
                new JButtonNoFocus (new Command (commands, "fileSave", defaults));
        viewMap = 
                new JButtonNoFocus (new Command (commands, "viewMap", defaults));
        viewTable = 
                new JButtonNoFocus (new Command (commands, "viewTable", defaults));
        viewProperties = 
                new JButtonNoFocus (new Command (commands, "editPreferences", 
                                                 defaults));
        viewZoomIn = 
                new JButtonNoFocus (new Command (commands, "viewShiftZoomIn", 
                                                 defaults));
        viewZoomOut = 
                new JButtonNoFocus (new Command (commands, "viewShiftZoomOut", 
                                                 defaults));
        viewMoveLeft = 
                new JButtonNoFocus (new Command (commands, "viewShiftLeft", 
                                                 defaults));
        viewMoveRight = 
                new JButtonNoFocus (new Command (commands, "viewShiftRight", 
                                                 defaults));
        viewMoveUp = 
                new JButtonNoFocus (new Command (commands, "viewShiftUp", defaults));
        viewMoveDown = 
                new JButtonNoFocus (new Command (commands, "viewShiftDown", 
                                                 defaults));
        /*
        fileOpen.setIcon(Resources.getIcon("open"));
        fileOpen.setText(null);
        fileOpen.setFocusable(false);
        fileOpen.setToolTipText("Open (Ctrl+O)");
        fileOpen.setActionCommand(MapMenu.FILE_OPEN);

        fileSave.setIcon(Resources.getIcon("save"));
        fileSave.setText(null);
        fileSave.setFocusable(false);
        fileSave.setToolTipText("Save (Ctrl+S)");
        fileSave.setActionCommand(MapMenu.FILE_SAVE);

        viewMap.setIcon(Resources.getIcon("image"));
        viewMap.setText(null);
        viewMap.setFocusable(false);
        viewMap.setToolTipText("View Map");
        viewMap.setActionCommand(MapMenu.VIEW_MAP);

        viewTable.setIcon(Resources.getIcon("tableinsert"));
        viewTable.setText(null);
        viewTable.setFocusable(false);
        viewTable.setToolTipText("View Table");
        viewTable.setActionCommand(MapMenu.VIEW_TABLE);

        viewProperties.setIcon(Resources.getIcon("properties"));
        viewProperties.setText(null);
        viewProperties.setFocusable(false);
        viewProperties.setToolTipText("Edit map layout preferences");
        viewProperties.setActionCommand(MapMenu.EDIT_PREFERENCES);

        viewZoomIn.setIcon(Resources.getIcon("zoomin"));
        viewZoomIn.setText(null);
        viewZoomIn.setFocusable(false);
        viewZoomIn.setToolTipText("Zoom In (Ctrl+=)");
        viewZoomIn.setActionCommand(MapMenu.VIEW_SHIFT_ZOOMIN);

        viewZoomOut.setIcon(Resources.getIcon("zoomout"));
        viewZoomOut.setText(null);
        viewZoomOut.setFocusable(false);
        viewZoomOut.setToolTipText("Zoom Out (Ctrl+-)");
        viewZoomOut.setActionCommand(MapMenu.VIEW_SHIFT_ZOOMOUT);

        viewMoveLeft.setIcon(Resources.getIcon("moveleft"));
        viewMoveLeft.setText(null);
        viewMoveLeft.setFocusable(false);
        viewMoveLeft.setToolTipText("Move left to next group");
        viewMoveLeft.setActionCommand(MapMenu.VIEW_SHIFT_LEFT);

        viewMoveRight.setIcon(Resources.getIcon("moveright"));
        viewMoveRight.setText(null);
        viewMoveRight.setFocusable(false);
        viewMoveRight.setToolTipText("Move Right to next group");
        viewMoveRight.setActionCommand(MapMenu.VIEW_SHIFT_RIGHT);

        viewMoveUp.setIcon(Resources.getIcon("moveup"));
        viewMoveUp.setText(null);
        viewMoveUp.setFocusable(false);
        viewMoveUp.setToolTipText("Move up to next group");
        viewMoveUp.setActionCommand(MapMenu.VIEW_SHIFT_UP);

        viewMoveDown.setIcon(Resources.getIcon("movedown"));
        viewMoveDown.setText(null);
        viewMoveDown.setFocusable(false);
        viewMoveDown.setToolTipText("Move down to next group");
        viewMoveDown.setActionCommand(MapMenu.VIEW_SHIFT_DOWN);
        */
        this.add (fileOpen, null);
        this.add (fileSave, null);
        this.add (new JToolBar.Separator (), null);
        this.add (viewMap, null);
        this.add (viewTable, null);
        this.add (viewProperties, null);
        this.add (new JToolBar.Separator (), null);
        this.add (viewZoomIn, null);
        this.add (viewZoomOut, null);
        this.add (viewMoveLeft, null);
        this.add (viewMoveRight, null);
        this.add (viewMoveUp, null);
        this.add (viewMoveDown, null);
    }

    public void addGlobalActionListener (ActionListener a)
    {
        fileOpen.addActionListener (a);
        fileSave.addActionListener (a);
        viewMap.addActionListener (a);
        viewTable.addActionListener (a);
        viewProperties.addActionListener (a);
        viewZoomIn.addActionListener (a);
        viewZoomOut.addActionListener (a);
        viewMoveLeft.addActionListener (a);
        viewMoveRight.addActionListener (a);
        viewMoveUp.addActionListener (a);
        viewMoveDown.addActionListener (a);
    }
}
