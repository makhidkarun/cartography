/*
 * $Id: MapLabel.java,v 1.8 2008/12/15 16:13:05 tjoneslo Exp $
 * Copyright 2006 by Softstart Services Inc. 
 */

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
 * MapLabel is <code>JLabel</code> used to hold an <code>Icon</code>. 
 * In particular, a image of a large map which is placed in a <code>JScrollPane</code>.
 * In addition to holding the image, the class handles the mouse scrolling and 
 * keyboard scrolling. MapLabel also handles changes to the underlying map. 
 * 
 * MapLabel handles the keyboard scrolling through the default 
 * <code>getScrollableUnitIncrement</code> and <code>getScrollableBlockIncrement</code>.
 * The up/down/left/right keys trigger the former, the pgup/pgdn (for up /down )
 * and ctrl-pgup/ctrl-pgdn (for left/right) trigger the latter. 
 * 
 * @version $Revision: 1.8 $
 * @author $Author: tjoneslo $
 */
public class MapLabel extends JLabel implements Scrollable, MouseMotionListener, 
        PropertyChangeListener
{
    private int maxUnitIncrement = 1; // number of pixels to scroll 
    
    /**
     * Default constructor. Since MapLabel is designed to have an Icon to display, 
     * we only allow creating one with a proper icon. 
     * 
     * @param map the initial <code>Icon</code> to display. 
     */
    public MapLabel(Icon map)
    {
        this.setIcon(map);
        //Let the user scroll by dragging to outside the window.
        setAutoscrolls(true); //enable synthetic drag events
        addMouseMotionListener(this); //handle mouse drags
        this.setDoubleBuffered(false);

        // This triggers the acceptance of the key stroke handler. Internally
        // it generates the rest of the input map and action map. Though 
        // there should be a simpiler way of doing this. 
        this.getInputMap().put (KeyStroke.getKeyStroke ("UP"), "viewUp");
    }
    //Methods required by the MouseMotionListener interface:
    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed. This does nothing for simply moving
     * the mouse over the map.
     */
    public void mouseMoved(MouseEvent e) { }
    
    /**
     * Invoked when a mouse button is pressed on a component and then 
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be 
     * delivered to the component where the drag originated until the 
     * mouse button is released (regardless of whether the mouse position 
     * is within the bounds of the component).
     * <p> 
     * Due to platform-dependent Drag&Drop implementations, 
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native 
     * Drag&Drop operation.  
     * <p>
     * Causes the map to scroll in the direction of the drag. 
     */
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
    }

    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one block
     * of rows or columns, depending on the value of orientation. 
     * <p>
     * Scrolling containers, like JScrollPane, will use this method
     * each time the user requests a block scroll.
     * 
     * @param visibleRect The view area visible within the viewport
     * @param orientation Either SwingConstants.VERTICAL or SwingConstants.HORIZONTAL.
     * @param direction Less than zero to scroll up/left, greater than zero for down/right.
     * @return The "block" increment for scrolling in the specified direction.
     *         This value should always be positive.
     * @see JScrollBar#setBlockIncrement
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect,
                                           int orientation,
                                           int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }

    
    //public void setMaxUnitIncrement(int pixels) { maxUnitIncrement = pixels; }

    /**
     * Sets a new map Icon to display. Forces the display to refresh. 
     * @param map new map Icon to display. 
     */
    public void setMap (Icon map) { this.setIcon(map); iconChanged(); }
    
    /**
     * Sets the notification that the underlying Icon has changed and the Swing
     * code needs to redraw the image on the screen.
     */
    private void iconChanged ()
    {
        this.setPreferredSize(this.getPreferredSize());
        //this.invalidate();
        //this.revalidate();
        this.repaint();
    }

    /**
     * Forces the graphic image to be redrawn. Usually called when the underlying
     * data has changed. 
     */
    public void redrawMap () 
    { 
        ((MapIcon)(this.getIcon())).redrawMap();
        this.iconChanged(); }


    /**
     * Handles the property change event, usually a request to refresh and 
     * redraw the map
     * @param e The property change event. 
     */
    public void propertyChange (PropertyChangeEvent e)
    {
        redrawMap();
    }
}
