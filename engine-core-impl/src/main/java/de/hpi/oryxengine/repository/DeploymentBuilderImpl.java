package de.hpi.oryxengine.repository;

import java.io.InputStream;

import de.hpi.oryxengine.RepositoryServiceImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * The Class DeployerImpl. It allows to deploy a process definition for a specific navigator. During this, start events
 * are registered.
 */
public class DeploymentBuilderImpl implements DeploymentBuilder {

    private RepositoryServiceImpl repositoryServiceImpl;

    public DeploymentBuilderImpl(RepositoryServiceImpl repositoryServiceImpl) {

        this.repositoryServiceImpl = repositoryServiceImpl;
    }

    @Override
    public DeploymentBuilder deployResourceAsInputStream(String resourceName, InputStream inputStream) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeploymentBuilder deployClasspathResource(String resourceName, String resourceClasspath) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeploymentBuilder deployResourceAsString(String resourceName, String resourceStringContent) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeploymentBuilder deployProcessDefinition(String processDefinitionName,
                                                     ProcessDefinitionImporter processDefinitionImporter) {

        ProcessDefinition processDefinition = processDefinitionImporter.createProcessDefinition();
        processDefinition.setName(processDefinitionName);

        repositoryServiceImpl.getProcessDefinitionsTable().put(processDefinition.getID(), processDefinition);

        return this;
    }

    @Override
    public DeploymentBuilder deployProcessDefinition(ProcessDefinitionImporter processDefinitionImporter) {

        ProcessDefinition processDefinition = processDefinitionImporter.createProcessDefinition();
        
        repositoryServiceImpl.getProcessDefinitionsTable().put(processDefinition.getID(), processDefinition);
        
        return this;
    }

}

// implements Deployer, Service {
//
// private final Logger logger = LoggerFactory.getLogger(getClass());
//
// @Override
// public void start() {
//
// logger.info("Starting the correlation manager");
// }
//
// @Override
// public void stop() {
//
// logger.info("Stopping the correlation manager");
// }
//
// @Override
// public void deploy(ProcessDefinition def) {
//
// // Add the given definition to the repository
// RepositoryService repo = ServiceFactory.getRepositoryService();
// repo.addDefinition(def);
//
// // Register start events at event manager.
// CorrelationManager correlation = ServiceFactory.getCorrelationService();
// for (StartEvent event : def.getStartTriggers().keySet()) {
// correlation.registerStartEvent(event);
// }
// }
//
// @Override
// public void deactivate(UUID id, Navigator nav) {
//
// }
//
// @Override
// public void delete(UUID id, Navigator nav) {
//
// // Remove definition from repository.
// RepositoryService repo = ServiceFactory.getRepositoryService();
// repo.deleteDefinition(id);
//
// // unregister events from event manager.
// }

