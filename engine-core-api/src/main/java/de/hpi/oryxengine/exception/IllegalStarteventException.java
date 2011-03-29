package de.hpi.oryxengine.exception;

/**
 * The Class IllegalStarteventException. It is thrown if a start event is registered for a node that is not a start
 * node.
 */
public class IllegalStarteventException extends Exception {

    private static final long serialVersionUID = -4385034233606773441L;

    /** The Constant DEFAULT_EXCEPTION_MESSAGE. */
    private static final String DEFAULT_EXCEPTION_MESSAGE = "Attempted to register a start event for a non-start node";

    /**
     * Default Constructor.
     */
    public IllegalStarteventException() {

        super(DEFAULT_EXCEPTION_MESSAGE);
    }
}
