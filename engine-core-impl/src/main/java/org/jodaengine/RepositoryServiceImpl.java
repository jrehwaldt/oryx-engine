package org.jodaengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jodaengine.bootstrap.Service;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.DeploymentBuilderImpl;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionImpl;
import org.jodaengine.process.definition.ProcessDefinitionInside;


/**
 * The Class ProcessRepositoryImpl. The Repository holds the process definitions in the engine. To instantiate these,
 * the repository has to be asked.
 */
public class RepositoryServiceImpl implements RepositoryServiceInside, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final UUID SIMPLE_PROCESS_ID = UUID.randomUUID();

    private Map<UUID, ProcessDefinitionImpl> processDefinitionsTable;

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

        return getProcessDefinitionImpl(processDefintionID);
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

        ProcessDefinitionInside processDefintion;
        try {
            processDefintion = getProcessDefinitionImpl(processDefintionID);
        } catch (DefinitionNotFoundException exception) {
            String errorMessage = "The processDefinition '" + processDefintionID + "' have not been deployed yet.";
            logger.error(errorMessage, exception);
            throw new JodaEngineRuntimeException(errorMessage, exception);
        }

        // Register start events at event manager.
        // TODO: Das muss raus gehalten werden
        processDefintion.activate(ServiceFactory.getCorrelationService());
        // for (StartEvent event : processDefintion.getStartTriggers().keySet()) {
        // correlation.registerStartEvent(event);
        // }

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
    public AbstractProcessArtifact getProcessResource(UUID processResourceID)
    throws DefinitionNotFoundException {

        AbstractProcessArtifact processArtifact = getProcessArtifactsTable().get(processResourceID);
        if (processArtifact == null) {
            throw new DefinitionNotFoundException(processResourceID);
        }
        return getProcessArtifactsTable().get(processResourceID);
    }

    @Override
    public List<AbstractProcessArtifact> getProcessResources() {

        List<AbstractProcessArtifact> listToReturn = new ArrayList<AbstractProcessArtifact>(getProcessArtifactsTable()
        .values());
        return Collections.unmodifiableList(listToReturn);
    }

    @Override
    public void deleteProcessResource(UUID processResourceID) {

        getProcessArtifactsTable().remove(processResourceID);
    }

    /**
     * Returns a map of all deployed process definitions.
     * 
     * @return the process definitions table
     */
    public Map<UUID, ProcessDefinitionImpl> getProcessDefinitionsTable() {

        if (processDefinitionsTable == null) {
            this.processDefinitionsTable = new HashMap<UUID, ProcessDefinitionImpl>();
        }
        return this.processDefinitionsTable;
    }

    /**
     * Retrieves the {@link ProcessDefinitionImpl} that is stored.
     * 
     * @param processDefintionID
     *            - the {@link UUID id} of the {@link ProcessDefinition}
     * @return a {@link ProcessDefinitionImpl}
     * @throws DefinitionNotFoundException
     *             - thrown, if the given ID does not exist
     */
    private ProcessDefinitionImpl getProcessDefinitionImpl(UUID processDefintionID)
    throws DefinitionNotFoundException {

        ProcessDefinitionImpl processDefinition = getProcessDefinitionsTable().get(processDefintionID);

        if (processDefinition == null) {
            throw new DefinitionNotFoundException(processDefintionID);
        }

        return processDefinition;
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

    @Override
    public ProcessDefinitionInside getProcessDefinitionInside(UUID processDefintionID)
    throws DefinitionNotFoundException {

        return getProcessDefinitionImpl(processDefintionID);
    }
}
