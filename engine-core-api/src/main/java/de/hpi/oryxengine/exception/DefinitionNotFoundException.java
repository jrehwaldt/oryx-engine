package de.hpi.oryxengine.exception;

import javax.annotation.Nonnull;

/**
 * The Class DefinitionNotFoundException.
 */
public class DefinitionNotFoundException extends DalmatinaException {
    private static final long serialVersionUID = 5826993901901839412L;

    /** The Constant DEFAULT_EXCEPTION_MESSAGE. */
    private static final String DEFAULT_EXCEPTION_MESSAGE =
            "ProcessDefinition or ProcessArtifact with given UUID not found in repository.";

    /**
     * Default Constructor.
     */
    public DefinitionNotFoundException() {

        super(DEFAULT_EXCEPTION_MESSAGE);
    }

    /**
     * Instantiates a new {@link DefinitionNotFoundException}.
     * 
     * @param errorMessage
     *            - the error message that describes the exception
     */
    public DefinitionNotFoundException(@Nonnull String errorMessage) {

        super(errorMessage);
    }
}
