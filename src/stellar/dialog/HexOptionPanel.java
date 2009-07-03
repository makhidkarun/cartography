package stellar.dialog;

import com.jgoodies.forms.builder.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import stellar.data.Astrogation;

import stellar.map.HexIcons;
import stellar.map.LocationIDType;


import stellar.map.MapLabel;
import stellar.map.MapScale;
import stellar.map.MapScope;
import stellar.map.layout.HexLayout;
import stellar.map.layout.HexOptions;
import stellar.map.layout.HexOptionsProperties;

import java.awt.*;

import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.*;

import javax.swing.*;

import say.swing.*;

import stellar.io.Resources;

/**
 * HexOptionPanel displays the hex options for the user to change as needed.
 * This is the UI component to the {@link HexOptions} data component. Usually this
 * is used as part of the EditOptions dialog (five copies) for for the five map
 * scale levels.
 * @see EditOptions
 * @see HexOptions
 * @author Thomas Jones-Low
 * @version $Revision: 1.12 $
 */
public class HexOptionPanel extends JPanel implements PropertyChangeListener
{
    private HexLayout layout;
    private MapLabel hexLabel;
    private HexIcons hexMap = new HexIcons(true);
    private JPanel mapPane = new JPanel();
    private BorderLayout borderLayout = new BorderLayout();

    private JCheckBox optJumpRoutes = new JCheckBox();
    private JCheckBox optShowBorders = new JCheckBox();
    private JCheckBox optTravelZones = new JCheckBox();

    private JComboBox hexFillChoice = new JComboBox();
    
    private JButton bBorderColorChooser = new JButton();
    private JButton bBackgroundColorChooser = new JButton();
    private JButton bFontChooser = new JButton();

    private Color borderColor = Color.RED;
    private Color backgroundColor = Color.WHITE;
    private Color foregroundColor = Color.BLACK;

    public HexOptionPanel(HexLayout layout)
    {
        this.layout = layout;
        layout.getOptions().addPropertyChangeListener(this);
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        layout.addPropertyChangeListener(hexLabel);        
    }
    
