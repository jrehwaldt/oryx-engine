package de.hpi.oryxengine.exception;

/**
 * The Class IllegalNavigationException.
 * Is called if someone invokes an undefined navigation-task on ProcessInstance
 */
public class IllegalNavigationException extends OryxEngineException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_EXCEPTION_MESSAGE = "Please. Don't try to take this way.";

    /**
     * Default Constructor.
     */
    public IllegalNavigationException() {

        super(DEFAULT_EXCEPTION_MESSAGE);
    }
}
