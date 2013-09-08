package stellar;

import com.lowagie.text.DocumentException;

import stellar.data.Astrogation;
import stellar.io.AstrogationFileReader;
import stellar.io.Resources;
import stellar.swing.AstrogationChangeEvent;
import stellar.data.GroupRecord;
import stellar.data.HexID;
import stellar.dialog.EditGroups;
import stellar.dialog.EditLink;
import stellar.dialog.EditOptions;
import stellar.dialog.EditSystem;
import stellar.dialog.EditViewOptions;
import stellar.dialog.Map_AboutBoxPanel1;
import stellar.dialog.ViewTableData;
import stellar.io.AccessXMLFile;
import stellar.io.filter.CXMLFileFilter;
import stellar.io.GalDATReader;
import stellar.io.GalSARReader;
import stellar.io.filter.JPEGFileFilter;
import stellar.io.filter.PDFFileFilter;
import stellar.io.PDFFileWriter;
import stellar.io.filter.SARFileFilter;
import stellar.io.filter.SECFileFilter;
import stellar.io.SECFileReader;
import stellar.io.SECFileStateMachineException;
import stellar.io.filter.WBSFileFilter;
import stellar.io.WBSReader;
import stellar.io.filter.XMLFileFilter;
import stellar.map.MapIcon;
import stellar.swing.AstrogationChangeListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.jibx.runtime.JiBXException;

/**
 * MapCommands implements the handlers for the menu and toolbar commands in one
 * place. <code>MapMenu</code> and <code>MapToolbar</code> use this class 
 * indirectly through the <code>Action</code> interface and .properties files to 
 * construct the menu items and toolbar buttons. 
 * 
 * @see MapMenu
 * @see MapToolbar
 * @see com.softstart.stellar.io.AppDefaults
 * @see com.softstart.stellar.swing.Command
 * @version $Id$
 */
public class MapCommands
{
    EventListenerList listenerList = new EventListenerList ();
    PropertyChangeEvent propertyChange;
    PropertyChangeEvent refreshMap = 
        new PropertyChangeEvent (this, MapMenu.VIEW_REFRESH, null, null);
    Component display;
    MapIcon map;

    public MapCommands (Component display, MapIcon map)
    {
        this.display = display;
        this.map = map;
    }

    public void setMapIcon (MapIcon map)
    {
        this.map = map;
    }

    public void addChangeListener (ChangeListener l)
    {
        listenerList.add (ChangeListener.class, l);
    }

    public void removeChangeListener (ChangeListener l)
    {
        listenerList.remove (ChangeListener.class, l);
    }

    /**
     * Adds an <code>AstrogationChangeListener</code> to the command handler.
     * @param l the <code>AstrogationChangeListener</code> to be added.
     */
    public void addAstrogationChangeListener (AstrogationChangeListener l)
    {
        listenerList.add (AstrogationChangeListener.class, l);
    }

    /**
     * Removes an <code>AstrogationChangeListener</code> from the command handler.
     * @param l the <code>AstrogationChangeListener</code> to be removed.
     */
    public void removeAstrogationChangeListener (AstrogationChangeListener l)
    {
        listenerList.remove (AstrogationChangeListener.class, l);
    }

    /**
     * Adds a <code>PropertyChangeListener</code> to the command handler.
     * @param l the <code>PropertyChangeListener</code> to be added. 
     */
    public void addPropertyChangeListener (PropertyChangeListener l)
    {
        listenerList.add (PropertyChangeListener.class, l);
    }

    /**
     * Removed a <code>PropertyChangeListener</code> from the command handler.
     * @param l the <code>PropertyChangeListener</code> to be removed. 
     */
    public void removePropertyChangeListener (PropertyChangeListener l)
    {
        listenerList.remove (PropertyChangeListener.class, l);
    }

