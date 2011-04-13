package de.hpi.oryxengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.repository.AbstractProcessArtifacts;
import de.hpi.oryxengine.repository.DeploymentBuilder;
import de.hpi.oryxengine.repository.DeploymentBuilderImpl;

/**
 * The Class ProcessRepositoryImpl. The Repository holds the process definitions in the engine. To instantiate these,
 * the repository has to be asked.
 */
public class RepositoryServiceImpl implements RepositoryService, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final UUID SIMPLE_PROCESS_ID = UUID.randomUUID();

    private Map<UUID, ProcessDefinition> processDefinitionsTable;

    private Map<UUID, AbstractProcessArtifacts> processArtifactsTable;

    @Override
    public void start() {

        logger.info("Starting the RespositoryService.");
    }

    @Override
    public void stop() {

        logger.info("Stopping the RespositoryService");
    }

    @Override
    public DeploymentBuilder getDeploymentBuilder() {

        return new DeploymentBuilderImpl(this);
    }

    @Override
    public ProcessDefinition getProcessDefinition(UUID processDefintionID)
    throws DefinitionNotFoundException {

        ProcessDefinition processDefinition = getProcessDefinitionsTable().get(processDefintionID);

        if (processDefinition == null) {
            throw new DefinitionNotFoundException();
        }
        return processDefinition;
    }

    // public void addDefinition(ProcessDefinition definition) {
    //
    // UUID id = definition.getID();
    // definitions.put(id, definition);
    // }
    //
    // public void deleteDefinition(UUID id) {
    //
    // definitions.remove(id);
    //
    // }

    @Override
    public List<ProcessDefinition> getProcessDefinitions() {

        List<ProcessDefinition> listToReturn = new ArrayList<ProcessDefinition>(getProcessDefinitionsTable().values());
        return Collections.unmodifiableList(listToReturn);
    }

    @Override
    public boolean containsProcessDefinition(@Nonnull UUID processDefintionID) {

        return this.getProcessDefinitionsTable().containsKey(processDefintionID);
    }

    @Override
    public void activateProcessDefinition(UUID processDefintionID) {

        // TODO Auto-generated method stub

    }

    @Override
    public void deactivateProcessDefinition(UUID processDefintionID) {

        // TODO Auto-generated method stub

    }

    @Override
    public void deleteProcessDefinition(UUID processResourceID) {

        getProcessDefinitionsTable().remove(processResourceID);
    }

    @Override
    public AbstractProcessArtifacts getProcessResource(UUID id)
    throws DefinitionNotFoundException {

        AbstractProcessArtifacts processArtifact = getProcessArtifactsTable().get(id);
        if (processArtifact == null) {
            throw new DefinitionNotFoundException("The ProcessArtifact (with the ID: '" + id + "') "
                + "has not been deployed.");
        }
        return getProcessArtifactsTable().get(id);
    }

    @Override
    public List<AbstractProcessArtifacts> getProcessResources() {

        List<AbstractProcessArtifacts> listToReturn = new ArrayList<AbstractProcessArtifacts>(
            getProcessArtifactsTable().values());
        return Collections.unmodifiableList(listToReturn);
    }

    @Override
    public void deleteProcessResource(UUID processArtifact) {
    
        getProcessArtifactsTable().remove(processArtifact);
    
    }

    public Map<UUID, ProcessDefinition> getProcessDefinitionsTable() {

        if (processDefinitionsTable == null) {
            this.processDefinitionsTable = new HashMap<UUID, ProcessDefinition>();
        }
        return this.processDefinitionsTable;
    }

    public Map<UUID, AbstractProcessArtifacts> getProcessArtifactsTable() {

        if (processArtifactsTable == null) {
            this.processArtifactsTable = new HashMap<UUID, AbstractProcessArtifacts>();
        }
        return this.processArtifactsTable;
    }
}
