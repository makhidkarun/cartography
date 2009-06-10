package stellar.dialog;

import stellar.map.layout.HexLine;

import stellar.map.layout.HexLineProperties;

import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;

import stellar.map.layout.LongLineList;
import stellar.map.layout.ShortLineList;

import java.awt.event.ActionEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HexLinePanel extends JPanel implements PropertyChangeListener
{
    private HexLine line;

    private JLabel lineName = new JLabel();
    private JRadioButton longOption = new JRadioButton();
    private JRadioButton shortOption = new JRadioButton();
    private JComboBox shortChoice1 = new JComboBox();
    private JComboBox shortChoice2 = new JComboBox();
    private JComboBox longChoice = new JComboBox();
    private JComboBox shortChoice3 = new JComboBox();
    
    
    private GridBagLayout gb = new GridBagLayout();
    private GridBagConstraints gbc = new GridBagConstraints();
    private ButtonGroup optionGroup = new ButtonGroup();
    
    //private int lineNumber;
    private boolean twoShort = false;

    public HexLinePanel ()
    {
        line = new HexLine();
        line.addPropertyChangeListener(this);
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public HexLinePanel(HexLine line)
    {
        this.line = line;
        line.addPropertyChangeListener(this);
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
        this.setLayout(gb);
        //this.setSize(new Dimension(510, 55));
        //this.setBounds(new Rectangle(10, 10, 510, 55));
        //this.setBounds(new Rectangle(10, 10, 435, 50));
        lineName.setText("Line  ");

        longOption.setText("Long");
        shortOption.setText("Short");

        //longChoice.i
        //longChoice.setActionCommand("longOptionChanged");
        //shortChoice1.setActionCommand("short1OptionChanged");
        //shortChoice2.setActionCommand("short2OptionChanged");
        //shortChoice3.setActionCommand("short3OptionChanged");

        optionGroup.add(longOption);
        optionGroup.add(shortOption);


        longOption.setSelected(true);
        longChoice.setModel(new DefaultComboBoxModel (LongLineList.values()));
        longChoice.setSelectedIndex(0);

        shortChoice1.setModel(new DefaultComboBoxModel (ShortLineList.values()));
        shortChoice2.setModel(new DefaultComboBoxModel (ShortLineList.values()));
        shortChoice3.setModel(new DefaultComboBoxModel (ShortLineList.values()));

        shortChoice1.setSelectedIndex(0);
        shortChoice2.setSelectedIndex(0);
        shortChoice3.setSelectedIndex(0);
        
        longChoice.addActionListener(new ActionListener ()
            {
                public void actionPerformed (ActionEvent e)
                {
                    line.setLongItem((LongLineList)(longChoice.getSelectedItem()));
                }
            });
        
        shortChoice1.addActionListener(new ActionListener ()
           {
               public void actionPerformed (ActionEvent e)
               {
                   line.setShortItem1((ShortLineList)shortChoice1.getSelectedItem());
               }
           });

        shortChoice2.addActionListener(new ActionListener ()
           {
               public void actionPerformed (ActionEvent e)
               {
                   line.setShortItem2((ShortLineList)shortChoice2.getSelectedItem());
               }
           });
        
        shortChoice3.addActionListener (new ActionListener ()
            {
                public void actionPerformed (ActionEvent e)
                {
                    line.setShortItem3((ShortLineList)shortChoice3.getSelectedItem());
                }
            });
        
        longOption.addActionListener(new ActionListener ()
             {
                 public void actionPerformed (ActionEvent e)
                 {
                     line.setLongSelected(longOption.isSelected());
                 }
             });
        this.add(lineName, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 4, 0, 4), 0, 0));
        this.add(longOption, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
        this.add(longChoice, new GridBagConstraints(2, 0, GridBagConstraints.RELATIVE, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
        this.add(shortOption, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.add(shortChoice1, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
        this.add(shortChoice2, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
        this.add(shortChoice3, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 5), 0, 0));
        this.setSize(this.getPreferredSize());        
    }
    
    public void addActionListener (ActionListener a)
    {
        longOption.addActionListener(a);
        shortOption.addActionListener(a);
        longChoice.addActionListener(a);
        shortChoice1.addActionListener(a);
        shortChoice2.addActionListener(a);
        shortChoice3.addActionListener(a);
    }

    public void setLineNumber (int lineNumber)
    {
        line.setLineNumber(lineNumber);
        lineName.setText ("Line " + String.valueOf(lineNumber));
        //this.lineNumber = lineNumber;
    }

    public void setTwoShortItems ()
    {
        this.twoShort = true;
        this.remove(shortChoice3);
    }

    public void setLongOption (boolean setOption)
    {
        if (setOption) longOption.setSelected(true); else shortOption.setSelected(true);
    }
    
    public void setLongIndex (int id) { longChoice.setSelectedIndex(id); }
    public void setShort1Index (int id) { shortChoice1.setSelectedIndex(id); }
    public void setShort2Index (int id) { shortChoice2.setSelectedIndex(id); } 
    public void setShort3Index (int id) { shortChoice3.setSelectedIndex(id); } 
    
    public boolean isLongItem() { return longOption.isSelected(); }
    public boolean isShortItem() { return shortOption.isSelected(); }
    public boolean isTwoShortItems() { return shortOption.isSelected() && twoShort; } 

    public LongLineList getLongItem() { return (LongLineList)longChoice.getSelectedItem(); }
    //public String getLongItem() { return (String)longChoice.getSelectedItem(); }
    public ShortLineList getShortItem1() { return (ShortLineList)shortChoice1.getSelectedItem(); }
    public ShortLineList getShortItem2() { return (ShortLineList)shortChoice2.getSelectedItem(); }
    public ShortLineList getShortItem3() { return (ShortLineList)shortChoice3.getSelectedItem(); }
    //public LongLineList getLongIndex() { return longChoice.getSelectedIndex(); }
    //public int getShort1Index() { return shortChoice1.getSelectedIndex(); } 
    //public int getShort2Index() { return shortChoice2.getSelectedIndex(); } 
    //public int getShort3Index() { return shortChoice3.getSelectedIndex(); } 
    
    public void propertyChange (PropertyChangeEvent e)
    {
        if (e.getPropertyName() == HexLineProperties.LONG_OPTION.toString())
        {
            longChoice.setSelectedItem(line.getLongItem());
        }
        else if (e.getPropertyName() == HexLineProperties.SHORT_OPTION1.toString())
        {
            shortChoice1.setSelectedItem(line.getShortItem1());
        }
        else if (e.getPropertyName() == HexLineProperties.SHORT_OPTION2.toString())
        {
            shortChoice2.setSelectedItem(line.getShortItem2());
        }
        else if (e.getPropertyName() == HexLineProperties.SHORT_OPTION3.toString())
        {
            shortChoice3.setSelectedItem(line.getShortItem3());
        }
        return;
    }
}