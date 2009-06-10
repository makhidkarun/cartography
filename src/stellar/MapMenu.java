package stellar;

import stellar.io.AppDefaults;
import stellar.io.Resources;
import stellar.swing.Command;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

/**
 * Map menu builds the main menu for the cartography main menu. This uses the 
 * AppDefualts and the Resources (wingui.properties) file to generate the menu
 * item text and icons. As the user selects a menu item, the selection is processes
 * through the MapCommands class.
 * @see AppDefaults
 * @see Resources
 * @see MapCommands
 * @author Thomas Jones-Low
 * @version $Revision: 1.11 $
 */
public class MapMenu extends JMenuBar
{

    private AppDefaults defaults = new AppDefaults ();
    private MapCommands commands;

    private JMenu menuFile;
    private JMenu menuEdit;
    private JMenu menuView;
    private JMenu menuViewShift;
    private JMenu menuHelp;

    public static final String VIEW_MAP = "View Map";
    public static final String VIEW_TABLE = "View Table";
    public static final String VIEW_CLEAR = "View Clear";
    public static final String VIEW_REFRESH = "View Refresh";
    public static final String VIEW_DISPLAY = "View Display";

    public MapMenu (MapCommands commands)
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

        defaults.addResourceBundle (Resources.getResourceBundleName ());

        menuFile = new JMenu (new Command (commands, "file", defaults));
        
        menuFile.add (new JMenuItem (new Command (commands, "fileNew", defaults)));
        menuFile.add (new JMenuItem (new Command (commands, "fileOpen", defaults)));
        menuFile.add (new JMenuItem (new Command (commands, "fileSave", defaults)));
        menuFile.add (new JMenuItem (new Command (commands, "fileSaveAs", defaults)));
        menuFile.add (new JMenuItem (new Command (commands, "fileExit", defaults)));

        menuEdit = new JMenu (new Command (commands, "edit", defaults));
        menuEdit.add (new JMenuItem (new Command (commands, "editSystem", defaults)));
        menuEdit.addSeparator ();
        menuEdit.add (new JMenuItem (new Command (commands, "editLinks", defaults)));
        menuEdit.add (new JMenuItem (new Command (commands, "editGroups", defaults)));
        menuEdit.add (new JMenuItem (new Command (commands, "editTable", defaults)));
        menuEdit.addSeparator ();
        menuEdit.add (new JMenuItem (new Command (commands, "editPreferences", defaults)));

        menuViewShift = new JMenu (new Command (commands, "viewShift", defaults));

        menuViewShift.add (new JMenuItem (new Command (commands, "viewShiftUp", defaults)));
        menuViewShift.add (new JMenuItem (new Command (commands, "viewShiftDown", defaults)));
        menuViewShift.add (new JMenuItem (new Command (commands, "viewShiftLeft", defaults)));
        menuViewShift.add (new JMenuItem (new Command (commands, "viewShiftRight", defaults)));
        menuViewShift.add (new JMenuItem (new Command (commands, "viewShiftZoomIn", defaults)));
        menuViewShift.add (new JMenuItem (new Command (commands, "viewShiftZoomOut", defaults)));

        menuView = new JMenu (new Command (commands, "view", defaults));

        menuView.add (new JMenuItem (new Command (commands, "viewMap", defaults)));
        menuView.add (new JMenuItem (new Command (commands, "viewTable", defaults)));
        menuView.add (new JMenuItem (new Command (commands, "viewDisplay", defaults)));
        menuView.add (new JMenuItem (new Command (commands, "viewLegend", defaults)));
        menuView.addSeparator ();
        menuView.add (menuViewShift);
        menuView.add (new JMenuItem (new Command (commands, "viewRefresh", defaults)));

        menuHelp = new JMenu (new Command (commands, "help", defaults));

        menuHelp.add (new JMenuItem (new Command (commands, "helpAbout", defaults)));

        this.add (menuFile);
        this.add (menuEdit);
        this.add (menuView);
        this.add (menuHelp);
    }
}
