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

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.DeploymentBuilderImpl;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.process.definition.AbstractProcessArtifact;
import de.hpi.oryxengine.process.definition.ProcessDefinition;

/**
 * The Class ProcessRepositoryImpl. The Repository holds the process definitions in the engine. To instantiate these,
 * the repository has to be asked.
 */
public class RepositoryServiceImpl implements RepositoryService, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final UUID SIMPLE_PROCESS_ID = UUID.randomUUID();

    private Map<UUID, ProcessDefinition> processDefinitionsTable;

    private Map<UUID, AbstractProcessArtifact> processArtifactsTable;

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
            throw new DefinitionNotFoundException(processDefintionID);
        }
        
        return processDefinition;
    }

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

        ProcessDefinition processDefintion;
        try {
            processDefintion = getProcessDefinition(processDefintionID);
        } catch (DefinitionNotFoundException exception) {
            String errorMessage = "The processDefinition '" + processDefintionID + "' have not been deployed yet.";
            logger.error(errorMessage, exception);
            throw new JodaEngineRuntimeException(errorMessage, exception);
        }
        
        // Register start events at event manager.
        CorrelationManager correlation = ServiceFactory.getCorrelationService();
        for (StartEvent event : processDefintion.getStartTriggers().keySet()) {
            correlation.registerStartEvent(event);
        }
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
    public AbstractProcessArtifact getProcessResource(UUID processDefinitionID)
    throws DefinitionNotFoundException {
        
        AbstractProcessArtifact processArtifact = getProcessArtifactsTable().get(processDefinitionID);
        if (processArtifact == null) {
            throw new DefinitionNotFoundException(processDefinitionID);
        }
        return getProcessArtifactsTable().get(processDefinitionID);
    }

    @Override
    public List<AbstractProcessArtifact> getProcessResources() {

        List<AbstractProcessArtifact> listToReturn = new ArrayList<AbstractProcessArtifact>(
            getProcessArtifactsTable().values());
        return Collections.unmodifiableList(listToReturn);
    }

    @Override
    public void deleteProcessResource(UUID processArtifact) {
        getProcessArtifactsTable().remove(processArtifact);
    }
    
    /**
     * Returns a map of all deployed process definitions.
     *
     * @return the process definitions table
     */
    public Map<UUID, ProcessDefinition> getProcessDefinitionsTable() {
        
        if (processDefinitionsTable == null) {
            this.processDefinitionsTable = new HashMap<UUID, ProcessDefinition>();
        }
        return this.processDefinitionsTable;
    }
    
    /**
     * Returns a map of all deployed process artifacts, such as forms, etc.
     *
     * @return the process artifacts table
     */
    public Map<UUID, AbstractProcessArtifact> getProcessArtifactsTable() {
        
        if (processArtifactsTable == null) {
            this.processArtifactsTable = new HashMap<UUID, AbstractProcessArtifact>();
        }
        return this.processArtifactsTable;
    }
}
