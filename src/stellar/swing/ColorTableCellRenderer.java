/*
 * $Id: ColorTableCellRenderer.java,v 1.4 2006/05/02 19:49:26 tjoneslo Exp $
 * Copyright 2005 Softstart Services, Inc. All rights reserved. 
 */
package stellar.swing;
import com.softstart.VectorImage.SquareVectorImage;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Class to render the <code>Color</code> class in a java table cell. 
 * The cell is rendered as a square filled with the color specified by <code>Color</code>,
 * and the text the RGB value of the color. 
 * 
 * @author Thomas Jones-Low
 * @version $Id: ColorTableCellRenderer.java,v 1.4 2006/05/02 19:49:26 tjoneslo Exp $
 */
public class ColorTableCellRenderer extends DefaultTableCellRenderer 
{
    SquareVectorImage icon = new SquareVectorImage (); // graphic to display the color
    // We need a place to store the color the JLabel should be returned 
    // to after its foreground and background colors have been set 
    // to the selection background color. 
    // These ivars will be made protected when their names are finalized. 
    private Color unselectedForeground; 
    private Color unselectedBackground; 


    /**
     * Default constructor.
     */
    public ColorTableCellRenderer()
    {
        this.setHorizontalAlignment(JLabel.LEFT);
        this.setHorizontalTextPosition(JLabel.RIGHT);
        icon.setFillShape(true);
    }
    
    /**
     * The table cell rendering setup code. This overrides the 
     * <code>DefultTableCellRenderer</code> to include the color square and 
     * the RGB text value for the Color class. The 
     * <code>DefaultTableCellRenderer</code> is an extension of the
     * <code>JLabel</code> class, allowing this to add both an <code>Icon</code>
     * and text to the same component. 
     * @param table  the <code>JTable</code>
     * @param value  the value to assign to the cell at
     *			<code>[row, column]</code>
     * @param isSelected true if cell is selected
     * @param hasFocus true if cell has focus
     * @param row  the row of the cell to render
     * @param column the column of the cell to render
     * @return the table cell renderer
     */
    public Component getTableCellRendererComponent 
        (JTable table, Object value, boolean isSelected, boolean hasFocus,
         int row, int column)
    {
        if (isSelected) {
           super.setForeground(table.getSelectionForeground());
           super.setBackground(table.getSelectionBackground());
        }
        else {
            super.setForeground((unselectedForeground != null) ? unselectedForeground 
                                                               : table.getForeground());
            super.setBackground((unselectedBackground != null) ? unselectedBackground 
                                                               : table.getBackground());
        }
        // render in the same font at the rest of the table
    	setFont(table.getFont());
        
        /*
         * Set the foreground and background colors of the cell if the cell
         * has focus.
         */
        if (hasFocus) {
            setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );
            if (table.isCellEditable(row, column)) {
                super.setForeground( UIManager.getColor("Table.focusCellForeground") );
                super.setBackground( UIManager.getColor("Table.focusCellBackground") );
            }
        } else {
            setBorder(noFocusBorder);
        }
        /*
         * Do the proper setup for the Color code. The icon needs to have 
         * the color set properly, and the text is based upon the Color RGB 
         * values.
         */
        if (value instanceof Color)
        {
            Color color = (Color)value;
            //int start = table.getFont().getSize() - 2;
            //AffineTransform at = AffineTransform.getScaleInstance(start/10, 
            //                    start/10);
            //icon.transform(at);
            icon.setColor (color);
            
            this.setIcon (icon);
            this.setText("0x" + Integer.toHexString(color.getRGB()).toUpperCase());
        }
        else
        {
            this.setIcon(null);
            this.setValue (value);
        }
    	return this;
    }

    /**
     * Overrides <code>JComponent.setForeground</code> to assign
     * the unselected-foreground color to the specified color.
     * 
     * @param c set the foreground color to this value
     */
    public void setForeground(Color c) {
        super.setForeground(c); 
        unselectedForeground = c; 
    }
    
    /**
     * Overrides <code>JComponent.setBackground</code> to assign
     * the unselected-background color to the specified color.
     *
     * @param c set the background color to this value
     */
    public void setBackground(Color c) {
        super.setBackground(c); 
        unselectedBackground = c; 
    }

    /**
     * Notification from the <code>UIManager</code> that the look and feel
     * [L&F] has changed.
     * Replaces the current UI object with the latest version from the 
     * <code>UIManager</code>.
     *
     * @see javax.swing.JComponent#updateUI
     */
    public void updateUI() {
        super.updateUI(); 
	setForeground(null);
	setBackground(null);
    }
    
}
