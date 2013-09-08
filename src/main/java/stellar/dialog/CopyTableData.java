package stellar.dialog;
import stellar.io.Resources;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.BorderFactory;
import java.awt.GridLayout;

public class CopyTableData extends JDialog 
{
    private ButtonPanel buttonPanel = new ButtonPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel jPanel1 = new JPanel();

    private ButtonGroup buttonGroup1 = new ButtonGroup();
    private JRadioButton jRadioButton1 = new JRadioButton();
    private JRadioButton jRadioButton2 = new JRadioButton();

    private ButtonGroup buttonGroup2 = new ButtonGroup();
    private JRadioButton jRadioButton3 = new JRadioButton();
    private JRadioButton jRadioButton4 = new JRadioButton();

    private JPanel jPanel2 = new JPanel();
    private JPanel jPanel3 = new JPanel();

    private BoxLayout boxLayout2;
    private BoxLayout boxLayout3;
    
    
    boolean copyOK = false;
    private GridLayout gridLayout1 = new GridLayout();

    public CopyTableData()
    {
        this(null, "", false);
    }

    /**
     * 
     * @param parent
     * @param title
     * @param modal
     */
    public CopyTableData(Frame parent, String title, boolean modal)
    {
        super(parent, title, modal);
        try
        {
            jbInit();
        }
        catch(Exception e)
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
        copyOK = false;
        // Setup the buttons and button panel
        buttonPanel.addOKActionListener(new OKActionListener());
        buttonPanel.addCancelActionListener(new CancelActionListener());

        boxLayout2 = new BoxLayout (jPanel2, BoxLayout.PAGE_AXIS);
        boxLayout3 = new BoxLayout (jPanel3, BoxLayout.PAGE_AXIS);

        jRadioButton1.setText(Resources.getString("ctd.jRadioButton1"));
        jRadioButton2.setText(Resources.getString("ctd.jRadioButton2"));
        jRadioButton2.setEnabled(false);
        buttonGroup1.add (jRadioButton1);
        buttonGroup1.add (jRadioButton2);
        jRadioButton1.setSelected(true);
        jRadioButton3.setText(Resources.getString("ctd.jRadioButton3"));
        jRadioButton4.setText(Resources.getString("ctd.jRadioButton4"));

        buttonGroup2.add (jRadioButton3);
        buttonGroup2.add (jRadioButton4);
        jRadioButton3.setSelected(true);

        jPanel2.setLayout(boxLayout2);
        jPanel2.setBorder(BorderFactory.createTitledBorder(Resources.getString("ctd.border1")));

        jPanel2.add(jRadioButton1, null);
        jPanel2.add(jRadioButton2, null);
        jPanel2.add(Box.createVerticalGlue(), null);
        
        jPanel3.setLayout(boxLayout3);
        jPanel3.setBorder(BorderFactory.createTitledBorder(Resources.getString("ctd.border2")));
        jPanel3.add(jRadioButton3, null);
        jPanel3.add(jRadioButton4, null);
        jPanel3.add(Box.createVerticalGlue(), null);
        
        gridLayout1.setHgap(6);
        jPanel1.setLayout(gridLayout1);
        jPanel1.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        jPanel1.add (jPanel2, null);
        jPanel1.add (jPanel3, null);

        this.setTitle(Resources.getString("ctd.Title"));
        this.getContentPane().setLayout(borderLayout1);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        //this.setSize(this.getPreferredSize());
        this.pack();
    }
    private void bOK_actionPerformed(ActionEvent e)
    {
        copyOK = true;
        this.setVisible(false);
    }

    private void bCancel_actionPerformed(ActionEvent e)
    {
        copyOK = false;
        this.setVisible(false);
    }

    public boolean getCopyOK () { return copyOK; }
    public boolean getCopyEntireTable() { return jRadioButton1.isSelected(); }
    public boolean getReplaceLocalData() { return jRadioButton3.isSelected(); }
}
