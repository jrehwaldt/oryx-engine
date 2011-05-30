package org.jodaengine.ext.debugging.listener;

import javax.annotation.Nonnull;

import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.ext.Extension;
import org.jodaengine.ext.debugging.DebuggerServiceImpl;
import org.jodaengine.ext.debugging.api.DebuggerService;
import org.jodaengine.ext.debugging.shared.DebuggerAttribute;
import org.jodaengine.ext.listener.RepositoryDeploymentListener;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This implementation is triggered when the repository deploys an
 * artifact or a whole deployment.
 * 
 * It will react on {@link ProcessDefinition} deployments
 * and register the therein defined {@link Breakpoint}s
 * within the {@link DebuggerServiceImpl}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-29
 */
@Extension(DebuggerService.DEBUGGER_SERVICE_NAME)
public class DebuggerRepositoryDeploymentListener implements RepositoryDeploymentListener {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private DebuggerServiceImpl debugger;
    
    /**
     * Default constructor.
     * 
     * @param debugger the {@link DebuggerServiceImpl}
     */
    public DebuggerRepositoryDeploymentListener(@Nonnull DebuggerServiceImpl debugger) {
        this.debugger = debugger;
    }

    @Override
    public void definitionDeployed(RepositoryService repository,
                                   ProcessDefinition definition) {
        
        logger.debug("Definition {} deployed", definition);
        DebuggerAttribute attribute = DebuggerAttribute.getAttributeIfExists(definition);
        if (attribute == null) {
            return;
        }
        
        this.debugger.registerBreakpoints(attribute.getBreakpoints(), definition);
    }

    @Override
    public void definitionDeleted(RepositoryService repository,
                                  ProcessDefinition definition) {
        
        logger.debug("Definition {} deleted", definition);
        this.debugger.unregisterBreakpoints(definition);
    }

    @Override
    public void deploymentDeployed(RepositoryService repository,
                                   Deployment deployment) {
        
        logger.debug("Skipping deployment {} deployed", deployment);
        
    }

    @Override
    public void deploymentDeleted(RepositoryService repository,
                                  Deployment deployment) {
        
        logger.debug("Skipping deployment {} deleted", deployment);
        
    }

    @Override
    public void artifactDeployed(RepositoryService repository,
                                 AbstractProcessArtifact artifact) {
        
        logger.debug("Skipping artifact {} deployed", artifact);
        
    }

    @Override
    public void artifactDeleted(RepositoryService repository,
                                AbstractProcessArtifact artifact) {
        
        logger.debug("Skipping artifact {} deleted", artifact);
        
    }
}
