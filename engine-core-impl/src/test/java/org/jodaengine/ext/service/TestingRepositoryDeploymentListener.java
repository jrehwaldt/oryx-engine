package org.jodaengine.ext.service;

import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.ext.Extension;
import org.jodaengine.ext.listener.RepositoryDeploymentListener;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.testng.Assert;

/**
 * Listener implementation for testing the {@link ExtensionService} integration in
 * our {@link RepositoryService}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-29
 */
@Extension("testing-repository-integration")
public class TestingRepositoryDeploymentListener implements RepositoryDeploymentListener {
    
    protected TestingListenerExtensionService listenerService;
    
    /**
     * Default constructor.
     * 
     * @param listenerService the listener service
     */
    public TestingRepositoryDeploymentListener(TestingListenerExtensionService listenerService) {
        Assert.assertNotNull(listenerService);
        this.listenerService = listenerService;
        this.listenerService.registered(this);
    }

    @Override
    public void definitionDeployed(RepositoryService repository,
                                   ProcessDefinition definition) {
        this.listenerService.invoked(this);
    }

    @Override
    public void definitionDeleted(RepositoryService repository,
                                  ProcessDefinition definition) {
        this.listenerService.invoked(this);
    }

    @Override
    public void deploymentDeployed(RepositoryService repository,
                                   Deployment deployment) {
        this.listenerService.invoked(this);
    }

    @Override
    public void deploymentDeleted(RepositoryService repository,
                                  Deployment deployment) {
        this.listenerService.invoked(this);
    }

    @Override
    public void artifactDeployed(RepositoryService repository,
                                 AbstractProcessArtifact artifact) {
        this.listenerService.invoked(this);
    }

    @Override
    public void artifactDeleted(RepositoryService repository,
                                AbstractProcessArtifact artifact) {
        this.listenerService.invoked(this);
    }
}
