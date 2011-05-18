package org.jodaengine.deployment;

import java.util.Map;

import org.jodaengine.process.definition.AbstractProcessArtifact;

/**
 * The DeploymentScope realizes the scoping of process resources.
 */
public interface DeploymentScope {

    /**
     * Gets the table of all artifacts that are available in this scope
     *
     * @return the artifacts table
     */
    Map<String, AbstractProcessArtifact> getArtifactsTable();
    
    /**
     * Gets the process artifact with the given identifier.
     *
     * @param identifier the identifier
     * @return the process artifact
     */
    AbstractProcessArtifact getProcessArtifact(String identifier);
}
