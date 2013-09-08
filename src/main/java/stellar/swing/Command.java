/*
 * $Id$
 * Copyright 2005 Softstart Services, Inc. All rights reserved. 
 */

package stellar.swing;
import stellar.io.AppDefaults;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

/** 
 * An Action that uses reflection to call a target Object 
 * method and whose properties, like Action.NAME, are effectively 
 * loaded from a resource bundle.  All of the action's properties
 * are initialized with values from the AppDefaults defaults 
 * table - which gets them from the resource bundle.  See
 * UIDefaults.addResourceBundle(), and the resource file[s] 
 * itself in resources.properties.
 * 
 * This code has been borrowed from <href url="http://www.java.net">Java.net</href>,
 * specifically <href url="http://today.java.net/pub/a/today/2005/01/31/controlGUI.html">Asserting Control Over the GUI: Commands, Defaults, and Resource Bundles</href>
 * by Hans Muller. Neither the code nor the article has a copyright notice 
 * attached, which means the code it copyrighted by Hans Muller and is not
 * copyable. 
 */

public class Command extends AbstractAction {

    /* Just a list of the Action property keywords. See the Action
     * javadoc for more information.
     */
    private final static String actionKeys[] = {
	Action.NAME, 
	Action.SHORT_DESCRIPTION,
	Action.LONG_DESCRIPTION,
	Action.SMALL_ICON,
	Action.ACTION_COMMAND_KEY,
	Action.ACCELERATOR_KEY,
	Action.MNEMONIC_KEY,
    };

    private final String methodName;
    private final Object target;


    /** 
     * A command that doesn't do anything.  Used to create an "undefined" 
     * placeholder command.
     * 
     */
    public Command(String name) {
	super(name);
	methodName = null;
	target = null;
    }


    /**
     * Creates a command that calls target.methodName() whose
     * properties are loaded from defaults using keywords created by
     * appending the methodName and each of the standard Action
     * keywords.  For example if methodName was "foo.", then this
     * Command's Action.SHORT_DESCRIPTION property would be the 
     * value of 
     * <pre><code>defaults.get("foo.SHORT_DESCRIPTION")</code></pre>.
     * 
     */
    public Command(Object target, String methodName, AppDefaults defaults) {
	// assert methodName != null or empty, defaults != null
	super(methodName);
	this.methodName = methodName;
	this.target = target;

    for (int i = 0; i < actionKeys.length; i++)
    {
        String k = actionKeys[i];
	    String mk = methodName + "." + k;
	    if (k == Action.MNEMONIC_KEY) {
		putValue(k, defaults.getKeyCode(mk));
	    }
	    else if (k == Action.ACCELERATOR_KEY) {
        KeyStroke stroke = defaults.getKeyStroke(mk);
		putValue(k, stroke);
	    }
	    else if (k == Action.SMALL_ICON) {
		putValue(k, defaults.getIcon(mk));
	    }
	    else {
		putValue(k, defaults.get(mk));
	    }
	}
    }

    /* Log enough output for a developer to figure out 
     * what went wrong.
     */
    protected void actionFailed(ActionEvent actionEvent, Exception e) {
	// TBD Log an error
	System.err.println(actionEvent);
	e.printStackTrace();
    }

    /** 
     * Call target [methodName]() if it exists.  If not, call
     * target.[methodName](actionEvent). If that doesn't exist,
     * or if anything else goes wrong, then call actionFailed().
     */
    public void actionPerformed(ActionEvent actionEvent) {
	if ((target == null) || (methodName == null)) {
	    return;
	}
	Method m = null; 
	Class c = target.getClass();
    
	try {
            m = c.getMethod(methodName, (Class[])null);
	    m.invoke(target, (Object[])null);
	}
	catch (NoSuchMethodException ign) {
	    try {
        Class paramType [] = new Class [1];
        paramType[0] = ActionEvent.class;
		m = c.getMethod(methodName, paramType);
        Object params[] = new Object [1];
        params[0] = actionEvent;
		m.invoke(target, params);
	    }
	    catch (Exception e) {
		actionFailed(actionEvent, e);
	    }
	}
	catch (Exception e) {
	    actionFailed(actionEvent, e);
	}
    }

    @Override public String toString() {
	String name = (String)getValue(NAME); 
	KeyStroke accelerator = (KeyStroke)getValue(ACCELERATOR_KEY);
	Integer mnemonic = (Integer)getValue(MNEMONIC_KEY);
	String tooltip = (String)getValue(SHORT_DESCRIPTION);
	StringBuffer sb = new StringBuffer();
	sb.append(getClass().getName());
	sb.append("[");
	sb.append(name);
	if (accelerator != null) {
	    sb.append(" ");
	    sb.append(accelerator);
	}
	if (tooltip != null) {
	    sb.append(" \"");
	    sb.append(tooltip);
	    sb.append("\"");
	}
	sb.append("]");
	return sb.toString();
    }
}