    protected void fireAstrogationChanged (Astrogation data)
    {
        AstrogationChangeEvent change = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList ();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == AstrogationChangeListener.class)
            {
                // Lazily create the event:
                if (change == null)
                    change = new AstrogationChangeEvent (data);
                ((AstrogationChangeListener)listeners[i + 
                 1]).stateChanged (change);
            }
        }
    }

    protected void fireStateChanged ()
    {
        ChangeEvent change = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList ();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == ChangeListener.class)
            {
                // Lazily create the event:
                if (change == null)
                    change = new ChangeEvent (this);
                ((ChangeListener)listeners[i + 1]).stateChanged (change);
            }
        }
    }

    protected void firePropertyChanged ()
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList ();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == PropertyChangeListener.class)
            {
                // Lazily create the event:
                ((PropertyChangeListener)listeners[i + 
                 1]).propertyChange (propertyChange);
            }
        }
    }

    public void fileNew (ActionEvent e)
    {
        Astrogation starData = new Astrogation ();
        setAstrogrationData (starData);
        fireAstrogationChanged (starData);
        viewRefresh (e);
    }

    public void fileOpen (ActionEvent e)
    {
        AstrogationFileReader data = null;
        Astrogation starData = null;
        JFileChooser chooser = new JFileChooser ();
        chooser.setDialogTitle (Resources.getString ("map.openTitle"));
        chooser.setCurrentDirectory (new File (MapPreferences.getInstance().getWorkingDir()));
        chooser.addChoosableFileFilter (new SARFileFilter ());
        chooser.addChoosableFileFilter (new SECFileFilter ());
        chooser.addChoosableFileFilter (new WBSFileFilter ());
        chooser.addChoosableFileFilter (new CXMLFileFilter ());
        chooser.addChoosableFileFilter (new XMLFileFilter ());
        int option = chooser.showOpenDialog (display);
        if (option == JFileChooser.APPROVE_OPTION)
        {
            display.setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));
            File file = chooser.getSelectedFile ();
            if (file.canRead () && file.getName ().endsWith ("xml"))
            {
                data = new AccessXMLFile (file);
                if (file.getName ().endsWith ("cxml"))
                {
                    ((AccessXMLFile)data).setUseCompressedFile (true);
                }
            } else if (file.canRead () && 
                       file.getName ().toLowerCase ().endsWith ("sec"))
            {
                data = new SECFileReader (file);
            } else if (file.canRead () && 
                       file.getName ().toLowerCase ().endsWith ("dat"))
            {
                data = new GalDATReader (file);
            } else if (file.canRead () && 
                       file.getName ().toLowerCase ().endsWith ("sar"))
            {
                data = new GalSARReader (file);
            } else if (file.canRead () && 
                       file.getName ().toLowerCase ().endsWith ("wbs"))
            {
                data = new WBSReader (file);
            }
            if (data == null)
            {
                display.setCursor (Cursor.getDefaultCursor ());
                JOptionPane.showMessageDialog (display, 
                                               "Unrecognized data file. No data loaded", Resources.getString ("map.NoFileTitle"), 
                                               JOptionPane.ERROR_MESSAGE);
                return;
            }
            try
            {
                data.read ();
            } catch (FileNotFoundException ex)
            {
                JOptionPane.showMessageDialog (display, ex.getMessage (), Resources.getString ("map.NoFileTitle"), 
                                               JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex)
            {
                JOptionPane.showMessageDialog (display, ex.getMessage (), Resources.getString ("map.IOExceptionTitle"), 
                                               JOptionPane.ERROR_MESSAGE);
            } catch (SECFileStateMachineException ex)
            {
                JOptionPane.showMessageDialog (display, ex.getMessage (), 
                                               "Error: Sector File Read State Machine Error", 
                                               JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace ();
            } catch (JiBXException ex)
            {
                JOptionPane.showMessageDialog (display, ex.getMessage (), 
                                               "Error: Global Reference Read Failure", 
                                               JOptionPane.ERROR_MESSAGE);
            }
            starData = data.getAstrogation ();
            display.setCursor (Cursor.getDefaultCursor ());
            //statusBar.setText("Rendering Map...");
            MapPreferences.getInstance ().setCurrentFile(file.getAbsolutePath());
            MapPreferences.getInstance ().setWorkingDir(file.getParent ());
            if (starData != null)
            {
                setAstrogrationData (starData);
            }
            fireAstrogationChanged (starData);
            viewRefresh (e);
            //mapGraphic.invalidate();
        } // if approved to open the file
    }

    public void fileSave (ActionEvent e)
    {
        String outputFile = MapPreferences.getInstance ().getCurrentFile ();
        if (outputFile == null)
        {
            fileSaveAs (e);
            return;
        }
        if (!outputFile.endsWith ("xml"))
        {
            /* replace the file name with an xml file name */
            outputFile = 
                    outputFile.substring (0, outputFile.length () - 4) + ".xml";
            MapPreferences.getInstance ().setCurrentFile (outputFile);
        }
        AccessXMLFile data = new AccessXMLFile (outputFile);
        data.setAstrogation (map.getMapData ());
        if (outputFile.endsWith ("cxml"))
        {
            data.setUseCompressedFile (true);
        }
        try
        {
            data.write ();
        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog (display, ex.getMessage (), Resources.getString ("map.IOExceptionTitle"), 
                                           JOptionPane.ERROR_MESSAGE);
        } catch (JiBXException ex)
        {
            JOptionPane.showMessageDialog (display, ex.getMessage (), 
                                           "Error: JiBX XML Writer Exception", 
                                           JOptionPane.ERROR_MESSAGE);
        }
    }

    public void fileSaveAs (ActionEvent e)
    {
        JFileChooser chooser = new JFileChooser ();
        chooser.setDialogTitle (Resources.getString ("map.saveTitle"));
        chooser.setCurrentDirectory (new File (MapPreferences.getInstance ().getWorkingDir ()));
        chooser.addChoosableFileFilter (new JPEGFileFilter ());
        chooser.addChoosableFileFilter (new PDFFileFilter ());
        chooser.addChoosableFileFilter (new CXMLFileFilter ());
        chooser.addChoosableFileFilter (new XMLFileFilter ());
        int option = chooser.showSaveDialog (display);

        if (option == JFileChooser.APPROVE_OPTION)
        {
            File file = chooser.getSelectedFile ();
            MapPreferences.getInstance ().setWorkingDir(file.getParent ());
            MapPreferences.getInstance ().setCurrentFile (file.getAbsolutePath ());

            if (chooser.getFileFilter () instanceof JPEGFileFilter)
            {
                if (!file.getName ().endsWith (".jpeg"))
                {
                    file = new File (file.getAbsoluteFile () + ".jpeg");
                }
                try
                {
                    map.write (file);
                } catch (IOException ex)
                {
                    JOptionPane.showMessageDialog (display, ex.getMessage (), Resources.getString ("map.IOExceptionTitle"), 
                                                   JOptionPane.ERROR_MESSAGE);
                }
            } else if (chooser.getFileFilter () instanceof PDFFileFilter)
            {
                if (!file.getName ().endsWith (".pdf"))
                {
                    file = new File (file.getAbsoluteFile () + ".pdf");
                }
                try
                {
                    PDFFileWriter data = new PDFFileWriter (file);
                    data.setMapData (map.getMapData ());
                    data.setScale (map.getScale ());
                    data.setScope (map.getScope ());
                    if (map.getGroupData () != null)
                        data.setGroup (map.getGroupData ());
                    data.write ();
                } catch (IOException ex)
                {
                    JOptionPane.showMessageDialog (display, ex.getMessage (), Resources.getString ("map.IOExceptionTitle"), 
                                                   JOptionPane.ERROR_MESSAGE);
                } catch (DocumentException ex)
                {
                    JOptionPane.showMessageDialog (display, ex.getMessage (), 
                                                   "Error: Document Exception", 
                                                   JOptionPane.ERROR_MESSAGE);
                }
            } else
            {
                fileSave (e);
            }
        }
    }

    public void fileExit (ActionEvent e)
    {
        MapPreferences.getInstance ().savePreferences ();
        System.exit (0);
    }


    public void editSystem (ActionEvent e)
    {
        Astrogation data;
        EditSystem dialog = EditSystem.getInstance ();
        dialog.setLocationRelativeTo (display);
        dialog.newSystem ();
        dialog.setVisible (true);
        if (dialog.getSystem () == null)
            return;
        if (map.getMapData () == null)
        {
            data = new Astrogation ();
            data.addSystem (dialog.getSystem ());
        } else
        {
            data = map.getMapData ();
            data.addSystem (dialog.getSystem ());
        }
        fireAstrogationChanged (data);

    }

    public void editGroups (ActionEvent e)
    {
        EditGroups dialog = new EditGroups (map.getMapData ());
        dialog.setLocationRelativeTo (display);
        dialog.setVisible (true);
    }

    public void editLinks (ActionEvent e)
    {
        EditLink dialog = new EditLink (map.getMapData ());
        dialog.setLocationRelativeTo (display);
        dialog.setVisible (true);
    }

    public void editTable (ActionEvent e)
    {
        //ViewTableData.getInstance ().setVisible (true);
        ViewTableData instance = new ViewTableData();
        instance.setData(map.getMapData());
        instance.setVisible(true);
    }

    public void editPreferences (ActionEvent e)
    {
        EditOptions.getInstance ().setVisible (true);
        viewRefresh (e);
    }

    public void viewMap (ActionEvent e)
    {
        propertyChange = 
                new PropertyChangeEvent (this, MapMenu.VIEW_MAP, null, null);
        firePropertyChanged ();
        //        if (viewMode != VIEW_MODE_MAP)
        //        {
        //            viewMode = VIEW_MODE_MAP;
        //            mapScrollPanel.getViewport().remove(mapTable);
        //            mapScrollPanel.getViewport().add(mapGraphic);
        //        }
    }

    public void viewTable (ActionEvent e)
    {
        propertyChange = 
                new PropertyChangeEvent (this, MapMenu.VIEW_TABLE, null, null);
        firePropertyChanged ();
        /*
        if (viewMode != VIEW_MODE_TABLE)
        {
            viewMode = VIEW_MODE_TABLE;
            mapScrollPanel.getViewport().remove(mapGraphic);
            mapScrollPanel.getViewport().add(mapTable);
        }\\

*/
    }
    
    public void viewLegend (ActionEvent e)
    {
        JOptionPane.showMessageDialog (display, "View Legend Dialog placeholder dialog", 
                                       "View Legend", 
                                       JOptionPane.INFORMATION_MESSAGE);        
    }

    public void viewShiftZoomIn (ActionEvent e)
    {
        map.zoomIn ();
        Color backColor = map.getLayout().getOptions().getBackgroundColor();
        EditViewOptions.getInstance ().setScale (map.getScale ());
        propertyChange = refreshMap;
        firePropertyChanged ();
    }

    public void viewShiftZoomOut (ActionEvent e)
    {
        map.zoomOut ();
        Color backColor = map.getLayout().getOptions().getBackgroundColor();
        EditViewOptions.getInstance ().setScale (map.getScale ());
        
        propertyChange = refreshMap;
        firePropertyChanged ();
    }

    public void viewRefresh (ActionEvent e)
    {
        propertyChange = refreshMap;
        firePropertyChanged ();
    }

    public void viewShiftRight (ActionEvent e)
    {
        GroupRecord g = map.getGroupData ();
        Astrogation data = map.getMapData ();
        HexID h = new HexID (g.getLocation ().toString ());
        //h.convertHextoID();
        h.x += 1;
        g = data.getGroup (h, g.getType ());
        if (g != null)
            map.setGroup (g);
        propertyChange = refreshMap;
        firePropertyChanged ();
    }

    public void viewShiftLeft (ActionEvent e)
    {
        GroupRecord g = map.getGroupData ();
        Astrogation data = map.getMapData ();
        if (g != null && data != null)
        {
            HexID h = new HexID (g.getLocation ().toString ());
            //h.convertHextoID();
            h.x -= 1;
            g = data.getGroup (h, g.getType ());
            if (g != null)
                map.setGroup (g);
            propertyChange = refreshMap;
            firePropertyChanged ();
        }
    }

    public void viewShiftUp (ActionEvent e)
    {
        GroupRecord g = map.getGroupData ();
        Astrogation data = map.getMapData ();
        HexID h = new HexID (g.getLocation ().toString ());
        //h.convertHextoID();
        h.y -= 1;
        g = data.getGroup (h, g.getType ());
        if (g != null)
            map.setGroup (g);
        propertyChange = refreshMap;
        firePropertyChanged ();

    }

    public void viewShiftDown (ActionEvent e)
    {
        GroupRecord g = map.getGroupData ();
        Astrogation data = map.getMapData ();
        HexID h = new HexID (g.getLocation ().toString ());
        //h.convertHextoID();
        h.y += 1;
        g = data.getGroup (h, g.getType ());
        if (g != null)
            map.setGroup (g);
        propertyChange = refreshMap;
        firePropertyChanged ();

    }

    public void viewDisplay (ActionEvent e)
    {
        EditViewOptions dialogEditViewOptions = EditViewOptions.getInstance ();
        dialogEditViewOptions.setLocationRelativeTo (display);
        dialogEditViewOptions.setVisible (true);
        if (!dialogEditViewOptions.isUpdated ())
        {
            dialogEditViewOptions.setLevel (map.getLevel ());
            dialogEditViewOptions.setScale (map.getScale ());
            dialogEditViewOptions.setScope (map.getScope ());
        } else
        {
            propertyChange = 
                    new PropertyChangeEvent (this, MapMenu.VIEW_DISPLAY, null, 
                                             null);
            firePropertyChanged ();
            propertyChange = null;
        }
    }

    public void helpAbout (ActionEvent e)
    {
        JOptionPane.showMessageDialog (display, new Map_AboutBoxPanel1 (), 
                                       "About", JOptionPane.PLAIN_MESSAGE);
    }


    private void setAstrogrationData (Astrogation starData)
    {
        try
        {
            starData.loadGlobalReferences (MapPreferences.getInstance ().getExternalRefsFileName ());
        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog (display, ex.getMessage (), Resources.getString ("map.IOExceptionTitle"), 
                                           0);
        } catch (JiBXException ex)
        {
            JOptionPane.showMessageDialog (display, ex.getMessage (), 
                                           "Error: Load Global JiBX XML Reader Exception", 
                                           0);
        }
    }

}
