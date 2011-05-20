package org.jodaengine.deployment;

import java.util.Map;
import java.util.Set;

import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * The deployment is a container used to be able to deploy a process definition together with forms, custom data types
 * etc.
 */
public interface Deployment {

    /**
     * Adds a {@link ProcessDefinition} to the deployment. Definitions with the same {@link ProcessDefinitionID} are
     * allowed, as they will have different versions assigned upon deployment.
     * 
     * @param definition
     *            the definition
     */
    void addProcessDefinition(ProcessDefinition definition);

    /**
     * Adds an {@link AbstractProcessArtifact} to the deployment. Will override any artifact with the same id.
     * 
     * @param artifact
     *            the artifact
     */
    void addProcessArtifact(AbstractProcessArtifact artifact);

    /**
     * Gets all contained {@link ProcessDefinition}.
     * 
     * @return the definitions
     */
    Set<ProcessDefinition> getDefinitions();

    /**
     * Gets a map of all contained artifact identifiers pointing to the specific {@link AbstractProcessArtifact}s.
     * 
     * @return the artifacts
     */
    Map<String, AbstractProcessArtifact> getArtifacts();

}
