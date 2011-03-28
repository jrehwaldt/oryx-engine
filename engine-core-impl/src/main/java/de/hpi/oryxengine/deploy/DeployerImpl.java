package de.hpi.oryxengine.deploy;

import java.util.UUID;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.correlation.CorrelationManagerImpl;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.repository.ProcessRepository;

/**
 * The Class DeployerImpl. It allows to deploy a process definition for a specific navigator. During this, start events
 * are registered.
 */
public class DeployerImpl implements Deployer {

    /**
     * constructor.
     */
    public DeployerImpl() {

    }

    @Override
    public void deploy(ProcessDefinition def, Navigator nav) {

        // Add the given definition to the repository
        ProcessRepository repo = ServiceFactory.getRepositoryService();
        repo.addDefinition(def);

        // Register start events at event manager.
        CorrelationManagerImpl correlation = ServiceFactory.getCorrelationService(nav);
        for (StartEvent event : def.getStartTriggers().keySet()) {
            correlation.registerStartEvent(event);
        }
    }

    @Override
    public void deactivate(UUID id, Navigator nav) {

    }

    @Override
    public void delete(UUID id, Navigator nav) {

        // Remove definition from repository.
        ProcessRepository repo = ServiceFactory.getRepositoryService();
        repo.deleteDefinition(id);

        // unregister events from event manager.
    }

}
