package org.jodaengine.deployment;

import java.util.Map;
import java.util.Set;

import org.jodaengine.forms.AbstractForm;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;

/**
 * The deployment is a container used to be able to deploy a process definition together with forms, custom data types
 * etc.
 */
public interface Deployment {

    /**
     * Adds a {@link ProcessDefinition} to the deployment. Definitions with the same {@link ProcessDefinitionID} are
     * allowed, as they will have different versions assigned upon deployment.
     * 
     * @param definition
     *            the definition
     */
    void addProcessDefinition(ProcessDefinition definition);

    /**
     * Adds an {@link AbstractProcessArtifact} to the deployment. Will override any artifact with the same id.
     * 
     * @param artifact
     *            the artifact
     */
    void addProcessArtifact(AbstractProcessArtifact artifact);
    
    /**
     * Adds an {@link AbstractForm} to the deployment.
     *
     * @param form the form
     */
    void addForm(AbstractForm form);
    
    /**
     * Gets {@link Map} of all contained {@link AbstractForm}s.
     *
     * @return the forms
     */
    Map<String, AbstractForm> getForms();

    /**
     * Gets all contained {@link ProcessDefinition}.
     * 
     * @return the definitions
     */
    Set<ProcessDefinition> getDefinitions();

    /**
     * Gets a map of all contained artifact identifiers pointing to the specific {@link AbstractProcessArtifact}s.
     * 
     * @return the artifacts
     */
    Map<String, AbstractProcessArtifact> getArtifacts();

    /**
     * Adds a class to this deployment. It can be used in the context of every contained {@link ProcessDefinition}.
     * 
     * @param className
     *            the class name
     * @param classData
     *            the class data
     */
    void addClass(String className, byte[] classData);

    /**
     * Returns a Map of all classes, that were added to this deployment.
     * 
     * @return the classes
     */
    Map<String, byte[]> getClasses();

}
