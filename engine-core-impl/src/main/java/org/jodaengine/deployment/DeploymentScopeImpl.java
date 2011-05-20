package org.jodaengine.deployment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.process.definition.AbstractProcessArtifact;

/**
 * The Class DeploymentScopeImpl.
 */
public class DeploymentScopeImpl implements DeploymentScope {
    
    private Map<String, AbstractProcessArtifact> artifactsTable;
    private CustomClassLoader classLoader;
    
    /**
     * Instantiates a new deployment scope impl with the given artifacts table.
     *
     * @param artifacts the artifacts
     */
    public DeploymentScopeImpl(Map<String, AbstractProcessArtifact> artifacts) {
        this.artifactsTable = artifacts;
        this.classLoader = new CustomClassLoader();
    }

    @Override
    public Map<String, AbstractProcessArtifact> getArtifactsTable() {

        return Collections.unmodifiableMap(artifactsTable);
    }

    @Override
    public AbstractProcessArtifact getProcessArtifact(String identifier) throws ProcessArtifactNotFoundException {

        AbstractProcessArtifact artifact  = artifactsTable.get(identifier);
        if (artifact == null) {
            throw new ProcessArtifactNotFoundException(identifier);
        }
        return artifactsTable.get(identifier);
    }

    @Override
    public void addProcessArtifact(AbstractProcessArtifact artifact) {

        artifactsTable.put(artifact.getID(), artifact);
        
    }

    @Override
    public void deleteProcessArtifact(String identifier) {

        artifactsTable.remove(identifier);
        
    }

    @Override
    public void addClass(String className, byte[] classData) {

        classLoader.addLoadableClass(className, classData);
        
    }

    @Override
    public Class<?> getClass(String className) {

        return classLoader.findClass(className);
    }

}
