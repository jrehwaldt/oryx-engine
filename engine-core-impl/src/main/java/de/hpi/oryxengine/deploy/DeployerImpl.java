package de.hpi.oryxengine.deploy;

import java.util.UUID;

import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.repository.ProcessRepository;
import de.hpi.oryxengine.repository.ProcessRepositoryImpl;

/**
 * The Class DeployerImpl.
 */
public class DeployerImpl implements Deployer {

    @Override
    public void deploy(ProcessDefinition def) {

        // Add the given definition to the repository
        ProcessRepository repo = ProcessRepositoryImpl.getInstance();
        repo.addDefinition(def);

        // Register start events at event manager.
    }

    @Override
    public void deactivate(UUID id) {

    }

    @Override
    public void delete(UUID id) {

        // Remove definition from repository.
        ProcessRepository repo = ProcessRepositoryImpl.getInstance();
        repo.deleteDefinition(id);

        // unregister events from event manager.
    }

}
