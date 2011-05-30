package org.jodaengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jodaengine.bootstrap.Service;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.DeploymentBuilderImpl;
import org.jodaengine.deployment.DeploymentScope;
import org.jodaengine.deployment.DeploymentScopeImpl;
import org.jodaengine.deployment.importer.archive.DarImporter;
import org.jodaengine.deployment.importer.archive.DarImporterImpl;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.ext.AbstractExtensible;
import org.jodaengine.ext.listener.RepositoryDeploymentListener;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.forms.AbstractForm;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.ProcessDefinitionInside;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ProcessRepositoryImpl. The Repository holds the process definitions in the engine. To instantiate these,
 * the repository has to be asked.
 */
public class RepositoryServiceImpl extends AbstractExtensible implements RepositoryServiceInside, Service {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String SIMPLE_PROCESS_NAME = UUID.randomUUID().toString();
    public static final ProcessDefinitionID SIMPLE_PROCESS_ID = new ProcessDefinitionID(SIMPLE_PROCESS_NAME, 0);

    private Map<ProcessDefinitionID, ProcessDefinition> processDefinitionsTable;

    // we do not need a link from scopes to all corresponding process definitions, as the scope should be automatically
    // dropped, as soon as no definition links to the scope any longer.
    private Map<ProcessDefinitionID, DeploymentScope> scopes;

    private Map<String, Integer> processVersions;

    // private Map<UUID, AbstractProcessArtifact> processArtifactsTable;
    
    private ExtensionService extensionService;

    private boolean running = false;

    /**
     * Default constructor.
     * 
     * @param extensionService an {@link ExtensionService} instance
     */
    public RepositoryServiceImpl(@Nullable ExtensionService extensionService) {
        
        processVersions = new HashMap<String, Integer>();
        scopes = new HashMap<ProcessDefinitionID, DeploymentScope>();
        this.extensionService = extensionService;
    }

    @Override
    public synchronized void start(JodaEngineServices services) {
        
        logger.info("Starting the RespositoryService.");
        
        if (this.extensionService != null) {
            this.registerListeners(
                RepositoryDeploymentListener.class,
                this.extensionService.getExtensions(RepositoryDeploymentListener.class));
        }
        
        this.running = true;
    }

    @Override
    public synchronized void stop() {

        logger.info("Stopping the RespositoryService");

        this.running = false;
    }

