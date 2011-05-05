package de.hpi.oryxengine.exception;

/**
 * This is a runtime exception stating that a resource does not exist.
 * 
 * @author Jan Rehwaldt
 */
public class ResourceNotAvailableException extends JodaEngineException {
    private static final long serialVersionUID = 6124739997417080338L;
    
    private static final String MESSAGE = "The requested resource is (no longer) available.";

    /**
     * Default constructor.
     */
    public ResourceNotAvailableException() {
        super(MESSAGE, null);
    }
}
