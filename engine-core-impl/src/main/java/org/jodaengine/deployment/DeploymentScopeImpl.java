package org.jodaengine.deployment;

import java.util.Collections;
import java.util.Map;

import org.jodaengine.process.definition.AbstractProcessArtifact;

/**
 * The Class DeploymentScopeImpl.
 */
public class DeploymentScopeImpl implements DeploymentScope {
    
    private Map<String, AbstractProcessArtifact> artifactsTable;
    
    public DeploymentScopeImpl(Map<String, AbstractProcessArtifact> artifacts) {
        this.artifactsTable = artifacts;
    }

    @Override
    public Map<String, AbstractProcessArtifact> getArtifactsTable() {

        return Collections.unmodifiableMap(artifactsTable);
    }

    @Override
    public AbstractProcessArtifact getProcessArtifact(String identifier) {

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

}
