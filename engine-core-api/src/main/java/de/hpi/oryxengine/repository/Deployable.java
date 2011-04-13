package de.hpi.oryxengine.repository;

import java.util.UUID;

import de.hpi.oryxengine.RepositoryService;

/**
 * This interface standardizes the method of deploying objects into the repository.
 */
public interface Deployable {

    /**
     * This method deploys an object into the {@link RepositoryService}.
     * 
     * @param repositoryService
     *            - the {@link RepositoryService} where the object is deployed to
     * 
     * @return the UUID of the deployed object
     */
    public UUID deploy(RepositoryService repositoryService);
}
