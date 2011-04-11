package de.hpi.oryxengine;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.repository.Deployment;
import de.hpi.oryxengine.repository.DeploymentBuilder;

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
    DeploymentBuilder createDeployment();

    /**
     * Retrieves all {@link Deployment}s that have been deployed previously.
     * 
     * @return a list containing all {@link Deployment}s
     */
    List<Deployment> getDeployments();

    /**
     * Retrieves a certain {@link Deployment} with the given deplymentID.
     * 
     * @param deploymentID
     *            - id of the deployment, cannot be null.
     * @return a {@link Deployment}
     */
    @Nonnull Deployment getDeployment(@Nonnull UUID deploymentID);

    /**
     * Deletes the given deployment.
     * 
     * @param deploymentID
     *            - id of the deployment, cannot be null.
     * 
     * @throws RuntimeException
     *             if there are still runtime or history process instances or jobs.
     */
    void deleteDeployment(@Nonnull UUID deploymentID);

    /**
     * Deactivates a {@link Deployment} with a ceratin dep. It means that process in that deployment cannot be instantiated.
     * 
     * @param id
     *            the id
     * @param nav
     *            the nav
     */
    void deactivateDeployment(@Nonnull UUID deploymentID);

    void activateDeployment(@Nonnull UUID deploymentID);

    /**
     * Retrieves the {@link ProcessDefinition} with the given key within a deployment.
     * 
     * @param ID
     *            - the ID of the {@link ProcessDefinition}
     * @return a {@link ProcessDefinition}
     * 
     * @throws DefinitionNotFoundException
     *             - thrown, if the given ID does not exist
     */
    ProcessDefinition getDefinition(@Nonnull UUID processDefintionID)
    throws DefinitionNotFoundException;

    /**
     * Retrieves all {@link ProcessDefinition}s that have been deployed.
     * 
     * @return a list containing the {@link ProcessDefinition}s
     */
    List<ProcessDefinition> getDefinitions();

    /**
     * Says YES, whether a certain definition is available.
     * 
     * @param id
     *            the definition's id
     * @return true, ifavailable
     */
    boolean containsDefinition(@Nonnull UUID id);
}
