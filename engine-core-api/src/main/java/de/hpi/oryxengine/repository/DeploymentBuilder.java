package de.hpi.oryxengine.repository;

import java.io.InputStream;

/**
 * The class helps to define a deployment and to deploy it. At first the {@link DeploymentBuilder} gathers all
 * {@link AbstractProcessResource ProcessResources} that need to be deployed. By calling the method {@link #deploy()
 * Deploy} all added {@link AbstractProcessResource ProcessResources} will be deployed at once.
 * 
 * Be aware that this class does not check whether duplicate!!
 * 
 * @author Gerardo Navarro
 */
public interface DeploymentBuilder {

    /**
     * Adds a resource to the repository. So that it is available for all {@link ProcessDefinition}s.
     * 
     * @param resourceName
     *            - the name of the resource that is deployed
     * @param inputStream
     *            - the {@link InputStream} that contains the content of the
     * @return a {@link DeploymentBuilder} in order to keep on deploying resources
     */
    DeploymentBuilder addResourceAsInputStream(String resourceName, InputStream inputStream);

    /**
     * Adds a resource to the repository. So that it is available for all {@link ProcessDefinition}s. This method allows
     * to add the {@link AbstractProcessResource} as ClasspathResource.
     * 
     * @param resourceName
     *            - the name of the {@link AbstractProcessResource} that is deployed
     * @param resourceClasspath
     *            - the classpath of the {@link AbstractProcessResource}
     * @return a {@link DeploymentBuilder} in order to keep on deploying resources
     */
    DeploymentBuilder addClasspathResource(String resourceName, String resourceClasspath);

    /**
     * Adds a resource to the repository. So that it is available for all {@link ProcessDefinition}s. This method allows
     * to add the {@link AbstractProcessResource} as {@link String}.
     * 
     * @param resourceName
     *            - the name of the {@link AbstractProcessResource} that is deployed
     * @param resourceStringContent
     *            - the {@link String} content of the {@link AbstractProcessResource ProcessResource}
     * @return a {@link DeploymentBuilder} in order to keep on deploying resources
     */
    DeploymentBuilder addResourceAsString(String resourceName, String resourceStringContent);

    /**
     * Adds a {@link ProcessDefinition} to the repository. So that it is available for instantiation after activation.
     * So the {@link ProcessDefinition} have to be activated after this deployment. This method allows to add the
     * {@link ProcessDefinition} as {@link InputStream}.
     * 
     * @param processDefinitionName
     *            - the name of the {@link ProcessDefinition}
     * @param inputStream
     *            - the {@link InputStream} that contains the {@link ProcessDefinition} in some way
     * @param processDefinitionImporter
     *            - that is able to translate the {@link InputStream} into our {@link ProcessDefinition}
     * @return a {@link DeploymentBuilder} in order to keep on deploying resources
     */
    DeploymentBuilder addProcessDefinitionAsInputStream(String processDefinitionName,
                                                        InputStream inputStream,
                                                        ProcessDefinitionImporter processDefinitionImporter);

    /**
     * Adds a {@link ProcessDefinition} to the repository. So that it is available for instantiation after activation.
     * So the {@link ProcessDefinition} have to be activated after this deployment. This method allows to add the
     * {@link ProcessDefinition} as {@link InputStream}.
     * 
     * The name of the {@link ProcessDefinition} will be the name stored in the {@link ProcessDefinition}. If there is
     * no name define then the name will be the processDefintionID.
     * 
     * @param inputStream
     *            - the {@link InputStream} that contains the {@link ProcessDefinition} in some way
     * @param processDefinitionImporter
     *            - that is able to translate the {@link InputStream} into our {@link ProcessDefinition}
     * @return a {@link DeploymentBuilder} in order to keep on deploying resources
     */
    DeploymentBuilder addProcessDefinitionAsInputStream(InputStream inputStream,
                                                        ProcessDefinitionImporter processDefinitionImporter);

    /**
     * Deploys the added {@link AbstractProcessResource ProcessResources} and {@link ProcessDefinition
     * ProcessDefinitions} at once. Afterwards the {@link DeploymentBuilder} is reseted.
     * 
     * @return a {@link DeploymentBuilder} in order to keep on deploying resources
     */
    DeploymentBuilder deploy();
}