    @Override
    public boolean isRunning() {
        return this.running;
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

        getProcessDefinitionsTable().put(definition.getID(), definition);
        
        for (RepositoryDeploymentListener listener: this.getListeners(RepositoryDeploymentListener.class)) {
            listener.definitionDeployed(this, definition);
        }
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
    public void deleteProcessDefinition(ProcessDefinitionID processDefinitionID) {
        
        removeDefinitionFromScope(processDefinitionID);
        ProcessDefinition deleted = getProcessDefinitionsTable().remove(processDefinitionID);
        
        for (RepositoryDeploymentListener listener: this.getListeners(RepositoryDeploymentListener.class)) {
            listener.definitionDeleted(this, deleted);
        }
    }

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

    @Override
    public ProcessDefinitionInside getProcessDefinitionInside(ProcessDefinitionID processDefintionID)
    throws DefinitionNotFoundException {
        
        return (ProcessDefinitionInside) getProcessDefinition(processDefintionID);
    }

    @Override
    public DeploymentScope deployInNewScope(Deployment processDeployment) {
        
        DeploymentScope scope = new DeploymentScopeImpl(processDeployment.getArtifacts(), processDeployment.getForms());
        
        for (ProcessDefinition definition : processDeployment.getDefinitions()) {
            // determine version of the currently deployed process
            registerNewProcessVersion(definition);

            // add the definition
            addProcessDefinition(definition);

            // create the connection between definition and scope
            setScopeForDefinition(definition.getID(), scope);
        }
        
        Map<String, byte[]> customClasses = processDeployment.getClasses();
        for (String className : customClasses.keySet()) {
            scope.addClass(className, customClasses.get(className));
        }
        
        for (RepositoryDeploymentListener listener: this.getListeners(RepositoryDeploymentListener.class)) {
            listener.deploymentDeployed(this, processDeployment);
        }
        
        return scope;
    }

    /**
     * Sets the scope of the definition.
     * 
     * @param definitionID
     *            the definition id
     * @param scope
     *            the scope
     */
    private void setScopeForDefinition(ProcessDefinitionID definitionID, DeploymentScope scope) {
        
        scopes.put(definitionID, scope);
    }

    /**
     * Removes the link between the definition and its scope.
     * 
     * @param definitionID
     *            the definition id
     */
    private void removeDefinitionFromScope(ProcessDefinitionID definitionID) {
        
        scopes.remove(definitionID);
    }

    /**
     * Registers a new version of a process.
     * 
     * @param definition
     *            the definition
     */
    private void registerNewProcessVersion(ProcessDefinition definition) {
        
        Integer currentVersionNumber = processVersions.get(definition.getID().getIdentifier());
        if (currentVersionNumber != null) {
            currentVersionNumber++;
        } else {
            currentVersionNumber = Integer.valueOf(0);
        }
        processVersions.put(definition.getID().getIdentifier(), currentVersionNumber);
        definition.getID().setVersion(currentVersionNumber);
    }

    @Override
    public DeploymentScope getScopeForDefinition(ProcessDefinitionID definitionID) {
        
        return scopes.get(definitionID);
    }

    @Override
    public void addProcessArtifact(AbstractProcessArtifact artifact, ProcessDefinitionID definitionID) {
        
        DeploymentScope scope = scopes.get(definitionID);
        scope.addProcessArtifact(artifact);
        
        for (RepositoryDeploymentListener listener: this.getListeners(RepositoryDeploymentListener.class)) {
            listener.artifactDeployed(this, artifact);
        }
    }

    @Override
    public AbstractProcessArtifact getProcessArtifact(String processArtifactID, ProcessDefinitionID definitionID)
    throws ProcessArtifactNotFoundException, DefinitionNotFoundException {
        
        DeploymentScope scope = scopes.get(definitionID);
        
        if (scope == null) {
            throw new DefinitionNotFoundException(definitionID);
        }
        
        return scope.getProcessArtifact(processArtifactID);
    }

    @Override
    public void deleteProcessArtifact(String processArtifactID, ProcessDefinitionID definitionID) {
        
        DeploymentScope scope = scopes.get(definitionID);
        AbstractProcessArtifact deleted = scope.deleteProcessArtifact(processArtifactID);
        
        for (RepositoryDeploymentListener listener: this.getListeners(RepositoryDeploymentListener.class)) {
            listener.artifactDeleted(this, deleted);
        }
    }

    @Override
    public DarImporter getNewDarImporter() {
        
        return new DarImporterImpl(this, this.extensionService);
    }

    @Override
    public Class<?> getDeployedClass(ProcessDefinitionID definitionID, String fullClassName)
    throws ClassNotFoundException {
        
        // get the class loader
        DeploymentScope scope = scopes.get(definitionID);
        return scope.getClass(fullClassName);
    }

    @Override
    public void addForm(AbstractForm form, ProcessDefinitionID definitionID) {
        
        DeploymentScope scope = scopes.get(definitionID);
        scope.addForm(form);
        
    }

    @Override
    public AbstractForm getForm(String formID, ProcessDefinitionID definitionID)
    throws ProcessArtifactNotFoundException {
        
        DeploymentScope scope = scopes.get(definitionID);
        return scope.getForm(formID);
    }

    @Override
    public void deleteForm(String formID, ProcessDefinitionID definitionID) {

        DeploymentScope scope = scopes.get(definitionID);
        scope.deleteForm(formID);
        
    }

    @Override
    public boolean supportsExtension(Class<?> type) {
        return RepositoryDeploymentListener.class.isAssignableFrom(type);
    }
}
