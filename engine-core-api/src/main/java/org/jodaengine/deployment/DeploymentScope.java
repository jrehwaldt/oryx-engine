package org.jodaengine.deployment;

import java.util.Map;

import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.resource.allocation.AbstractForm;

/**
 * The DeploymentScope realizes the scoping of process resources.
 */
public interface DeploymentScope {

    /**
     * Gets the table of all artifacts that are available in this scope.
     * Returns the artifacts as an unmodifiable (i.e. read-only) map.
     *
     * @return the artifacts table as an unmodifiable Map
     */
    Map<String, AbstractProcessArtifact> getArtifactsTable();
    
    /**
     * Gets the process artifact with the given identifier.
     *
     * @param identifier the identifier
     * @return the process artifact
     * @throws ProcessArtifactNotFoundException thrown if the artifact does not exist in this scope
     */   
    AbstractProcessArtifact getProcessArtifact(String identifier) throws ProcessArtifactNotFoundException;
    
    /**
     * Adds a process artifact to the scope.
     *
     * @param artifact the artifact
     */
    void addProcessArtifact(AbstractProcessArtifact artifact);
    
    /**
     * Deletes a process artifact from the scope.
     *
     * @param identifier the identifier
     */
    void deleteProcessArtifact(String identifier);
        
    /**
     * Adds a custom class to this scope.
     *
     * @param className the class name
     * @param classData the class data
     */
    void addClass(String className, byte[] classData);
    
    /**
     * Gets a class that was deployed to this scope.
     *
     * @param className the class name
     * @return the class
     * @throws ClassNotFoundException thrown if the class does not exist in this scope
     */
    Class<?> getClass(String className) throws ClassNotFoundException;
    
    /**
     * Adds a form to this scope.
     *
     * @param form the form
     */
    void addForm(AbstractForm form);
    
    /**
     * Deletes a form from this scope.
     *
     * @param formID the form id
     */
    void deleteForm(String formID);
    
    /**
     * Gets a form from this scope identified by the id.
     *
     * @param formID the form id
     * @return the form
     * @throws ProcessArtifactNotFoundException the process artifact not found exception
     */
    AbstractForm getForm(String formID) throws ProcessArtifactNotFoundException;
}
