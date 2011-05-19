package org.jodaengine.exception;

import java.util.UUID;

import javax.annotation.Nonnull;

import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * The Class ProcessArtifactNotFoundException is used for requested process artifacts that were not found.
 */
public class ProcessArtifactNotFoundException extends JodaEngineException {
    private static final long serialVersionUID = 5826993901901839412L;

    private static final String MESSAGE = "The requested process artifact is not available or was removed.";

    private final String artifactID;

    /**
     * Default Constructor.
     * 
     * @param artifactID
     *            the id of the resource that is not available
     */
    public ProcessArtifactNotFoundException(@Nonnull String artifactID) {

        super(MESSAGE);

        this.artifactID = artifactID;
    }

    /**
     * Gets the definition id.
     * 
     * @return the definition id
     */
    public String getProcessArtifactID() {

        return artifactID;
    }

    @Override
    @Nonnull
    public String toString() {

        return String.format("ProcessDefinition[id: %s]", getProcessArtifactID());
    }
}
