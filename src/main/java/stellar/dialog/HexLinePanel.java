package stellar.dialog;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

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

import stellar.io.Resources;

public class HexLinePanel extends JPanel implements PropertyChangeListener
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private HexLine line;

    private JLabel lineName = new JLabel();
    private JRadioButton longOption = new JRadioButton();
    private JRadioButton shortOption = new JRadioButton();
    private JComboBox shortChoice1 = new JComboBox();
    private JComboBox shortChoice2 = new JComboBox();
    private JComboBox longChoice = new JComboBox();
    private JComboBox shortChoice3 = new JComboBox();
    
    private ButtonGroup optionGroup = new ButtonGroup();

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
        
        FormLayout formLayout1 = new FormLayout("right:pref, $rgap, 4*(pref, $rgap)",
                                                 "pref, $rgap, pref");
        PanelBuilder builder = new PanelBuilder (formLayout1, this);
        CellConstraints cc = new CellConstraints();
        builder.setBorder(Borders.DLU4_BORDER);

        lineName.setText(Resources.getString("eo.hl.line") + " " + 
                         String.valueOf(line.getLineNumber()));

        longOption.setText(Resources.getString("eo.hl.long"));
        shortOption.setText(Resources.getString("eo.hl.short"));

        optionGroup.add(longOption);
        optionGroup.add(shortOption);

        if (line.isLongSelected())
            longOption.setSelected(true);
        else
            shortOption.setSelected(true);
        
        longChoice.setModel(new DefaultComboBoxModel (LongLineList.values()));
        longChoice.setSelectedItem(line.getLongItem());

        shortChoice1.setModel(new DefaultComboBoxModel (ShortLineList.values()));
        shortChoice2.setModel(new DefaultComboBoxModel (ShortLineList.values()));
        shortChoice3.setModel(new DefaultComboBoxModel (ShortLineList.values()));

        shortChoice1.setSelectedItem(line.getShortItem1());
        shortChoice2.setSelectedItem(line.getShortItem2());
        shortChoice3.setSelectedItem(line.getShortItem3());
        
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
        shortOption.addActionListener(new ActionListener()
              {
                  public void actionPerformed(ActionEvent e)
                  {
                      line.setLongSelected(longOption.isSelected());
                  }
              });
        
        builder.add (lineName, cc.xywh (1, 1, 1, 3));
        builder.add (longOption, cc.xy(3,1));
        builder.add (shortOption, cc.xy(3,3));
        builder.add (longChoice, cc.xyw(5,1, 3));
        builder.add (shortChoice1, cc.xy(5,3));
        builder.add (shortChoice2, cc.xy(7,3));
        if (line.isThreeShortItems())
            builder.add (shortChoice3, cc.xy(9, 3));
        this.setSize(this.getPreferredSize());        
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

    public LongLineList getLongItem() { return (LongLineList)longChoice.getSelectedItem(); }
    public ShortLineList getShortItem1() { return (ShortLineList)shortChoice1.getSelectedItem(); }
    public ShortLineList getShortItem2() { return (ShortLineList)shortChoice2.getSelectedItem(); }
    public ShortLineList getShortItem3() { return (ShortLineList)shortChoice3.getSelectedItem(); }
    
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