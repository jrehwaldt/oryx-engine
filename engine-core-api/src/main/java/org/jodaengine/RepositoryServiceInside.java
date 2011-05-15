package org.jodaengine;

import java.util.UUID;

import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.process.definition.ProcessDefinitionInside;

/**
 * Extends the RepositoryServiceInterface so that it provides more methods for the internal classes.
 * 
 * This Interface is expected to be used by internal classes.
 */
public interface RepositoryServiceInside extends RepositoryService {

    public ProcessDefinitionInside getProcessDefinitionInside(UUID processDefintionID)
    throws DefinitionNotFoundException;
}
