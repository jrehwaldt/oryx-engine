package org.jodaengine.deployment;

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

        return artifactsTable;
    }

    @Override
    public AbstractProcessArtifact getProcessArtifact(String identifier) {

        return artifactsTable.get(identifier);
    }

}
