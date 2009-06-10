package stellar.io;

/**
 * An exception thrown by the SectorTextReader during the state machine
 * processing of the Sector file. Usually indicates a malformed or new
 * format of the file. 
 * 
 * @see SectorTextReader
 */
public class SECFileStateMachineException extends Exception 
{
    /**
     * Default Constructor. 
     */
    public SECFileStateMachineException()
    {
        super();
    }
    
    /**
     * Default Constructor with a message
     * @param message Exception reason message. 
     */
    public SECFileStateMachineException (String message)
    {
        super (message);
    }
}