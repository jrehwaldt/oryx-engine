package org.jodaengine.deployment;

import java.util.HashSet;
import java.util.Set;

import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;

/**
 * The Class DeploymentImpl is a container for resources that should be deployed in the same scope.
 */
public class DeploymentImpl implements Deployment {
    
    private Set<ProcessDefinition> definitions;
    private Set<AbstractProcessArtifact> artifacts;
    
    /**
     * Instantiates a new deployment impl.
     */
    public DeploymentImpl() {
        definitions = new HashSet<ProcessDefinition>();
        artifacts = new HashSet<AbstractProcessArtifact>();
    }

    @Override
    public void addProcessDefinition(ProcessDefinition definition) {

        definitions.add(definition);
        
    }

    @Override
    public void addProcessArtifact(AbstractProcessArtifact artifact) {

        artifacts.add(artifact);
        
    }

    @Override
    public Set<ProcessDefinition> getDefinitions() {

        return definitions;
    }

    @Override
    public Set<AbstractProcessArtifact> getArtifacts() {

        return artifacts;
    }

    
}
