package org.jodaengine.deployment;

import java.io.File;
import java.io.InputStream;

import org.jodaengine.forms.AbstractForm;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;

/**
 * The class helps to define a deployment and to deploy it.
 * 
 * Be aware that this class does not check whether duplicate!!
 */
public interface DeploymentBuilder {

    /**
     * Adds a {@link ProcessDefinition}.
     * 
     * @param definition
     *            the definition
     * @return the deployment builder
     */
    DeploymentBuilder addProcessDefinition(ProcessDefinition definition);

    /**
     * Adds an artifact, that is already an {@link AbstractProcessArtifact}. If you have only the concrete resource, use
     * one of the other add***Artifact-Methods.
     * 
     * @param artifact
     *            the artifact
     * @return the deployment builder
     */
    DeploymentBuilder addProcessArtifact(AbstractProcessArtifact artifact);

    /**
     * Creates an {@link AbstractProcessArtifact} from the {@link InputStream} with the given name.
     * 
     * @param resourceName
     *            the resource name
     * @param inputStream
     *            the input stream
     * @return the deployment builder
     */
    DeploymentBuilder addInputStreamArtifact(String resourceName, InputStream inputStream);

    /**
     * Creates an {@link AbstractProcessArtifact} from a resource that is available in the classpath.
     * 
     * @param resourceName
     *            the resource name
     * @param resourceClasspath
     *            the resource classpath
     * @return the deployment builder
     */
    DeploymentBuilder addClasspathResourceArtifact(String resourceName, String resourceClasspath);

    /**
     * Creates an {@link AbstractProcessArtifact} from a simple String.
     * 
     * @param resourceName
     *            the resource name
     * @param resourceStringContent
     *            the resource string content
     * @return the deployment builder
     */
    DeploymentBuilder addStringArtifact(String resourceName, String resourceStringContent);

    /**
     * Creates an {@link AbstractProcessArtifact} from a file that is located in the local file system.
     * 
     * @param resourceName
     *            the resource name
     * @param file
     *            the file
     * @return the deployment builder
     */
    DeploymentBuilder addFileArtifact(String resourceName, File file);

    /**
     * Adds a form to the deployment. As {@link AbstractForm} is a subclass of {@link AbstractProcessArtifact} it might
     * be also deployed as an artifact.
     * In this case, it cannot be used in HumanTaskActivities, etc.
     * 
     * @param form
     *            the form
     * @return the deployment builder
     */
    DeploymentBuilder addForm(AbstractForm form);

    /**
     * Adds a form from an {@link InputStream}.
     * 
     * @param formName
     *            the form name
     * @param inputStream
     *            the input stream
     * @return the deployment builder
     */
    DeploymentBuilder addInputStreamForm(String formName, InputStream inputStream);

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
    //ProcessDefinitionBuilder getProcessDefinitionBuilder();

    /**
     * Adds a class to the deployment. This will be visible during every execution of the contained
     * 
     * @param className
     *            the classes name
     * @param classData
     *            the class data {@link ProcessDefinition}s.
     */
    void addClass(String className, byte[] classData);

}
