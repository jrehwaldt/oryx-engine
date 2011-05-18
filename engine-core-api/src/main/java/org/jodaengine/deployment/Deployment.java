package org.jodaengine.deployment;

import java.util.Map;
import java.util.Set;

import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;

/**
 * The deployment is a container used to be able to deploy a process definition together with forms, custom data types
 * etc.
 */
public interface Deployment {

    /**
     * Adds a process definition to the deployment. Definitions with the same ID are allowed, as they will have
     * different versions assigned upon deployment.
     * 
     * @param definition
     *            the definition
     */
    void addProcessDefinition(ProcessDefinition definition);

    /**
     * Adds an artifact to the deployment. Will override any artifact with the same id.
     * 
     * @param artifact
     *            the artifact
     */
    void addProcessArtifact(AbstractProcessArtifact artifact);

    Set<ProcessDefinition> getDefinitions();

    Map<String, AbstractProcessArtifact> getArtifacts();

}
