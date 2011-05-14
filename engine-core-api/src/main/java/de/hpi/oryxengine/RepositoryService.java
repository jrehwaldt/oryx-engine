package de.hpi.oryxengine;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.definition.AbstractProcessArtifact;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;

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
    ProcessDefinition getProcessDefinition(@Nonnull UUID processDefintionID)
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
    boolean containsProcessDefinition(@Nonnull UUID processDefintionID);

    /**
     * Deletes the given {@link ProcessDefinition}.
     * 
     * @param processDefintionID
     *            - id of the {@link ProcessDefinition}, cannot be null.
     */
    void deleteProcessDefinition(@Nonnull UUID processDefintionID);

    /**
     * Deactivates a {@link ProcessDefinition} with the given processDefinitionID. It means that no
     * {@link AbstractProcessInstance} of that {@link ProcessDefinition} cannot be instantiated.
     * 
     * @param processDefintionID
     *            - id of the {@link ProcessDefinition}, cannot be null.
     */
    void deactivateProcessDefinition(@Nonnull UUID processDefintionID);

    /**
     * Activates a {@link ProcessDefinition} with the given processDefinitionID. It means that no
     * {@link AbstractProcessInstance} of that {@link ProcessDefinition} can be instantiated.
     * 
     * @param processDefintionID
     *            - id of the {@link ProcessDefinition}, cannot be null.
     */
    void activateProcessDefinition(@Nonnull UUID processDefintionID);

    /**
     * Retrieves a certain {@link AbstractProcessArtifact ProcessResource} with the given processResourceID.
     * 
     * @param processResourceID
     *            - id of the {@link AbstractProcessArtifact ProcessResource}, cannot be null.
     * 
     * @return a {@link AbstractProcessArtifact ProcessResource}
     * 
     * @throws DefinitionNotFoundException
     *             - thrown, if the given ID does not exist
     */
    @Nonnull
    AbstractProcessArtifact getProcessResource(@Nonnull UUID processResourceID)
    throws DefinitionNotFoundException;

    /**
     * Retrieves all {@link AbstractProcessArtifact ProcessResources} that have been deployed previously.
     * 
     * @return a list containing all {@link AbstractProcessArtifact ProcessResources}
     */
    List<AbstractProcessArtifact> getProcessResources();

    /**
     * Deletes the given {@link AbstractProcessArtifact ProcessResource}.
     * 
     * @param processResourceID
     *            - id of the {@link AbstractProcessArtifact ProcessResource}, cannot be null.
     */
    void deleteProcessResource(@Nonnull UUID processResourceID);
}
