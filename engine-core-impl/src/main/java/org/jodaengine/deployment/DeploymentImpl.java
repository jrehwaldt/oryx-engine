package org.jodaengine.deployment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jodaengine.allocation.AbstractForm;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;

/**
 * The Class DeploymentImpl is a container for resources that should be deployed in the same scope.
 */
public class DeploymentImpl implements Deployment {
    
    private Set<ProcessDefinition> definitions;
    private Map<String, AbstractProcessArtifact> artifacts;
    private Map<String, AbstractForm> forms;
    private Map<String, byte[]> customClasses;
    
    /**
     * Instantiates a new deployment impl.
     */
    public DeploymentImpl() {
        definitions = new HashSet<ProcessDefinition>();
        artifacts = new HashMap<String, AbstractProcessArtifact>();
        forms = new HashMap<String, AbstractForm>();
        customClasses = new HashMap<String, byte[]>();
    }

    @Override
    public void addProcessDefinition(ProcessDefinition definition) {

        definitions.add(definition);
        
    }

    @Override
    public void addProcessArtifact(AbstractProcessArtifact artifact) {

        artifacts.put(artifact.getID(), artifact);
        
    }

    @Override
    public Set<ProcessDefinition> getDefinitions() {

        return definitions;
    }

    @Override
    public Map<String, AbstractProcessArtifact> getArtifacts() {

        return artifacts;
    }

    @Override
    public void addClass(String className, byte[] classData) {

        customClasses.put(className, classData);
        
    }

    @Override
    public Map<String, byte[]> getClasses() {

        return customClasses;
    }

    @Override
    public void addForm(AbstractForm form) {

        forms.put(form.getID(), form);
        
    }

    @Override
    public Map<String, AbstractForm> getForms() {

        return forms;
    }

    
}
