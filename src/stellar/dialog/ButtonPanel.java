package stellar.dialog;
import stellar.io.Resources;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
/**
 * Creates a panel which holds by default a OK and Cancel button. You may add
 * other buttons to the panel. BoxLayout is used to layout the buttons properly. 
 * 
 * You will need to create and add action handlers for the OK and Cancel 
 * buttons, none are provided for by default. 
 */
public class ButtonPanel extends JPanel 
{
    private JButton bOK = new JButton();
    private JButton bCancel = new JButton();
    private BoxLayout layout1 = new BoxLayout (this, BoxLayout.LINE_AXIS);
        
    public ButtonPanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void jbInit() throws Exception
    {
        this.setLayout(layout1);
        this.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        /* Create the initial two buttons (OK, Cancel) */
        bOK.setText(Resources.getString("ok.Name"));
        bOK.setMnemonic('O');
        bCancel.setText(Resources.getString("cancel.Name"));
        bCancel.setMnemonic('C');
        
        /* Add the buttons to the panel */
        this.add (Box.createHorizontalGlue(), null);
        this.add(bOK, null);
        this.add (Box.createRigidArea(new Dimension (6,0)),null);
        this.add(bCancel, null);
    }

    /**
     * Adds a new button to the panel. Buttons are added right to left, reverse
     * order from normally expected. 
     * @return The button just added
     * @param button The button to add 
     */
    public Component add (Component button)
    {
        if (button instanceof JButton)
        {
            this.add (Box.createRigidArea(new Dimension(6,0)),0);
            this.add(button,0);
        }
        return button;
    }
    /**
     * Add an action listener for the default OK button
     * @param a
     */
    public void addOKActionListener (ActionListener a) { bOK.addActionListener(a); }
    
    /**
     * Add an action listener for the default Cancel button
     * @param a
     */
    public void addCancelActionListener (ActionListener a) { bCancel.addActionListener(a); } 
}