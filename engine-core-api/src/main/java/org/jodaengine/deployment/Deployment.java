package org.jodaengine.deployment;

import java.util.Set;

import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;

/**
 * The deployment is a container used to be able to deploy a process definition together with forms, custom data types
 * etc.
 */
public interface Deployment {
    
   void addProcessDefinition(ProcessDefinition definition);
   
   void addProcessArtifact(AbstractProcessArtifact artifact);
   
   Set<ProcessDefinition> getDefinitions();
   
   Set<AbstractProcessArtifact> getArtifacts();

}