    private void jbInit() throws Exception
    {
        HexOptions options = layout.getOptions();
        
        FormLayout formLayout1 = new FormLayout("pref, $rgap, fill:p:grow, $rgap, pref",
                                                "7*(pref, $rgap)");
        
        PanelBuilder builder = new PanelBuilder (formLayout1, this);
        CellConstraints cc = new CellConstraints();
        builder.setBorder(Borders.DLU4_BORDER);
        
        hexMap.setLayout(layout);
        hexLabel = new MapLabel (hexMap);
        
        mapPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        mapPane.setLayout (borderLayout);
        mapPane.add(hexLabel, BorderLayout.CENTER);
        mapPane.setPreferredSize(new Dimension(100, 126));

        builder.add (mapPane, cc.xywh(1,1,5,1,CellConstraints.CENTER, CellConstraints.FILL));
        
        bFontChooser.setText("...");
        bFontChooser.addActionListener( new ActionListener ()
                                        {
                                            public void actionPerformed (ActionEvent e)
                                            {
                                                bFontChooser_actionPerformed(e);
                                            }
                                        });
        builder.addLabel(Resources.getString("eo.hop.font"), cc.xyw(1,3,3)).setFocusable(false);
        setFontLabelTitle((JLabel)this.getComponent(1));
        
        builder.add (bFontChooser, cc.xy(5,3));

        bBackgroundColorChooser.setText("...");
        bBackgroundColorChooser.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bBackgroundColorChooser_actionPerformed(e);
                }
            }); 
        builder.addLabel (Resources.getString("eo.hop.backColor"), cc.xyw(1,5,3)).setFocusable(false);
        builder.add (bBackgroundColorChooser, cc.xy(5,5));

        optShowBorders.setText(Resources.getString("eo.hop.showBorder"));
        optShowBorders.addActionListener(new ActionListener()
                                         {
                                             public void actionPerformed (ActionEvent e)
                                             {
                                                 layout.getOptions().setShowBorders(optShowBorders.isSelected());
                                             }
                                         });
        optShowBorders.setSelected(options.isShowBorders());
        
        bBorderColorChooser.setText("...");
        bBorderColorChooser.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    bBorderColorChooser_actionPerformed(e);
                }
            });

        builder.add (optShowBorders, cc.xyw(1,7,3));
        builder.add (bBorderColorChooser, cc.xy(5,7));

        optJumpRoutes.setText(Resources.getString("eo.hop.showRoutes"));
        optJumpRoutes.setSelected(options.isShowJumpRoutes());
        optJumpRoutes.addActionListener( new ActionListener()
                                         {
                                             public void actionPerformed (ActionEvent e)
                                             {
                                                 layout.getOptions().setShowJumpRoutes(optJumpRoutes.isSelected());
                                             }
                                         });
        builder.add (optJumpRoutes, cc.xyw(1,9,5));

        optTravelZones.setText(Resources.getString("eo.hop.showZones"));
        optTravelZones.setSelected(options.isShowTravelZones());
        optTravelZones.addActionListener(new ActionListener()
                                         {
                                             public void actionPerformed (ActionEvent e)
                                             {
                                                 layout.getOptions().setShowTravelZones(optTravelZones.isSelected());
                                             }
                                         });
        builder.add (optTravelZones, cc.xyw (1,11,5));

        hexFillChoice.setModel(new DefaultComboBoxModel (LocationIDType.values()));
        hexFillChoice.setSelectedItem(options.getLocationID());
        hexFillChoice.setToolTipText(Resources.getString("eo.hop.showIDs"));
        hexFillChoice.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent e)
                                            {
                                                layout.getOptions().setLocationID((LocationIDType)hexFillChoice.getSelectedItem());
                                            }
                                        });
        builder.addLabel(Resources.getString("eo.hop.setIDs"), cc.xy(1,13)).setFocusable(false);
        builder.add (hexFillChoice, cc.xyw(3,13,3));
        //this.setSize(this.getPreferredSize());
    }
    
    public void setMapSize (int height, int width) { hexMap.setSize(height, width); }
    public void setMapScale (MapScale scale) { hexMap.setScale(scale); }
    public void redrawMap () { hexLabel.redrawMap(); }
    public void setMapData (Astrogation data) { hexMap.setMapData(data); }
    public void setMapScope (MapScope scope)  {hexMap.setScope(scope);}
    public Astrogation getMapData () { return hexMap.getMapData(); } 
 
    public void propertyChange (PropertyChangeEvent e)
    {
        HexOptions options = layout.getOptions();
        if (e.getPropertyName() == HexOptionsProperties.LOCATION_ID.toString())
        {
            hexFillChoice.setSelectedItem(options.getLocationID());
        }
        else if (e.getPropertyName() == HexOptionsProperties.SHOW_BORDERS.toString())
        {
            optShowBorders.setSelected(options.isShowBorders());
        }
        else if (e.getPropertyName() == HexOptionsProperties.SHOW_JUMP_ROUTES.toString())
        {
            optJumpRoutes.setSelected(options.isShowJumpRoutes());
        }
        else if (e.getPropertyName() == HexOptionsProperties.SHOW_TRAVEL_ZONES.toString())
        {
            optTravelZones.setSelected(options.isShowTravelZones());
        }
        else if (e.getPropertyName() == HexOptionsProperties.FONT_NAME.toString())
        {
            JLabel label = (JLabel)this.getComponent(1);
            setFontLabelTitle((JLabel)this.getComponent(1));
        }
        hexLabel.redrawMap();
    }
    
    private void setFontLabelTitle (JLabel label)
    {
        Font display = layout.getOptions().getDisplayFont();
        if (display == null)
        {
            label.setText (Resources.getString("eo.hop.font") + "Unknown");
        }
        else
        {
            label.setText (Resources.getString("eo.hop.font") + display.getName() + " ["
                           + display.getSize() + "]");
        }
        if (label.getText().length() > 16)
        {
            label.setText(label.getText().substring(0, 16) + "...");
        }
    }
    
    private void bBorderColorChooser_actionPerformed(ActionEvent e)
    {
        borderColor = JColorChooser.showDialog(this, Resources.getString("eo.hop.allegianceColorTitle"), borderColor);
        if (borderColor != null)
        {
            layout.getOptions().setBorderColor(borderColor);
            bBorderColorChooser.setBackground(borderColor);
        }
    }

    private void bBackgroundColorChooser_actionPerformed(ActionEvent e)
    {
        Color color;
        color = JColorChooser.showDialog(this, Resources.getString("eo.hop.mapColorTitle"), backgroundColor);
        if (color != null)
        {
            layout.getOptions().setBackgroundColor(color);
            bBackgroundColorChooser.setBackground(color);
            bBackgroundColorChooser.setForeground(layout.getOptions().getForegroundColor());
            
        }
    }

    private void bFontChooser_actionPerformed(ActionEvent e)
    {
        JFontChooser fontDialog = new JFontChooser ();
        fontDialog.setSelectedFont(layout.getOptions().getDisplayFont());
        int result = fontDialog.showDialog(this);
        if (result == JFontChooser.OK_OPTION)
        {
            layout.getOptions().setDisplayFont(fontDialog.getSelectedFont());
        }
    }
}
