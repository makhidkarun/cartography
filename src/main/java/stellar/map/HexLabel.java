package stellar.map;

import stellar.MapMenu;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

/**
 * HexLabel is the Icon Label used to hold the either the hex based map as an 
 * Icon (image) or the square based map as an Icon (image). This class is designed
 * to handle the mouse scrolling of the image when placed in a <code>JScrollPane.</code>
 * 
 * HexLabel also handles the keyboard scrolling through the default 
 * <code>getScrollableUnitIncrement</code> and <code>getScrollableBlockIncrement</code>.
 * The up/down/left/right keys trigger the former, the pgup/pgdn (for up /down )
 * and ctrl-pgup/ctrl-pgdn (for left/right) trigger the latter. 
 */
public class HexLabel extends JLabel implements Scrollable, MouseMotionListener, 
        PropertyChangeListener
{
    private int maxUnitIncrement = 1;
    //private MapCommands commands;
    //private AppDefaults defaults = new AppDefaults();
    
    public HexLabel(Icon map)
    {
        this.setIcon(map);
        //Let the user scroll by dragging to outside the window.
        setAutoscrolls(true); //enable synthetic drag events
        addMouseMotionListener(this); //handle mouse drags
        this.setDoubleBuffered(false);
        //this.setInputMap(JComponent.WHEN_FOCUSED, new InputMap());
        this.getInputMap().put (KeyStroke.getKeyStroke ("UP"), "viewUp");
    }
    //Methods required by the MouseMotionListener interface:
    public void mouseMoved(MouseEvent e) { }
    public void mouseDragged(MouseEvent e) {
        //The user is dragging us, so scroll!
        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
        scrollRectToVisible(r);
        
    }

    // Method required by the Scrollable interface
    public Dimension getPreferredSize() 
    { 
        return new Dimension (this.getIcon().getIconWidth(), this.getIcon().getIconHeight());
    }
    /**
     * Returns the preferred size of the viewport for a view component. For 
     * example the preferredSize of a JList component is the size required to 
     * accommodate all of the cells in its list however the value of 
     * preferredScrollableViewportSize is the size required for 
     * JList.getVisibleRowCount() rows. A component without any properties that 
     * would effect the viewport size should just return getPreferredSize() here. 
     * 
     * @return The preferredSize of a JViewport whose view is this Scrollable.
     * @see javax.swing.Scrollable
     */
    public Dimension getPreferredScrollableViewportSize() { return super.getPreferredSize(); }

    /**
     * Return true if a viewport should always force the width of this 
     * Scrollable to match the width of the viewport. For example a normal 
     * text view that supported line wrapping would return true here, since it 
     * would be undesirable for wrapped lines to disappear beyond the right 
     * edge of the viewport. Note that returning true for a Scrollable whose 
     * ancestor is a JScrollPane effectively disables horizontal scrolling. 
     * Scrolling containers, like JViewport, will use this method each time 
     * they are validated.
     * @return True if a viewport should force the Scrollables width to match 
     * its own.
     * @see javax.swing.Scrollable

     */
    public boolean getScrollableTracksViewportWidth() { return false; }
    
    /**
     * Return true if a viewport should always force the height of this 
     * Scrollable to match the height of the viewport. For example a columnar 
     * text view that flowed text in left to right columns could effectively 
     * disable vertical scrolling by returning true here. Scrolling containers, 
     * like JViewport, will use this method each time they are validated. 
     * @return True if a viewport should force the Scrollables height to 
     * match its own.
     * @see javax.swing.Scrollable
     */
    public boolean getScrollableTracksViewportHeight() { return false; }

    /**
     * Components that display logical rows or columns should compute the scroll 
     * increment that will completely expose one new row or column, depending on 
     * the value of orientation. Ideally, components should handle a partially 
     * exposed row or column by returning the distance required to completely 
     * expose the item. Scrolling containers, like JScrollPane, will use this 
     * method each time the user requests a unit scroll. 
     * @return The "unit" increment for scrolling in the specified direction. 
     * This value should always be positive.
     * @param visibleRect The view area visible in the viewport
     * @param orientation Either SwingConstants.VERTICAL or SwingConstants.HORIZONTAL
     * @param direction Less than zero to scroll up/left, greater than zero to down/right.
     * @see javax.swing.Scrollable
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect,
                                          int orientation,
                                          int direction) {
        //Get the current position.
        int currentPosition = 0;
        // Scroll left / right
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
            MapIcon map = (MapIcon)this.getIcon();
            Dimension d = map.getMapSize();
            maxUnitIncrement = (int)( map.getIconWidth() / d.getWidth());
        // Scroll up/down            
        } else {
            currentPosition = visibleRect.y;
            MapIcon map = (MapIcon)this.getIcon();
            Dimension d = map.getMapSize();
            maxUnitIncrement = (int)(map.getIconHeight() / d.getHeight());
        }

        //Return the number of pixels between currentPosition
        //and the nearest tick mark in the indicated direction.
        if (direction < 0) {
            int newPosition = currentPosition -
                             (currentPosition / maxUnitIncrement)
                              * maxUnitIncrement;
            return (newPosition == 0) ? maxUnitIncrement : newPosition;
        } else {
            return ((currentPosition / maxUnitIncrement) + 1)
                   * maxUnitIncrement
                   - currentPosition;
        }
/*
            Rectangle r = this.getVisibleRect();
            MapIcon map = (MapIcon)this.getIcon();
            Dimension d = map.getMapSize();
            int height = map.getIconHeight();
            r.y -= (height/d.getHeight());
            if (r.y < 0) r.y = 0;
            this.scrollRectToVisible(r);
*/
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
                                           int orientation,
                                           int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }

    public void setMaxUnitIncrement(int pixels) { maxUnitIncrement = pixels; }

    public void setMap (Icon map) { this.setIcon(map); iconChanged(); }
    
    private void iconChanged ()
    {
        this.setPreferredSize(this.getPreferredSize());
        this.revalidate();
    }

    /**
     * Force the graphic image to be redrawn. Usually called when the underlying
     * data has changed. 
     */
    public void redrawMap () { this.invalidate(); }


    /**
     * Handles the property change event, usually a request to refresh and 
     * redraw the map
     * @param e The property change event. 
     */
    public void propertyChange (PropertyChangeEvent e)
    {
        if (e.getPropertyName() == MapMenu.VIEW_REFRESH) { iconChanged(); }   
    }
}