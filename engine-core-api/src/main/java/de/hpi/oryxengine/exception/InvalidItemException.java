package de.hpi.oryxengine.exception;
/**
 * The Class InvalidItemException.
 */
public class InvalidItemException extends DalmatinaException {
    
    private static final String MESSAGE = "The requested item is (no longer) available.";

    /**
     * Instantiates a new invalid item exception.
     *
     */
    public InvalidItemException() {

        super(MESSAGE, null);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
