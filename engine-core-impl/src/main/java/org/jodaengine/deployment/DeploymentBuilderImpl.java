package org.jodaengine.deployment;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.util.io.ClassPathResourceStreamSource;
import org.jodaengine.util.io.FileStreamSource;
import org.jodaengine.util.io.InputStreamSource;
import org.jodaengine.util.io.StreamSource;
import org.jodaengine.util.io.StringStreamSource;


/**
 * The Class DeployerImpl. It allows to deploy a process definition for a specific navigator. During this, start events
 * are registered.
 */
public class DeploymentBuilderImpl implements DeploymentBuilder {

    

//    private RepositoryService repositoryService;
//
//    /**
//     * Creates a new deployment builder that works on the given repository.
//     *
//     * @param repositoryServiceImpl the repository to work on (deploy process definitions to, etc.)
//     */
//    public DeploymentBuilderImpl(RepositoryService repositoryService) {
//
//        this.repositoryService = repositoryService;
//    }
//
//    @Override
//    public UUID deployArtifactAsInputStream(String resourceName, InputStream inputStream) {
//
//        StreamSource inputStreamSource = new InputStreamSource(inputStream);
//        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, inputStreamSource);
//
//        deployProcessArtifactInRepository(processArtifact);
//
//        return processArtifact.getID();
//    }
//
//    @Override
//    public UUID deployArtifactAsClasspathResource(String resourceName, String resourceClasspath) {
//
//        StreamSource classpathResourceStreamSource = new ClassPathResourceStreamSource(resourceClasspath);
//        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, classpathResourceStreamSource);
//
//        deployProcessArtifactInRepository(processArtifact);
//
//        return processArtifact.getID();
//    }
//
//    @Override
//    public UUID deployArtifactAsString(String resourceName, String resourceStringContent) {
//
//        StreamSource stringStreamSource = new StringStreamSource(resourceStringContent);
//        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, stringStreamSource);
//
//        deployProcessArtifactInRepository(processArtifact);
//
//        return processArtifact.getID();
//    }
//
//    @Override
//    public UUID deployArtifactAsFile(String resourceName, File file) {
//
//        StreamSource fileStreamSource = new FileStreamSource(file);
//        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, fileStreamSource);
//        
//        deployProcessArtifactInRepository(processArtifact);
//        
//        return processArtifact.getID();
//    }
//
//    // @Override
//    // public UUID deployProcessDefinition(String processDefinitionName,
//    // ProcessDefinitionImporter processDefinitionImporter) {
//    //
//    // ProcessDefinition processDefinition = processDefinitionImporter.createProcessDefinition();
//    // processDefinition.setName(processDefinitionName);
//    //
//    // repositoryServiceImpl.getProcessDefinitionsTable().put(processDefinition.getID(), processDefinition);
//    //
//    // return processDefinition.getID();
//    // }
//
//    /**
//     * All {@link ProcessDefinitionImporter ProcessDefinitionImporters} can be found in the the package
//     * 'org.jodaengine.repository.importer'.
//     * 
//     * {@inheritDoc}
//     */
//    @Override
//    public ProcessDefinitionID deployProcessDefinition(ProcessDefinitionImporter processDefinitionImporter) {
//
//        ProcessDefinition processDefinition = processDefinitionImporter.createProcessDefinition();
//
//        // Checking if the ProcessDefintion already exists in the Repository
//        if (repositoryService.containsProcessDefinition(processDefinition.getID())) {
//            String errorMessage = "The ProcessDefinition is already deployed.";
//            throw new JodaEngineRuntimeException(errorMessage);
//        }
//
//        // TODO @Gerardo das hier noch n
//        repositoryService.addProcessDefinition(processDefinition);
//
//        return processDefinition.getID();
//    }
//
//    private void deployProcessArtifactInRepository(AbstractProcessArtifact processArtifact) {
//
//        // Checking if the ProcessArtifact already exists in the Repository
//        if (repositoryService.containsProcessArtifact(processArtifact.getID())) {
//            String errorMessage = "The ProcessDefinition is already deployed.";
//            throw new JodaEngineRuntimeException(errorMessage);
//        }
//
//        repositoryService.addProcessArtifact(processArtifact);
//    }
    
    private Deployment currentDeployment;
    
    public DeploymentBuilderImpl() {
        currentDeployment = new DeploymentImpl();
    }
    

    @Override
    public ProcessDefinitionBuilder getProcessDefinitionBuilder() {

        return new ProcessDefinitionBuilderImpl();
    }

    @Override
    public void addProcessDefinition(ProcessDefinition definition) {

        this.currentDeployment.addProcessDefinition(definition);
        
    }

    @Override
    public void addProcessArtifact(AbstractProcessArtifact artifact) {

        this.currentDeployment.addProcessArtifact(artifact);
        
    }

    @Override
    public Deployment buildDeployment() {

        Deployment returnDeployment = currentDeployment;
        currentDeployment = new DeploymentImpl();
        return returnDeployment;
    }

}
