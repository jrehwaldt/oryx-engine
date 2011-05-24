package org.jodaengine.deployment;

import java.io.File;
import java.io.InputStream;

import org.jodaengine.allocation.AbstractForm;
import org.jodaengine.allocation.Form;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.resource.allocation.FormImpl;
import org.jodaengine.util.io.ClassPathResourceStreamSource;
import org.jodaengine.util.io.FileStreamSource;
import org.jodaengine.util.io.InputStreamSource;
import org.jodaengine.util.io.StreamSource;
import org.jodaengine.util.io.StringStreamSource;


/**
 * The Class DeployerImpl. It allows to deploy a process definition for a specific navigator. During this, start events
 * are registered.
 */
public class DeploymentBuilderImpl implements DeploymentBuilder {

    
    private Deployment currentDeployment;
    
    /**
     * Instantiates a new deployment builder Impl with a new empty deployment.
     */
    public DeploymentBuilderImpl() {
        currentDeployment = new DeploymentImpl();
    }
    

    @Override
    public ProcessDefinitionBuilder getProcessDefinitionBuilder() {

        return new ProcessDefinitionBuilderImpl();
    }



    @Override
    public DeploymentBuilder addProcessDefinition(ProcessDefinition definition) {

        this.currentDeployment.addProcessDefinition(definition);
        return this;
        
    }

    @Override
    public DeploymentBuilder addProcessArtifact(AbstractProcessArtifact artifact) {

        this.currentDeployment.addProcessArtifact(artifact);
        return this;
        
    }

    @Override
    public Deployment buildDeployment() {

        Deployment returnDeployment = currentDeployment;
        currentDeployment = new DeploymentImpl();
        return returnDeployment;
    }


    @Override
    public DeploymentBuilder addInputStreamArtifact(String resourceName, InputStream inputStream) {

        StreamSource inputStreamSource = new InputStreamSource(inputStream);
        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, inputStreamSource);

        addProcessArtifact(processArtifact);
        
        return this;
    }


    @Override
    public DeploymentBuilder addClasspathResourceArtifact(String resourceName, String resourceClasspath) {

        StreamSource classpathResourceStreamSource = new ClassPathResourceStreamSource(resourceClasspath);
        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, classpathResourceStreamSource);

        addProcessArtifact(processArtifact);
        return this;
    }


    @Override
    public DeploymentBuilder addStringArtifact(String resourceName, String resourceStringContent) {

        StreamSource stringStreamSource = new StringStreamSource(resourceStringContent);
        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, stringStreamSource);

        addProcessArtifact(processArtifact);
        return this;
    }


    @Override
    public DeploymentBuilder addFileArtifact(String resourceName, File file) {

        StreamSource fileStreamSource = new FileStreamSource(file);
        AbstractProcessArtifact processArtifact = new ProcessArtifact(resourceName, fileStreamSource);
      
        addProcessArtifact(processArtifact);
        return this;
    }


    @Override
    public void addClass(String className, byte[] classData) {

        currentDeployment.addClass(className, classData);
        
    }


    @Override
    public DeploymentBuilder addInputStreamForm(String formName, InputStream inputStream) {

        StreamSource inputStreamSource = new InputStreamSource(inputStream);
        AbstractForm form = new FormImpl(formName, inputStreamSource);
        addForm(form);
        return this;
    }


    @Override
    public DeploymentBuilder addForm(AbstractForm form) {

        currentDeployment.addForm(form);
        return this;
    }

}
