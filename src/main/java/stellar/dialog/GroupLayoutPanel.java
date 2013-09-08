package stellar.dialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

public class GroupLayoutPanel extends JPanel 
{
    private JTextField groupAName = new JTextField();
    private JButton bGroupA = new JButton();
    private JTextField groupBName = new JTextField();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JButton bGroupB = new JButton();
    private JTextField groupCName = new JTextField();
    private JButton jButton1 = new JButton();
    private JTextField groupDName = new JTextField();
    private JButton jButton2 = new JButton();

    public GroupLayoutPanel()
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
        this.setLayout(gridBagLayout1);
        this.setSize(new Dimension(440, 300));
        groupAName.setText("Subsector A");
        bGroupA.setText("Zoom");
        bGroupA.setPreferredSize(new Dimension(71, 25));
        groupBName.setText("Subsector B");
        bGroupB.setText("Zoom");
        groupCName.setText("Subsector C");
        jButton1.setText("Zoom");
        groupDName.setText("Subsector D");
        jButton2.setText("Zoom");
        this.add(groupAName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        this.add(bGroupA, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 8));
        this.add(groupBName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        this.add(bGroupB, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(groupCName, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        this.add(jButton1, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(groupDName, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        this.add(jButton2, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }
}