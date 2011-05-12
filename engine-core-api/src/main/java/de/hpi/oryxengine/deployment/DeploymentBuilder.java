package de.hpi.oryxengine.deployment;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;


/**
 * The class helps to define a deployment and to deploy it.
 * 
 * Be aware that this class does not check whether duplicate!!
 */
public interface DeploymentBuilder {

    /**
     * Adds a resource to the repository. So that it is available for all {@link ProcessDefinition}s.
     * 
     * @param resourceName
     *            - the name of the resource that is deployed
     * @param inputStream
     *            - the {@link InputStream} that contains the content of the
     * @return a {@link UUID} representing the internal ID of the {@link AbstractProcessArtifact ProcessArtifacts
     */
    UUID deployArtifactAsInputStream(String resourceName, InputStream inputStream);

    /**
     * Adds a resource to the repository. So that it is available for all {@link ProcessDefinition}s. This method allows
     * to add the {@link AbstractProcessArtifact} as ClasspathResource.
     * 
     * @param resourceName
     *            - the name of the {@link AbstractProcessArtifact} that is deployed
     * @param resourceClasspath
     *            - the classpath of the {@link AbstractProcessArtifact}
     * @return a {@link UUID} representing the internal ID of the {@link AbstractProcessArtifact ProcessArtifact
     */
    UUID deployArtifactAsClasspathResource(String resourceName, String resourceClasspath);

    /**
     * Adds a resource to the repository. So that it is available for all {@link ProcessDefinition}s. This method allows
     * to add the {@link AbstractProcessArtifact} as {@link String}.
     * 
     * @param resourceName
     *            - the name of the {@link AbstractProcessArtifact} that is deployed
     * @param resourceStringContent
     *            - the {@link String} content of the {@link AbstractProcessArtifact ProcessResource}
     * @return a {@link UUID} representing the internal ID of the {@link AbstractProcessArtifact ProcessArtifact}
     */
    UUID deployArtifactAsString(String resourceName, String resourceStringContent);

    /**
     * Adds a resource to the repository. So that it is available for all {@link ProcessDefinition}s. This method allows
     * to add the {@link AbstractProcessArtifact} as {@link String}.
     * 
     * @param resourceName
     *            - the name of the {@link AbstractProcessArtifact} that is deployed
     * @param file
     *            - the {@link File File Object} that contains the {@link AbstractProcessArtifact ProcessArtifact}
     * @return a {@link UUID} representing the internal ID of the {@link AbstractProcessArtifact ProcessArtifact}
     */
    UUID deployArtifactAsFile(String resourceName, File file);

    // /**
    // * Adds a {@link ProcessDefinition} to the repository. So that it is available for instantiation after activation.
    // * So the {@link ProcessDefinition} have to be activated after this deployment. This method allows to add a
    // * {@link ProcessDefinition}.
    // *
    // * @param processDefinitionName
    // * - the name of the {@link ProcessDefinition}
    // * @param processDefinitionImporter
    // * - that is able to create a {@link ProcessDefinition}
    // * @return a {@link UUID} representing the internal ID of the {@link ProcessDefinition}
    // */
    // UUID deployProcessDefinition(String processDefinitionName,
    // ProcessDefinitionImporter processDefinitionImporter);

    /**
     * Adds a {@link ProcessDefinition} to the repository. So that it is available for instantiation after activation.
     * So the {@link ProcessDefinition} have to be activated after this deployment. This method allows to add a
     * {@link ProcessDefinition}.
     * 
     * The name of the {@link ProcessDefinition} will be the name stored in the {@link ProcessDefinition}. If there is
     * no name define then the name will be the processDefintionID.
     * 
     * {@link ProcessDefinition ProcessDefinitions} are imported using a {@link ProcessDefinitionImporter}. The
     * {@link ProcessDefinitionImporter} allows to create a {@link ProcessDefinition} using several different types.
     * 
     * @param processDefinitionImporter
     *            - that is able to create a {@link ProcessDefinition}
     * @return a {@link UUID} representing the internal ID of the {@link ProcessDefinition}
     * TODO explicit exception
     * @throws JodaEngineRuntimeException
     *             - in case the {@link ProcessDefinition} is already deployed in the {@link RepositoryService
     *             Repository}
     */
    UUID deployProcessDefinition(ProcessDefinitionImporter processDefinitionImporter);

    // /**
    // * Deploys the added {@link AbstractProcessResource ProcessResources} and {@link ProcessDefinition
    // * ProcessDefinitions} at once. Afterwards the {@link DeploymentBuilder} is reseted.
    // *
    // * @return a {@link DeploymentBuilder} in order to keep on deploying resources
    // */
    // DeploymentBuilder deploy();

}
