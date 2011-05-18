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
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.DeploymentBuilderImpl;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.ProcessDefinitionImpl;
import org.jodaengine.process.definition.ProcessDefinitionInside;


/**
 * The Class ProcessRepositoryImpl. The Repository holds the process definitions in the engine. To instantiate these,
 * the repository has to be asked.
 */
public class RepositoryServiceImpl implements RepositoryServiceInside, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final UUID SIMPLE_PROCESS_UUID = UUID.randomUUID();
    public static final ProcessDefinitionID SIMPLE_PROCESS_ID = new ProcessDefinitionID(SIMPLE_PROCESS_UUID, 0);

    private Map<ProcessDefinitionID, ProcessDefinition> processDefinitionsTable;

    private Map<UUID, Integer> processVersions;
//    private Map<UUID, AbstractProcessArtifact> processArtifactsTable;

    public RepositoryServiceImpl() {
        processVersions = new HashMap<UUID, Integer>();
    }
    
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

        return new DeploymentBuilderImpl();
    }

    @Override
    public List<ProcessDefinition> getProcessDefinitions() {

        List<ProcessDefinition> listToReturn = new ArrayList<ProcessDefinition>(getProcessDefinitionsTable().values());
        return Collections.unmodifiableList(listToReturn);
    }



    @Override
    public void addProcessDefinition(ProcessDefinition definition) {

        getProcessDefinitionsTable().put(definition.getID(), (ProcessDefinitionImpl) definition);
        
    }
    
    @Override
    public boolean containsProcessDefinition(@Nonnull ProcessDefinitionID processDefintionID) {

        return this.getProcessDefinitionsTable().containsKey(processDefintionID);
    }

    @Override
    public void activateProcessDefinition(ProcessDefinitionID processDefintionID) {

        ProcessDefinitionInside processDefintion;
        try {
            processDefintion = (ProcessDefinitionInside) getProcessDefinition(processDefintionID);
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
    public void deactivateProcessDefinition(ProcessDefinitionID processDefintionID) {

        // TODO Auto-generated method stub
    }

    @Override
    public void deleteProcessDefinition(ProcessDefinitionID processResourceID) {

        getProcessDefinitionsTable().remove(processResourceID);
    }

    
    
    
//    @Override
//    public AbstractProcessArtifact getProcessArtifact(UUID processResourceID)
//    throws ProcessArtifactNotFoundException {
//
//        AbstractProcessArtifact processArtifact = getProcessArtifactsTable().get(processResourceID);
//        if (processArtifact == null) {
//            throw new ProcessArtifactNotFoundException(processResourceID);
//        }
//        return getProcessArtifactsTable().get(processResourceID);
//    }
//
//    @Override
//    public List<AbstractProcessArtifact> getProcessArtifacts() {
//
//        List<AbstractProcessArtifact> listToReturn = new ArrayList<AbstractProcessArtifact>(getProcessArtifactsTable()
//        .values());
//        return Collections.unmodifiableList(listToReturn);
//    }
//
//    @Override
//    public void deleteProcessResource(UUID processResourceID) {
//
//        getProcessArtifactsTable().remove(processResourceID);
//    }

    /**
     * Returns a map of all deployed process definitions.
     * 
     * @return the process definitions table
     */
    public Map<ProcessDefinitionID, ProcessDefinition> getProcessDefinitionsTable() {

        if (processDefinitionsTable == null) {
            this.processDefinitionsTable = new HashMap<ProcessDefinitionID, ProcessDefinition>();
        }
        return this.processDefinitionsTable;
    }
    
    @Override
    public ProcessDefinition getProcessDefinition(ProcessDefinitionID processDefintionID)
    throws DefinitionNotFoundException {

        ProcessDefinition processDefinition = getProcessDefinitionsTable().get(processDefintionID);

        if (processDefinition == null) {
            throw new DefinitionNotFoundException(processDefintionID);
        }

        return processDefinition;
    }

//    /**
//     * Returns a map of all deployed process artifacts, such as forms, etc.
//     * 
//     * @return the process artifacts table
//     */
//    public Map<UUID, AbstractProcessArtifact> getProcessArtifactsTable() {
//
//        if (processArtifactsTable == null) {
//            this.processArtifactsTable = new HashMap<UUID, AbstractProcessArtifact>();
//        }
//        return this.processArtifactsTable;
//    }

    @Override
    public ProcessDefinitionInside getProcessDefinitionInside(ProcessDefinitionID processDefintionID)
    throws DefinitionNotFoundException {

        return (ProcessDefinitionInside) getProcessDefinition(processDefintionID);
    }

    @Override
    public void deploy(Deployment processDeployment) {

        for (ProcessDefinition definition : processDeployment.getDefinitions()) {
            // determine version of the currently deployed process
            registerNewProcessVersion(definition);
            
            // add the definition
            getProcessDefinitionsTable().put(definition.getID(), definition);
        }        
    }
    
    /**
     * Registers a new version of a process.
     *
     * @param definition the definition
     */
    private void registerNewProcessVersion(ProcessDefinition definition) {
        Integer currentVersionNumber = processVersions.get(definition.getID().getUUID());
        if (currentVersionNumber != null) {
            currentVersionNumber++;                    
        }
        else {
            currentVersionNumber = new Integer(0);
        }
        processVersions.put(definition.getID().getUUID(), currentVersionNumber);
        definition.getID().setVersion(currentVersionNumber);    
    }

//    @Override
//    public boolean containsProcessArtifact(UUID processResourceID) {
//
//        return this.getProcessArtifactsTable().containsKey(processResourceID);
//    }
//
//    @Override
//    public void addProcessArtifact(AbstractProcessArtifact artifact) {
//
//        getProcessArtifactsTable().put(artifact.getID(), artifact);
//        
//    }
}
