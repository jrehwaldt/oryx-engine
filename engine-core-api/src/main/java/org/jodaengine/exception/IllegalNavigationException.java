package org.jodaengine.exception;

/**
 * The Class IllegalNavigationException.
 * Is called if someone invokes an undefined navigation-task on ProcessInstance
 * 
 * @deprecated No one uses it... remove if used.
 */
@Deprecated
public class IllegalNavigationException extends JodaEngineException {
    private static final long serialVersionUID = -8543624402152145386L;

    private static final String DEFAULT_EXCEPTION_MESSAGE = "Please. Don't try to take this way.";

    /**
     * Default Constructor.
     */
    public IllegalNavigationException() {

        super(DEFAULT_EXCEPTION_MESSAGE);
    }
}
