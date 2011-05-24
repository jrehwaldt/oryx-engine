package org.jodaengine.deployment;

import java.util.Map;

import org.jodaengine.allocation.AbstractForm;
import org.jodaengine.allocation.Form;
import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.process.definition.AbstractProcessArtifact;

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
        
    void addClass(String className, byte[] classData);
    
    Class<?> getClass(String className) throws ClassNotFoundException;
    
    void addForm(AbstractForm form);
    
    void deleteForm(String formID);
    
    AbstractForm getForm(String formID) throws ProcessArtifactNotFoundException;
}
