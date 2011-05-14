package de.hpi.oryxengine.deployment;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

import de.hpi.oryxengine.RepositoryServiceImpl;
import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.process.definition.AbstractProcessArtifact;
import de.hpi.oryxengine.process.definition.ProcessArtifact;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinitionImpl;
import de.hpi.oryxengine.util.io.ClassPathResourceStreamSource;
import de.hpi.oryxengine.util.io.FileStreamSource;
import de.hpi.oryxengine.util.io.InputStreamSource;
import de.hpi.oryxengine.util.io.StreamSource;
import de.hpi.oryxengine.util.io.StringStreamSource;

/**
 * The Class DeployerImpl. It allows to deploy a process definition for a specific navigator. During this, start events
 * are registered.
 */
public class DeploymentBuilderImpl implements DeploymentBuilder {

    private RepositoryServiceImpl repositoryServiceImpl;

    /**
     * Creates a new deployment builder that works on the given repository.
     *
     * @param repositoryServiceImpl the repository to work on (deploy process definitions to, etc.)
     */
    public DeploymentBuilderImpl(RepositoryServiceImpl repositoryServiceImpl) {

        this.repositoryServiceImpl = repositoryServiceImpl;
    }

    @Override
    public UUID deployArtifactAsInputStream(String resourceName, InputStream inputStream) {

        StreamSource inputStreamSource = new InputStreamSource(inputStream);
        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, inputStreamSource);

        deployProcessArtifactInRepository(processArtifact);

        return processArtifact.getID();
    }

    @Override
    public UUID deployArtifactAsClasspathResource(String resourceName, String resourceClasspath) {

        StreamSource classpathResourceStreamSource = new ClassPathResourceStreamSource(resourceClasspath);
        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, classpathResourceStreamSource);

        deployProcessArtifactInRepository(processArtifact);

        return processArtifact.getID();
    }

    @Override
    public UUID deployArtifactAsString(String resourceName, String resourceStringContent) {

        StreamSource stringStreamSource = new StringStreamSource(resourceStringContent);
        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, stringStreamSource);

        deployProcessArtifactInRepository(processArtifact);

        return processArtifact.getID();
    }

    @Override
    public UUID deployArtifactAsFile(String resourceName, File file) {

        StreamSource fileStreamSource = new FileStreamSource(file);
        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, fileStreamSource);
        
        deployProcessArtifactInRepository(processArtifact);
        
        return processArtifact.getID();
    }

    // @Override
    // public UUID deployProcessDefinition(String processDefinitionName,
    // ProcessDefinitionImporter processDefinitionImporter) {
    //
    // ProcessDefinition processDefinition = processDefinitionImporter.createProcessDefinition();
    // processDefinition.setName(processDefinitionName);
    //
    // repositoryServiceImpl.getProcessDefinitionsTable().put(processDefinition.getID(), processDefinition);
    //
    // return processDefinition.getID();
    // }

    /**
     * All {@link ProcessDefinitionImporter ProcessDefinitionImporters} can be found in the the package
     * 'de.hpi.oryxengine.repository.importer'.
     * 
     * {@inheritDoc}
     */
    @Override
    public UUID deployProcessDefinition(ProcessDefinitionImporter processDefinitionImporter) {

        ProcessDefinition processDefinition = processDefinitionImporter.createProcessDefinition();

        // Checking if the ProcessDefintion already exists in the Repository
        if (repositoryServiceImpl.containsProcessDefinition(processDefinition.getID())) {
            String errorMessage = "The ProcessDefinition is already deployed.";
            throw new JodaEngineRuntimeException(errorMessage);
        }

        // TODO @Gerardo das hier noch n
        repositoryServiceImpl.getProcessDefinitionsTable().put(processDefinition.getID(), (ProcessDefinitionImpl) processDefinition);

        return processDefinition.getID();
    }

    private void deployProcessArtifactInRepository(AbstractProcessArtifact processArtifact) {

        // Checking if the ProcessArtifact already exists in the Repository
        if (repositoryServiceImpl.containsProcessDefinition(processArtifact.getID())) {
            String errorMessage = "The ProcessDefinition is already deployed.";
            throw new JodaEngineRuntimeException(errorMessage);
        }

        repositoryServiceImpl.getProcessArtifactsTable().put(processArtifact.getID(), processArtifact);
    }

    @Override
    public ProcessDefinitionBuilder getProcessDefinitionBuilder() {

        return new ProcessDefinitionBuilderImpl();
    }

}
