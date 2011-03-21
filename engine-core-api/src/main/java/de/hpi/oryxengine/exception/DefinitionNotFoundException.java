package de.hpi.oryxengine.exception;

/**
 * The Class DefinitionNotFoundException.
 */
public class DefinitionNotFoundException extends OryxEngineException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

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
