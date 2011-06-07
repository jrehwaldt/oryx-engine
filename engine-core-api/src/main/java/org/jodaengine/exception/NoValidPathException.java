package org.jodaengine.exception;

import org.jodaengine.process.structure.ControlFlow;

/**
 * The exception which is thrown, for instance, if during the XOR validation no true condition
 * of the outgoing {@link ControlFlow} is found.
 */
public class NoValidPathException extends JodaEngineException {
    private static final long serialVersionUID = 3512908462028716302L;

    private static final String DEFAULT_EXCEPTION_MESSAGE = "No path's conditions evaluated to true.";

    /**
     * Default Constructor.
     */
    public NoValidPathException() {

        super(DEFAULT_EXCEPTION_MESSAGE);
    }

}
