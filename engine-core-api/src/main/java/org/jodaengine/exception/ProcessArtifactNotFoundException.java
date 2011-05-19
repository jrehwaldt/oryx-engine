package org.jodaengine.exception;

import java.util.UUID;

import javax.annotation.Nonnull;

/**
 * The Class ProcessArtifactNotFoundException is used for requested process artifacts that were not found.
 */
public class ProcessArtifactNotFoundException extends JodaEngineException {
    private static final long serialVersionUID = 5826993901901839412L;

    private static final String MESSAGE = "The requested process artifact is not available or was removed.";

    private final UUID definitionID;

    /**
     * Default Constructor.
     * 
     * @param definitionID
     *            the id of the resource that is not available
     */
    public ProcessArtifactNotFoundException(@Nonnull UUID definitionID) {

        super(MESSAGE);

        this.definitionID = definitionID;
    }

    /**
     * Gets the definition id.
     * 
     * @return the definition id
     */
    public UUID getProcessDefinitionID() {

        return definitionID;
    }

    @Override
    @Nonnull
    public String toString() {

        return String.format("ProcessDefinition[id: %s]", getProcessDefinitionID());
    }
}
