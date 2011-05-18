package org.jodaengine.deployment;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionID;

/**
 * The class helps to define a deployment and to deploy it.
 * 
 * Be aware that this class does not check whether duplicate!!
 */
public interface DeploymentBuilder {

//    /**
//     * Adds a resource to the repository. So that it is available for all {@link ProcessDefinition}s.
//     * 
//     * @param resourceName
//     *            - the name of the resource that is deployed
//     * @param inputStream
//     *            - the {@link InputStream} that contains the content of the
//     * @return a {@link UUID} representing the internal ID of the {@link AbstractProcessArtifact ProcessArtifacts
//     */
//    UUID deployArtifactAsInputStream(String resourceName, InputStream inputStream);
//
//    /**
//     * Adds a resource to the repository. So that it is available for all {@link ProcessDefinition}s. This method allows
//     * to add the {@link AbstractProcessArtifact} as ClasspathResource.
//     * 
//     * @param resourceName
//     *            - the name of the {@link AbstractProcessArtifact} that is deployed
//     * @param resourceClasspath
//     *            - the classpath of the {@link AbstractProcessArtifact}
//     * @return a {@link UUID} representing the internal ID of the {@link AbstractProcessArtifact ProcessArtifact
//     */
//    UUID deployArtifactAsClasspathResource(String resourceName, String resourceClasspath);
//
//    /**
//     * Adds a resource to the repository. So that it is available for all {@link ProcessDefinition}s. This method allows
//     * to add the {@link AbstractProcessArtifact} as {@link String}.
//     * 
//     * @param resourceName
//     *            - the name of the {@link AbstractProcessArtifact} that is deployed
//     * @param resourceStringContent
//     *            - the {@link String} content of the {@link AbstractProcessArtifact ProcessResource}
//     * @return a {@link UUID} representing the internal ID of the {@link AbstractProcessArtifact ProcessArtifact}
//     */
//    UUID deployArtifactAsString(String resourceName, String resourceStringContent);
//
//    /**
//     * Adds a resource to the repository. So that it is available for all {@link ProcessDefinition}s. This method allows
//     * to add the {@link AbstractProcessArtifact} as {@link String}.
//     * 
//     * @param resourceName
//     *            - the name of the {@link AbstractProcessArtifact} that is deployed
//     * @param file
//     *            - the {@link File File Object} that contains the {@link AbstractProcessArtifact ProcessArtifact}
//     * @return a {@link UUID} representing the internal ID of the {@link AbstractProcessArtifact ProcessArtifact}
//     */
//    UUID deployArtifactAsFile(String resourceName, File file);
//
//    /**
//     * Adds a {@link ProcessDefinition} to the repository. So that it is available for instantiation after activation.
//     * So the {@link ProcessDefinition} have to be activated after this deployment. This method allows to add a
//     * {@link ProcessDefinition}.
//     * 
//     * The name of the {@link ProcessDefinition} will be the name stored in the {@link ProcessDefinition}. If there is
//     * no name define then the name will be the processDefintionID.
//     * 
//     * {@link ProcessDefinition ProcessDefinitions} are imported using a {@link ProcessDefinitionImporter}. The
//     * {@link ProcessDefinitionImporter} allows to create a {@link ProcessDefinition} using several different types.
//     * 
//     * @param processDefinitionImporter
//     *            - that is able to create a {@link ProcessDefinition}
//     * @return a {@link UUID} representing the internal ID of the {@link ProcessDefinition} TODO explicit exception
//     * @throws JodaEngineRuntimeException
//     *             - in case the {@link ProcessDefinition} is already deployed in the {@link RepositoryService
//     *             Repository}
//     */
//    ProcessDefinitionID deployProcessDefinition(ProcessDefinitionImporter processDefinitionImporter);
//
    
    void addProcessDefinition(ProcessDefinition definition);
    
    void addProcessArtifact(AbstractProcessArtifact artifact);
    
    /**
     * Builds the deployment.
     *
     * @return the deployment
     */
    Deployment buildDeployment();
    
    /**
     * Retrieves a {@link ProcessDefinitionBuilder} that helps an client to build {@link ProcessDefinition
     * processDefinitions} from scratch and customized.
     * 
     * @return a {@link ProcessDefinitionBuilder}
     */
    ProcessDefinitionBuilder getProcessDefinitionBuilder();

}
