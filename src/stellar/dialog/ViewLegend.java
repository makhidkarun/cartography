package stellar.dialog;


import java.awt.*;

import java.awt.event.*;

import javax.swing.JDialog;

import stellar.map.HexLegend;

public class ViewLegend extends JDialog
{
    private BorderLayout borderLayout1 = new BorderLayout();
    private ButtonPanel buttonPanel = new ButtonPanel();
    private HexLegend legend;
    
    public ViewLegend()
    {
        this(null, "", false);
    }

    public ViewLegend(Frame parent, String title, boolean modal)
    {
        super(parent, title, modal);
        try
        {
            jbInit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private class OKActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            bOK_actionPerformed(e);
        }
        
    }
    
    private class CancelActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            bCancel_actionPerformed(e);
        }
    }
    

    private void jbInit() throws Exception
    {
        this.getContentPane().setLayout(borderLayout1);
        buttonPanel.addOKActionListener(new OKActionListener());
        buttonPanel.addCancelActionListener(new CancelActionListener());
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        
        this.setTitle("View Legend");
        this.pack();

    }
    private void bOK_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
    }

    private void bCancel_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
    }

}
