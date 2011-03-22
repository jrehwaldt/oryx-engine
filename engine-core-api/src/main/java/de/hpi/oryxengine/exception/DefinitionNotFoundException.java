package de.hpi.oryxengine.exception;

/**
 * The Class DefinitionNotFoundException.
 */
public class DefinitionNotFoundException extends DalmatinaException {
    private static final long serialVersionUID = 5826993901901839412L;
    
    /** The Constant DEFAULT_EXCEPTION_MESSAGE. */
    private static final String DEFAULT_EXCEPTION_MESSAGE = "Process definition "
        + "with given UUID not found in repository.";
 
    /**
     * Default Constructor.
     */
    public DefinitionNotFoundException() {
        super(DEFAULT_EXCEPTION_MESSAGE);
    }
}
