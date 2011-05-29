package org.jodaengine.ext.listener;

import javax.annotation.Nonnull;

import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;

/**
 * An implementation may be injected to observe certain events of a {@link RepositoryService}.
*/
public interface RepositoryDeploymentListener {
   
   /**
    * A definition was deployed.
    *
    * @param repository the {@link RepositoryService}
    * @param definition the {@link ProcessDefinition}, which was deployed
    */
   void definitionDeployed(@Nonnull RepositoryService repository,
                           @Nonnull ProcessDefinition definition);
   
   /**
    * A definition was deleted.
    *
    * @param repository the {@link RepositoryService}
    * @param definition the {@link ProcessDefinition}, which was deleted
    */
   void definitionDeleted(@Nonnull RepositoryService repository,
                          @Nonnull ProcessDefinition definition);
   
   /**
    * A deployment was deployed.
    *
    * @param repository the {@link RepositoryService}
    * @param deployment the {@link Deployment}, which was deployed
    */
   void deploymentDeployed(@Nonnull RepositoryService repository,
                           @Nonnull Deployment deployment);
   
   /**
    * A deployment was deleted.
    *
    * @param repository the {@link RepositoryService}
    * @param deployment the {@link Deployment}, which was deleted
    */
   void deploymentDeleted(@Nonnull RepositoryService repository,
                          @Nonnull Deployment deployment);
   
   /**
    * An artifact was deployed.
    *
    * @param repository the {@link RepositoryService}
    * @param artifact the {@link AbstractProcessArtifact}, which was deployed
    */
   void artifactDeployed(@Nonnull RepositoryService repository,
                         @Nonnull AbstractProcessArtifact artifact);
   
   /**
    * An artifact was deleted.
    *
    * @param repository the {@link RepositoryService}
    * @param artifact the {@link AbstractProcessArtifact}, which was deleted
    */
   void artifactDeleted(@Nonnull RepositoryService repository,
                        @Nonnull AbstractProcessArtifact artifact);
}
