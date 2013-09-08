/*
 * $Id$
 * Copyright 2005 Softstart Services, Inc. All rights reserved. 
 */
package stellar.swing;
import javax.swing.event.ChangeEvent;
import stellar.data.Astrogation;

/**
 * A change event specific to changes to the Astrogation data structure. 
 * 
 * @author Thomas Jones-Low
 * @version $Id$
 */
public class AstrogationChangeEvent extends ChangeEvent
{
    /**
     * Default Constructor.
     * @param source The updated Astrogation data structure. 
     */
    public AstrogationChangeEvent(Astrogation source)
    {
        super (source);
    }
    
}
