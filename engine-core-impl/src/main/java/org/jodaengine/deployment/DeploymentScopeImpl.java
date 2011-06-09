package org.jodaengine.deployment;

import java.util.Collections;
import java.util.Map;

import org.jodaengine.exception.ProcessArtifactNotFoundException;
import org.jodaengine.forms.AbstractForm;
import org.jodaengine.process.definition.AbstractProcessArtifact;

/**
 * The Class DeploymentScopeImpl.
 */
public class DeploymentScopeImpl implements DeploymentScope {
    
    private Map<String, AbstractProcessArtifact> artifactsTable;
    private Map<String, AbstractForm> formsTable;
    private CustomClassLoader classLoader;
    
    /**
     * Instantiates a new deployment scope impl with the given artifacts table.
     *
     * @param artifacts the artifacts
     * @param forms the forms
     */
    public DeploymentScopeImpl(Map<String, AbstractProcessArtifact> artifacts, Map<String, AbstractForm> forms) {
        this.artifactsTable = artifacts;
        this.formsTable = forms;
        
        // gets the context loader as a parent, so all the org.jodaengine-classes are available to the new class loader.
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        this.classLoader = new CustomClassLoader(loader);
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
    public AbstractProcessArtifact deleteProcessArtifact(String identifier) {

        return artifactsTable.remove(identifier);
    }

    @Override
    public void addClass(String className, byte[] classData) {

        classLoader.addLoadableClass(className, classData);
        
    }

    @Override
    public Class<?> getClass(String className) throws ClassNotFoundException {

        return classLoader.findClass(className);
    }

    @Override
    public void addForm(AbstractForm form) {

        formsTable.put(form.getID(), form);
        
    }

    @Override
    public void deleteForm(String formID) {

        formsTable.remove(formID);
        
    }

    @Override
    public AbstractForm getForm(String formID) throws ProcessArtifactNotFoundException {

        AbstractProcessArtifact form  = formsTable.get(formID);
        if (form == null) {
            throw new ProcessArtifactNotFoundException(formID);
        }
        return formsTable.get(formID);
    }

}
