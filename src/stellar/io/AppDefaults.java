/*
 * $Id: AppDefaults.java,v 1.4 2006/05/02 19:49:20 tjoneslo Exp $
 * Copyright 2005 Softstart Services, Inc. All rights reserved. 
 */

package stellar.io;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.UIDefaults;

/**
 * AppDefaults extends the (@link javax.swing.UIDefaults) to add keystroke 
 * functionality to the UIDefaults. 
 * 
 * This code has been borrowed from <href url="http://www.java.net">Java.net</href>,
 * specifically <href url="http://today.java.net/pub/a/today/2005/01/31/controlGUI.html">Asserting Control Over the GUI: Commands, Defaults, and Resource Bundles</href>
 * by Hans Muller. Neither the code nor the article has a copyright notice 
 * attached, which means the code it copyrighted by Hans Muller and is not
 * copyable. 
 * 
 * @version $Id: AppDefaults.java,v 1.4 2006/05/02 19:49:20 tjoneslo Exp $
 */
public class AppDefaults extends UIDefaults {

    /**
     * Gets a keystroke from the AcceleratorKey as read from the properties. The
     * properties lists the AcceleratorKey (for example "control 0"), this
     * code converts that to use in 
     * {@link javax.swing.JMenuItem#setAccelerator setAccelerator}.
     * @see javax.swing.KeyStroke 
     * @return KeyStroke of the input string
     * @param key Description of the accelerator key
     */
    public KeyStroke getKeyStroke(String key) {
	return KeyStroke.getKeyStroke(getString(key));
    }

    /**
     * Gets the integer keycode from the MnemonicKey as read from the properties.
     * The properties listthe MnemonicKey (for example "0"), this code converts
     * that to use in (@link javax.swing.AbstractButton#setMnemonic).
     * @return Integer of the mnemonic keystroke
     * @param key String of the single letter code. 
     */
    public Integer getKeyCode(String key) {
	KeyStroke ks = getKeyStroke(key);
	return (ks != null) ? new Integer(ks.getKeyCode()) : null;
    }
    
    /**
     * Gets the Icon image to display as read from the properties. Extends the 
     * base <code>UIProperties.getIcon(Object)</code> to handle the key being
     * a String. Properties can be from a predefined class (extending 
     * <code>ListResourceBundle</code>) or from a file (from 
     * <code>PropertyResourceBundle</code>). The latter contains only strings, 
     * so adding Icons requires using file names, and the <code>Resources</code>
     * load from resource independent places. 
     * @return Icon to use.
     * @param key
     * @see Resources
     */
    public Icon getIcon (Object key)
    {
        Object value = get (key);
        if (value instanceof Icon) return (Icon)value;
        if (value instanceof String) return Resources.getIcon((String)value);
        return null;
    }
}
