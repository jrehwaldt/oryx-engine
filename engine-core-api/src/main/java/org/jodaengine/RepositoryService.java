package org.jodaengine;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.DeploymentScope;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instance.AbstractProcessInstance;

/**
 * The RepositoryService offers method to manage the processes that have been deployed to the navigator.
 * 
 * We decided that processes should be deployed in whole {@link Deployment}Units that It holds the process definitions
 * that are currently deployed identified by their id.
 */
public interface RepositoryService {

    /**
     * Creates a {@link DeploymentBuilder} that helps to create a deployment containing {@link ProcessDefinition}s and
     * other resources.
     * 
     * @return a {@link DeploymentBuilder}
     */
    DeploymentBuilder getDeploymentBuilder();

    /**
     * Retrieves the {@link ProcessDefinition} with the given processDefinitionID.
     * 
     * @param processDefintionID
     *            - the ID of the {@link ProcessDefinition}
     * @return a {@link ProcessDefinition}
     * 
     * @throws DefinitionNotFoundException
     *             - thrown, if the given ID does not exist
     */
    ProcessDefinition getProcessDefinition(@Nonnull ProcessDefinitionID processDefintionID)
    throws DefinitionNotFoundException;

    /**
     * Retrieves all {@link ProcessDefinition}s that have been deployed.
     * 
     * @return a list containing the {@link ProcessDefinition}s
     */
    List<ProcessDefinition> getProcessDefinitions();

    /**
     * Says Yes, whether a certain definition is available.
     * 
     * @param processDefintionID
     *            the definition's id
     * @return true, if available
     */
    boolean containsProcessDefinition(@Nonnull ProcessDefinitionID processDefintionID);

    /**
     * Adds a process definition to the repository.
     * 
     * @param definition
     *            the definition
     */
    void addProcessDefinition(@Nonnull ProcessDefinition definition);

    /**
     * Deletes the given {@link ProcessDefinition}.
     * 
     * @param processDefintionID
     *            - id of the {@link ProcessDefinition}, cannot be null.
     */
    void deleteProcessDefinition(@Nonnull ProcessDefinitionID processDefintionID);

    /**
     * Deactivates a {@link ProcessDefinition} with the given processDefinitionID. It means that no
     * {@link AbstractProcessInstance} of that {@link ProcessDefinition} cannot be instantiated.
     * 
     * @param processDefintionID
     *            - id of the {@link ProcessDefinition}, cannot be null.
     */
    void deactivateProcessDefinition(@Nonnull ProcessDefinitionID processDefintionID);

    /**
     * Activates a {@link ProcessDefinition} with the given processDefinitionID. It means that no
     * {@link AbstractProcessInstance} of that {@link ProcessDefinition} can be instantiated.
     * 
     * @param processDefintionID
     *            - id of the {@link ProcessDefinition}, cannot be null.
     */
    void activateProcessDefinition(@Nonnull ProcessDefinitionID processDefintionID);

    // /**
    // * Adds a process artifact to the repository.
    // *
    // * @param artifact the artifact
    // */
    // void addProcessArtifact(@Nonnull AbstractProcessArtifact artifact);
    //
    // /**
    // * Retrieves a certain {@link AbstractProcessArtifact ProcessResource} with the given processResourceID.
    // *
    // * @param processResourceID - id of the {@link AbstractProcessArtifact ProcessResource}, cannot be null.
    // * @return a {@link AbstractProcessArtifact ProcessResource}
    // * @throws ProcessArtifactNotFoundException thrown if the artifact does not exist
    // */
    // @Nonnull
    // AbstractProcessArtifact getProcessArtifact(@Nonnull UUID processResourceID)
    // throws ProcessArtifactNotFoundException;
    //
    // /**
    // * Retrieves all {@link AbstractProcessArtifact ProcessArtifacts} that have been deployed previously.
    // *
    // * @return a list containing all {@link AbstractProcessArtifact ProcessResources}
    // */
    // List<AbstractProcessArtifact> getProcessArtifacts();
    //
    // /**
    // * Checks whether the repository has saved an artifact with the given id.
    // *
    // * @param processResourceID the process resource id
    // * @return true, if successful
    // */
    // boolean containsProcessArtifact(UUID processResourceID);
    //
    // /**
    // * Deletes the given {@link AbstractProcessArtifact ProcessResource}.
    // *
    // * @param processResourceID
    // * - id of the {@link AbstractProcessArtifact ProcessResource}, cannot be null.
    // */
    // void deleteProcessResource(@Nonnull UUID processResourceID);

    /**
     * Deploys a deployment (i.e. the contained process definition, artifacts, etc.) to the repository. The contained
     * resources are deployed in a new scope, that means, the definitions share only the contained forms, etc.
     * 
     * @param processDeployment
     *            the process deployment
     */
    DeploymentScope deployInNewScope(Deployment processDeployment);
    
    DeploymentScope getScopeForDefinition(ProcessDefinitionID definitionID);
}
