package org.jodaengine;

import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.ProcessDefinitionInside;

/**
 * Extends the RepositoryServiceInterface so that it provides more methods for the internal classes.
 * 
 * This Interface is expected to be used by internal classes.
 */
public interface RepositoryServiceInside extends RepositoryService {

    ProcessDefinitionInside getProcessDefinitionInside(ProcessDefinitionID processDefintionID)
    throws DefinitionNotFoundException;
}
