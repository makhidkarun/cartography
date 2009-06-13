package stellar;

import stellar.dialog.EditOptions;

import java.awt.Frame;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.UIManager;

import java.lang.System;

public class Cartrographer
{
    public Cartrographer ()
    {
        MapPreferences prefs = MapPreferences.getInstance();
        Frame frame = new Map ();
        //EditOptions options = EditOptions.getInstance ();

        Dimension frameSize = 
            new Dimension (prefs.getAppWidth (), prefs.getAppHeight ());
        Point location = new Point (prefs.getAppX (), prefs.getAppY ());
        Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
        if (location.x < 0 || location.x > screenSize.width)
        {
            location.x = (screenSize.width - frameSize.width) / 2;
        }
        if (location.y < 0 || location.y > screenSize.height)
        {
            location.y = (screenSize.height - frameSize.height) / 2;
        }
        if (location.x + frameSize.width > screenSize.width)
        {
            frameSize.width = screenSize.width - location.x;
        }
        if (location.y + frameSize.height > screenSize.height)
        {
            frameSize.height = screenSize.height - location.y;
        }
        frame.setLocation (location);
        frame.addWindowListener (new WindowAdapter ()
                {
                    public void windowClosing (WindowEvent e)
                    {
                        System.exit (0);
                    }
                });
        frame.pack ();
        frame.setSize (frameSize);
        frame.setVisible (true);
    }

    public static void main (String[] args)
    {
        try
        {
            String MetalClassName = "javax.swing.plaf.metal.MetalLookAndFeel";
            String MotifClassName = 
                "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            String LFClassName = UIManager.getSystemLookAndFeelClassName ();
            UIManager.setLookAndFeel (LFClassName);
            //UIManager.setLookAndFeel(MetalClassName);
        } catch (Exception e)
        {
            e.printStackTrace ();
        }

        new Cartrographer ();
    }
}
